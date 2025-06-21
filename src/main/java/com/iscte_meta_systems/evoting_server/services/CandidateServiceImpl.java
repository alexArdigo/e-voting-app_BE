package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Candidate;
import com.iscte_meta_systems.evoting_server.repositories.CandidateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {

    CandidateRepository candidateRepository;

    @Override
    public List<Candidate> getCandidatesByType(String candidateType, String electionType) {
        return candidateRepository.findAllByType(candidateType, electionType);
    }

    @Override
    public Candidate getCandidatesById(long id) {
        return candidateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Candidate with id " + id + " not found"));
    }

    @Override
    public Candidate addCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }
}
