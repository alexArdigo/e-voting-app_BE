package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Voter;
import com.iscte_meta_systems.evoting_server.entities.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
    VotingSession findByElectionAndVoter(Election election, Voter voter);

    VotingSession findByElectionIdAndVoterId(Long electionId, Long voterId);
}
