package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.repositories.ElectionRepository;
import com.iscte_meta_systems.evoting_server.repositories.OrganisationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectionServiceImpl implements  ElectionService {

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private VoterService voterService;

    @Autowired
    private OrganisationService organisationService;

//    @Autowired
//    private VoteRepository voteRepository;

    @Override
    public List<Election> getElections(String electionType, Integer electionYear) {
        List<Election> elections = electionRepository.findAll();
        return elections.stream()
                .filter(e -> electionType == null || e.getClass().getSimpleName().equalsIgnoreCase(electionType))
                .filter(e -> electionYear == null || (e.getStartDate() != null && e.getStartDate().getYear() == electionYear))
                .collect(Collectors.toList());
    }

    @Override
    public Election getElectionById(Long id) {
        return electionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Eleição não encontrada com o ID: " + id));
    }

    @Override
    public ElectionDTO createElection(ElectionDTO dto) {
        if (dto.getName() == null) {
            throw new IllegalArgumentException("O nome da eleição é obrigatório.");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Datas de início e fim são obrigatórias.");
        }

        LocalDateTime startDate = LocalDateTime.parse(dto.getStartDate());
        LocalDateTime endDate = LocalDateTime.parse(dto.getEndDate());

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("A data de fim não pode ser anterior à data de início.");
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
                throw new IllegalArgumentException("Tipo de eleição desconhecido: " + dto.getElectionType());
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

//    @Override
//    public Vote castVote(Long electionId, Vote voteRequest) {
//        Election election = getElectionById(electionId);
//        Voter voter = voterService.getLoggedVoter();
//        Parish parish = voter.getParish();
//        Organisation organisation = organisationService.getOrganisationById(voteRequest.getOrganisation().getId());
//        Vote vote = new Vote();
//        vote.setOrganisation(organisation);
//        vote.setParish(parish);
//        electionRepository.save(election);
//        return voteRepository.save(vote);
//
//    }

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
            throw new IllegalArgumentException("A eleição com o ID " + id + " não foi iniciada.");
        }
        election.endElection();
        return electionRepository.save(election);
    }
}
