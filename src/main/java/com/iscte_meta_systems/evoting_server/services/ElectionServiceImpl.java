package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.repositories.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectionServiceImpl implements  ElectionService {

    @Autowired
    private ElectionRepository electionRepository;

    @Override
    public List<Election> getElections() {
        return electionRepository.findAll();
    }
}
