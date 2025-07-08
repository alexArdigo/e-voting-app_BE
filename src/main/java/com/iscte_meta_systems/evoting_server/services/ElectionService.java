package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Legislative;
import com.iscte_meta_systems.evoting_server.entities.Organisation;
import com.iscte_meta_systems.evoting_server.entities.Vote;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.VoteRequestModel;

import java.util.List;

public interface ElectionService {
    List<Organisation> getBallotByElectionId(Long id);

    List<ElectionDTO> getElections(String electionType, Integer electionYear, Boolean isActive);

    Election getElectionById(Long id);

    ElectionDTO createElection(ElectionDTO electionDTO);

    Election startElection(Long id);

    Election endElection(Long id);

    Vote castVote(Long id, VoteRequestModel vote);

    Boolean isStarted(Long id);

    List<Vote> generateTestVotes(int numberOfVotes, Long electionId);

    Legislative getLegislativeById(Long legislativeID);
    List<Legislative> getLegislatives();

    ElectionDTO updateElection(Long id, ElectionDTO electionDTO);

    void deleteElection(Long id);
}
