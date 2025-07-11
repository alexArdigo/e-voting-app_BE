package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.VoterHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoterHashRepository extends JpaRepository<VoterHash, Long> {

    boolean existsByHashIdentification(String hashIdentification);

    VoterHash getVoterHashByHashIdentification(String hash);

}
