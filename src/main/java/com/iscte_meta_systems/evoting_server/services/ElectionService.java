package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ElectionService {
    List<Election> getElections(String electionType, Integer electionYear);

    Election getElectionById(Long id);
}
