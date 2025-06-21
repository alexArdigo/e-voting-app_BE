package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectionServiceImpl implements  ElectionService {

    @Override
    public List<Election> getElections() {
        return electionRepository.findAll();
    }
}
