package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Organisation;
import com.iscte_meta_systems.evoting_server.entities.Vote;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;

import java.util.List;

public interface ElectionService {
    List<Organisation> getBallotByElectionId(Long id);

    List<Election> getElections(String electionType, Integer electionYear);

    Election getElectionById(Long id);

    ElectionDTO createElection(ElectionDTO electionDTO);

    Election startElection(Long id);

    Election endElection(Long id);

//    Vote castVote(Long id, Vote vote);
}
