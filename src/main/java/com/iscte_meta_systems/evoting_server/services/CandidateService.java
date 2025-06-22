package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Candidate;

import java.util.List;

public interface CandidateService {
    List<Candidate> getCandidatesByElection(Long electionId);

    Candidate getCandidatesById(long id);

    Candidate addCandidate(Candidate candidate);
}
