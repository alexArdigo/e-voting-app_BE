package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.UniParty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniPartyRepository extends JpaRepository<UniParty, Long> {
}
