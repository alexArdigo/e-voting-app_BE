package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.CandidateDTO;
import com.iscte_meta_systems.evoting_server.repositories.CandidateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    ElectionService electionService;

    @Override
    public List<CandidateDTO> getCandidatesByElection(Long electionId) {

        Election election = electionService.getElectionById(electionId);

        List<Organisation> organisations = election.getOrganisations();

        List<Candidate> allCandidates = new ArrayList<>();

        for (Organisation organisation : organisations) {
            if (organisation instanceof Party) {
                Party party = (Party) organisation;
                if (party.getCandidates() != null) {
                    allCandidates.addAll(party.getCandidates());
                }
            } else if (organisation instanceof UniParty) {
                UniParty uniParty = (UniParty) organisation;
                if (uniParty.getCandidate() != null) {
                    allCandidates.add(uniParty.getCandidate());
                }
            }
        }

        return allCandidates.stream()
                .map(CandidateDTO::new)
                .toList();
    }


    @Override
    public CandidateDTO getCandidatesById(long id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Candidate with id " + id + " not found"));
        return new CandidateDTO(candidate);
    }

    @Override
    public CandidateDTO addCandidate(CandidateDTO candidate) {
        Candidate candidateExists = candidateRepository.findByName(candidate.getName());
        if (candidateExists != null)
            throw new RuntimeException("Candidate already exists");
        Candidate newCandidate = new Candidate();
        newCandidate.setName(candidate.getName());
        newCandidate.setImageUrl(candidate.getImageUrl());
        candidateRepository.save(newCandidate);
        return candidate;
    }


}
