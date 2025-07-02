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

        List<Candidate> allCandidates = new ArrayList<>();

        for (Organisation organisation : election.getOrganisations()) {
            if (organisation instanceof Party party) {
                if (party.getCandidates() != null) {
                    allCandidates.addAll(party.getCandidates());
                }
            } else if (organisation instanceof UniParty uniParty) {
                if (uniParty.getCandidate() != null) {
                    allCandidates.add(uniParty.getCandidate());
                }
            }
        }

        List<CandidateDTO> result = allCandidates.stream()
                .distinct()
                .map(CandidateDTO::new)
                .toList();
        return result;
    }

    @Override
    public CandidateDTO getCandidatesById(Long id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Candidate with id " + id + " not found"));
        return new CandidateDTO(candidate);
    }

    @Override
    public Candidate addCandidate(CandidateDTO candidate) {
        boolean exists = candidateRepository.existsCandidateByName(candidate.getName());
        if (exists) {
            List<Candidate> candidates = candidateRepository.findAll();
            for (Candidate c : candidates) {
                if (c.getName().equalsIgnoreCase(candidate.getName())) {
                    return c;
                }
            }
            return null;
        }
        Candidate newCandidate = new Candidate();
        newCandidate.setName(candidate.getName());
        newCandidate.setImageUrl(candidate.getImageUrl());
        candidateRepository.save(newCandidate);
        return newCandidate;
    }

}
