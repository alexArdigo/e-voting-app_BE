package com.iscte_meta_systems.evoting_server.services;
import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.model.DistrictStatisticsDTO;
import com.iscte_meta_systems.evoting_server.model.MunicipalityStatisticsDTO;
import com.iscte_meta_systems.evoting_server.model.PartyVoteStatsDTO;
import com.iscte_meta_systems.evoting_server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService{

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private ElectoralCircleRepository electoralCircleRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private DistrictRepository districtRepository;


    @Override
    public List<PartyVoteStatsDTO> getVotePercentagesByPartyByDistrict(Long electionId, String districtName) {

//        Election election = electionRepository.findById(electionId).orElse(null);
//
//        if (election == null || districtName == null) {
//            throw new IllegalArgumentException("District name or election ID cannot be null");
//        }
//
//        if(!(election instanceof ElectoralCircle)) {
//            throw new IllegalArgumentException("Election does not belong to the specified district");
//        }
//
//        ElectoralCircle electoralCircle = (ElectoralCircle) election;
//
//        if (!electoralCircle.getDistricts().getDistrictName().equalsIgnoreCase(districtName)) {
//            throw new IllegalArgumentException("Election does not belong to the specified district");
//        }
//
//        List<Vote> votes = electoralCircle.getVotes();
//
//        if (votes.isEmpty()) {
//            throw new RuntimeException("No votes found for the specified election in the district");
//        }
//
//        Map<String, Integer> votesByParty = new HashMap<>();
//
//        for(Vote i : votes){
//            Organisation org = i.getOrganisation();
//            if(org instanceof Party){
//                String partyName = org.getOrganisationName();
//                int currentVotes = votesByParty.containsKey(partyName) ? votesByParty.get(partyName) : 0;
//                votesByParty.put(partyName, currentVotes + 1);
//            }
//        }
//
//        List<PartyVoteStatsDTO> partyVotesByDistrictPercentage = votesByParty.entrySet().stream()
//                .map(entry -> {
//                    String partyName = entry.getKey();
//                    int voteCount = entry.getValue();
//                    double percentage = (double) voteCount / votes.size() * 100;
//                    return new PartyVoteStatsDTO(partyName, percentage);
//                })
//                .toList();
//
//        return partyVotesByDistrictPercentage;
        return null;
    }

    @Override
    public int getTotalVotesByPartyByDistrict(String partyName, String districtName) {

        ElectoralCircle electoralCircle = electoralCircleRepository.findByDistricts_DistrictName(districtName);
        List<Organisation> org = electoralCircle.getOrganisations();

        return 0;
    }

    @Override
    public int getTotalVotesByElection(Long electionId) {

        Election election = electionRepository.findById(electionId).orElse(null);

        if (election == null) {
            throw new IllegalArgumentException("Election with ID " + electionId + " does not exist.");
        }
        List<Vote> votes = election.getVotes();
        if (votes == null || votes.isEmpty()) {
            System.out.println("No votes found for election with ID " + electionId);
            return 0;
        }
        return votes.size();
    }

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

            List<PartyVoteStatsDTO> partyResults = new ArrayList<>();
            for (Map.Entry<Long, Integer> orgEntry : organisationVotes.entrySet()) {
                Vote sampleVote = municipalityVotes.stream()
                        .filter(vote -> vote.getOrganisation().getId().equals(orgEntry.getKey()))
                        .findFirst()
                        .orElse(null);

                if (sampleVote != null) {
                    PartyVoteStatsDTO partyDTO = new PartyVoteStatsDTO();
                    partyDTO.setOrganisationId(orgEntry.getKey());
                    partyDTO.setOrganisationName(sampleVote.getOrganisation().getOrganisationName());
                    partyDTO.setOrganisationType(ElectoralCircleType.valueOf(sampleVote.getOrganisation().getClass().getSimpleName()));
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

    @Override
    public int getVotesByPartyByElectoralCircle(String partyName, Long electoralCircleId) {
        Election election = (ElectoralCircle) electionRepository.findById(electoralCircleId).orElse(null);

        if (election == null) {
            throw new IllegalArgumentException("Electoral Circle with ID " + electoralCircleId + " does not exist.");
        }

        List<Vote> votes = election.getVotes();

        if (votes == null || votes.isEmpty()) {
            System.out.println("No votes found for electoral circle with ID " + electoralCircleId);
            return 0;
        }

        int count = 0;

        for (Vote vote : votes) {
            if (vote.getOrganisation() instanceof Party && vote.getOrganisation().getOrganisationName().equalsIgnoreCase(partyName)) {
                count++;
            }
        }

        return count;

    }

}


