package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.ElectionResultDTO;
import com.iscte_meta_systems.evoting_server.model.LegislativeResultDTO;
import com.iscte_meta_systems.evoting_server.model.OrganisationResultDTO;
import com.iscte_meta_systems.evoting_server.repositories.ElectoralCircleRepository;
import com.iscte_meta_systems.evoting_server.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultsServiceImpl implements ResultsService {

    @Autowired
    private ElectionService electionService;
    @Autowired
    private ElectoralCircleRepository electoralCircleRepository;
    @Autowired
    private VoteRepository voteRepository;


    @Override
    public ElectionResultDTO getPresidentialResults(Long electionId) {
        Election election = electionService.getElectionById(electionId);

        if (!(election instanceof Presidential)) {
            throw new IllegalArgumentException("Election is not a presidential");
        }
        Map<Long, Integer> voteCounts = countVotesByOrganisation(election);
        int totalVotes = voteCounts.values().stream().mapToInt(Integer::intValue).sum();

        ElectionResultDTO result = new ElectionResultDTO();
        result.setElectionName(election.getName());
        result.setElectionType("presidential");
        result.setTotalVotes(totalVotes);

        List<OrganisationResultDTO> organisationResults = new ArrayList<>();

        for (Organisation org : election.getOrganisations()) {
            OrganisationResultDTO orgResult = new OrganisationResultDTO();
            orgResult.setOrganisationName(org.getOrganisationName());

            int votes = voteCounts.get(org.getId());
            orgResult.setVotes(votes);
            orgResult.setPercentage((double) (votes * 100) / totalVotes);
            orgResult.setSeats(0);

            organisationResults.add(orgResult);
        }

        organisationResults.sort((o1, o2) -> Double.compare(o2.getPercentage(), o1.getPercentage()));
        result.setResults(organisationResults);

        return result;
    }

    @Override
    public LegislativeResultDTO getLegislativeResults(Long electionId) {
        ElectoralCircle electoralCircle = electoralCircleRepository.findById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Electoral circle not found"));

        return calculateElectoralCircleResults(electoralCircle);
    }

    @Override
    public List<LegislativeResultDTO> getAllLegislativeResults(Long electoralCircleId) {

        ElectoralCircle electoralCircle = electoralCircleRepository.findById(electoralCircleId).orElseThrow(() -> new IllegalArgumentException("Electoral circle not found"));
        List<ElectoralCircle> allCircles = electoralCircleRepository.findAll();
        List<LegislativeResultDTO> results = new ArrayList<>();

        List<Vote> votes = voteRepository.findByDistrictName(electoralCircle.getDistricts().getDistrictName());

        for (ElectoralCircle circle : allCircles) {
            if (circle.getVotes() != null && !circle.getVotes().isEmpty()) {
                results.add(calculateElectoralCircleResults(circle));
            }
        }
        return results;
    }

    private LegislativeResultDTO calculateElectoralCircleResults(ElectoralCircle electoralCircle) {
        Map<Long, Integer> voteCounts = countVotesByOrganisation(electoralCircle);
        int totalVotes = voteCounts.values().stream().mapToInt(Integer::intValue).sum();
        int totalSeats = electoralCircle.getSeats();

        Map<Long, Integer> seatDistribution = calculateDHondtSeats(voteCounts, totalSeats);

        LegislativeResultDTO result = new LegislativeResultDTO();
        result.setElectionName(electoralCircle.getName());
        result.setDistrictName(getDistrictName(electoralCircle));
        result.setTotalSeats(totalSeats);
        result.setTotalVotes(totalVotes);

        List<OrganisationResultDTO> organisationResults = new ArrayList<>();

        if (electoralCircle.getOrganisations() != null) {
            for (Organisation org : electoralCircle.getOrganisations()) {
                OrganisationResultDTO orgResult = new OrganisationResultDTO();
                orgResult.setOrganisationName(org.getOrganisationName());

                int votes = voteCounts.getOrDefault(org.getId(), 0);
                orgResult.setVotes(votes);
                orgResult.setPercentage(totalVotes > 0 ? (votes * 100.0) / totalVotes : 0.0);
                orgResult.setSeats(seatDistribution.getOrDefault(org.getId(), 0));

                int seats = orgResult.getSeats();
                List<String> electedCandidates = new ArrayList<>();
                if (org instanceof Party) {
                    List<Candidate> candidates = ((Party) org).getCandidates();
                    if (candidates != null) {
                        electedCandidates = candidates.stream()
                                .limit(seats)
                                .map(Candidate::getName)
                                .collect(Collectors.toList());
                    }
                }
                orgResult.setElectedCandidates(electedCandidates);

                organisationResults.add(orgResult);
            }
        }

        organisationResults.sort((a, b) -> Integer.compare(b.getVotes(), a.getVotes()));
        result.setResults(organisationResults);

        return result;
    }

    private String getDistrictName(ElectoralCircle electoralCircle) {
        if (electoralCircle.getDistricts() != null) {
            return electoralCircle.getDistricts().getDistrictName();
        }
        return "Unknown District";
    }

    private Map<Long, Integer> countVotesByOrganisation(Election election) {
        Map<Long, Integer> voteCounts = new HashMap<>();

        if (election.getVotes() != null) {
            for (Vote vote : election.getVotes()) {
                if (vote.getOrganisation() != null) {
                    Long organizationId = vote.getOrganisation().getId();
                    voteCounts.put(organizationId, voteCounts.getOrDefault(organizationId, 0) + 1);
                }
            }
        }

        return voteCounts;
    }

    private Map<Long, Integer> calculateDHondtSeats(Map<Long, Integer> voteCounts, int totalSeats) {
        Map<Long, Integer> seats = new HashMap<>();

        for (Long organizationId : voteCounts.keySet()) {
            seats.put(organizationId, 0);
        }

        for (int seat = 0; seat < totalSeats; seat++) {
            Long winnerOrgId = null;
            double highestQuotient = 0;

            for (Map.Entry<Long, Integer> entry : voteCounts.entrySet()) {
                Long organizationId = entry.getKey();
                int votes = entry.getValue();

                if (votes > 0) {
                    double quotient = (double) votes / (seats.get(organizationId) + 1);

                    if (quotient > highestQuotient) {
                        highestQuotient = quotient;
                        winnerOrgId = organizationId;
                    }
                }
            }

            if (winnerOrgId != null) {
                seats.put(winnerOrgId, seats.get(winnerOrgId) + 1);
            }
        }

        return seats;
    }

    @Override
    public List<LegislativeResultDTO> getAllLegislativeResultsByYear(int year) {

        List<ElectoralCircle> allCircles = electoralCircleRepository.findAll();
        List<ElectoralCircle> e = new ArrayList<>();
        List<LegislativeResultDTO> results = new ArrayList<>();

        for (ElectoralCircle circle : allCircles) {
            if (circle.getVotes() != null && !circle.getVotes().isEmpty() && circle.getStartDate().getYear() == year) {
                results.add(calculateElectoralCircleResults(circle));
            }
        }
        return results;
    }

    @Override
    public Map<String, Integer> getLegislativeSeatsByPartyForYear(int year) {
        List<ElectoralCircle> allCircles = electoralCircleRepository.findAll();
        Map<String, Integer> partySeatsMap = new HashMap<>();

        for (ElectoralCircle circle : allCircles) {
            if (circle.getStartDate() != null && circle.getStartDate().getYear() == year) {
                List<Vote> votes = circle.getVotes();
                if (votes == null || votes.isEmpty()) continue;

                Map<Long, Integer> voteCounts = new HashMap<>();
                Map<Long, Organisation> organisationsById = new HashMap<>();

                for (Vote vote : votes) {
                    Organisation org = vote.getOrganisation();
                    if (org != null) {
                        Long organizationId = org.getId();
                        voteCounts.put(organizationId, voteCounts.getOrDefault(organizationId, 0) + 1);
                        organisationsById.putIfAbsent(organizationId, org);
                    }
                }

                Map<Long, Integer> seatDistribution = calculateDHondtSeats(voteCounts, circle.getSeats());

                for (Map.Entry<Long, Integer> entry : seatDistribution.entrySet()) {
                    Long organizationId = entry.getKey();
                    int seats = entry.getValue();

                    Organisation org = organisationsById.get(organizationId);
                    if (org != null) {
                        String partyName = org.getOrganisationName();
                        partySeatsMap.put(partyName, partySeatsMap.getOrDefault(partyName, 0) + seats);
                    }
                }
            }
        }

        return partySeatsMap;
    }

}