package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import com.iscte_meta_systems.evoting_server.repositories.ElectionRepository;
import com.iscte_meta_systems.evoting_server.repositories.OrganisationRepository;
import com.iscte_meta_systems.evoting_server.repositories.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectionServiceImpl implements  ElectionService {

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private VoterService voterService;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public List<ElectionDTO> getElections(String electionType, Integer electionYear) {
        List<Election> elections = electionRepository.findAll();

        return elections.stream()
                .filter(e -> electionType == null || e.getClass().getSimpleName().equalsIgnoreCase(electionType))
                .filter(e -> electionYear == null || (e.getStartDate() != null && e.getStartDate().getYear() == electionYear))
                .map(e -> {
                    ElectionDTO dto = new ElectionDTO();
                    dto.setId(e.getId());
                    dto.setName(e.getName());
                    dto.setDescription(e.getDescription());
                    dto.setStartDate(e.getStartDate() != null ? e.getStartDate().toString() : null);
                    dto.setEndDate(e.getEndDate() != null ? e.getEndDate().toString() : null);
                    dto.setElectionType(e.getClass().getSimpleName());

                    if (e.getOrganisations() != null) {
                        List<OrganisationDTO> orgDtos = e.getOrganisations().stream()
                                .map(org -> {
                                    OrganisationDTO orgDto = new OrganisationDTO();
                                    orgDto.setId(org.getId());
                                    orgDto.setName(org.getOrganisationName());
                                    orgDto.setOrganisationType(org.getClass().getSimpleName());
                                    orgDto.setElectionId(org.getElection() != null ? org.getElection().getId() : null);
                                    return orgDto;
                                })
                                .collect(Collectors.toList());
                        dto.setOrganisations(orgDtos);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }



    @Override
    public Election getElectionById(Long id) {
        return electionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Election with ID " + id + " was not found."));
    }

    @Override
    public ElectionDTO createElection(ElectionDTO dto) {
        if (dto.getName() == null) {
            throw new IllegalArgumentException("Election name is required.");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }

        LocalDateTime startDate = LocalDateTime.parse(dto.getStartDate());
        LocalDateTime endDate = LocalDateTime.parse(dto.getEndDate());

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        Election election;
        switch (dto.getElectionType().toLowerCase()) {
//            case "legislativa":
//                election = new Legislativa();
//                break;
            case "presidential":
                election = new Presidential();
                break;
            default:
                throw new IllegalArgumentException("Unknown election type: " + dto.getElectionType());
        }

        election.setName(dto.getName());
        election.setDescription(dto.getDescription());
        election.setStartDate(startDate);
        election.setEndDate(endDate);

        electionRepository.save(election);

        return dto;
    }

    @Override
    public List<Organisation> getBallotByElectionId(Long id) {
        Election election = getElectionById(id);
        return election.getOrganisations();
    }

    @Override
    public Vote castVote(Long electionId, Vote voteRequest) {
        Election election = getElectionById(electionId);
        Voter voter = voterService.getLoggedVoter();
        Parish parish = voter.getParish();
        Organisation organisation = organisationRepository.getReferenceById(voteRequest.getOrganisation().getId());
        Vote vote = new Vote();
        vote.setOrganisation(organisation);
        vote.setParish(parish);
        election.addVote(vote);
        election.addVoted(voter.getHashIdentification());
        electionRepository.save(election);
        return voteRepository.save(vote);

    }

    @Override
    public Boolean isStarted(Long id) {
        Election election = getElectionById(id);
        return election.isStarted();
    }

    @Override
    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    @Override
    public List<Election> getActiveElections() {
        List<Election> elections = electionRepository.findAll();
        List<Election> activeElection = new ArrayList<>();
        for(Election election : elections) {
            if (election.isStarted()){
                activeElection.add(election);
            }
        }
        return activeElection;
    }

    @Override
    public List<Election> getNotActiveElections() {
        List<Election> elections = electionRepository.findAll();
        List<Election> notActiveElections = new ArrayList<>();
        for(Election election : elections) {
            if (!election.isStarted()){
                notActiveElections.add(election);
            }
        }
        return notActiveElections;
    }

    @Override
    public Election startElection(Long id) {
        Election election = getElectionById(id);
        election.startElection();
        return electionRepository.save(election);
    }

    @Override
    public Election endElection(Long id) {
        Election election = getElectionById(id);
        if (!election.isStarted()) {
            throw new IllegalArgumentException("The election with the " + id + " was not found.");
        }
        election.endElection();
        return electionRepository.save(election);
    }

}
