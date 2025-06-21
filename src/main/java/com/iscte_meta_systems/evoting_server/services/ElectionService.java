package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;

import java.util.List;

public interface ElectionService {
    //List<Candidate> getBallotByElectionId(Long id);

    List<Election> getElections(String electionType, Integer electionYear);

    Election getElectionById(Long id);

    Election createElection(Election election);

    Election startElection(Long id);

    Election endElection(Long id);

    //Vote castVote(Long id, Vote vote);
}
