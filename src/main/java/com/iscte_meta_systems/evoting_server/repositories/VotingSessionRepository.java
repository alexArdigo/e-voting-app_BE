package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {

    VotingSession findByElectionIdAndVoterId(Long electionId, Long voterId);

    VotingSession findVotingSessionByVoterId(Long id);

    List<VotingSession> findAllByVoterId(Long id);
}
