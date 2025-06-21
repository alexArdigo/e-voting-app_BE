package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;

import java.util.List;

public interface ElectionService {
    List<Election> getElections();
}
