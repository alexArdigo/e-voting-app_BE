package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.OAuthToken;
import com.iscte_meta_systems.evoting_server.entities.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {

    Voter findVoterByNif(Long nif);

    Voter findVoterById(Long voterId);

    Voter findByoAuthToken(OAuthToken token);
}
