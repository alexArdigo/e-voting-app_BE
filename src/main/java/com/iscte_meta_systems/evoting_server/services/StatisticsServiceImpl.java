package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.District;
import com.iscte_meta_systems.evoting_server.entities.Vote;
import com.iscte_meta_systems.evoting_server.model.DistrictStatisticsDTO;
import com.iscte_meta_systems.evoting_server.model.MunicipalityStatisticsDTO;
import com.iscte_meta_systems.evoting_server.model.PartyVoteDTO;
import com.iscte_meta_systems.evoting_server.repositories.DistrictRepository;
import com.iscte_meta_systems.evoting_server.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public DistrictStatisticsDTO getDistrictStatistics(String districtName) {
        District district = districtRepository.findByDistrictName(districtName);
        if (district == null) {
            throw new IllegalArgumentException("District not found: " + districtName);
        }

        List<Vote> districtVotes = voteRepository.findByDistrictName(districtName);

        Map<String, List<Vote>> votesByMunicipality = districtVotes.stream()
                .collect(Collectors.groupingBy(vote ->
                        vote.getParish().getMunicipality().getMunicipalityName()));

        List<MunicipalityStatisticsDTO> municipalityStats = new ArrayList<>();

        for (Map.Entry<String, List<Vote>> entry : votesByMunicipality.entrySet()) {
            String municipalityName = entry.getKey();
            List<Vote> municipalityVotes = entry.getValue();

            MunicipalityStatisticsDTO municipalityDTO = new MunicipalityStatisticsDTO();
            municipalityDTO.setMunicipalityName(municipalityName);
            municipalityDTO.setTotalVotes(municipalityVotes.size());

            Map<Long, Integer> organisationVotes = municipalityVotes.stream()
                    .filter(vote -> vote.getOrganisation() != null)
                    .collect(Collectors.groupingBy(
                            vote -> vote.getOrganisation().getId(),
                            Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                    ));

            List<PartyVoteDTO> partyResults = new ArrayList<>();
            for (Map.Entry<Long, Integer> orgEntry : organisationVotes.entrySet()) {
                Vote sampleVote = municipalityVotes.stream()
                        .filter(vote -> vote.getOrganisation().getId().equals(orgEntry.getKey()))
                        .findFirst()
                        .orElse(null);

                if (sampleVote != null) {
                    PartyVoteDTO partyDTO = new PartyVoteDTO();
                    partyDTO.setOrganisationId(orgEntry.getKey());
                    partyDTO.setOrganisationName(sampleVote.getOrganisation().getOrganisationName());
                    partyDTO.setOrganisationType(sampleVote.getOrganisation().getClass().getSimpleName());
                    partyDTO.setVotes(orgEntry.getValue());
                    partyDTO.setPercentage((double) (orgEntry.getValue() * 100) / municipalityVotes.size());
                    partyResults.add(partyDTO);
                }
            }

            partyResults.sort((a, b) -> Integer.compare(b.getVotes(), a.getVotes()));
            municipalityDTO.setPartyResults(partyResults);
            municipalityStats.add(municipalityDTO);
        }

        municipalityStats.sort((a, b) -> Integer.compare(b.getTotalVotes(), a.getTotalVotes()));

        DistrictStatisticsDTO result = new DistrictStatisticsDTO();
        result.setDistrictName(districtName);
        result.setTotalVotes(districtVotes.size());
        result.setMunicipalities(municipalityStats);

        return result;
    }
}
