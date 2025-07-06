package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
    Party findPartyById(Long id);
}
