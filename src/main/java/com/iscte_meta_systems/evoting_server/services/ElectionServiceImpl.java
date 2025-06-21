package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.repositories.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectionServiceImpl implements  ElectionService {

    @Autowired
    private ElectionRepository electionRepository;

    @Override
    public List<Election> getElections(String electionType, Integer electionYear) {
        List<Election> elections = electionRepository.findAll();
        return elections.stream()
                .filter(e -> electionType == null || e.getClass().getSimpleName().equalsIgnoreCase(electionType))
                .filter(e -> electionYear == null || (e.getStartDate() != null && e.getStartDate().getYear() == electionYear))
                .collect(Collectors.toList());
    }

    @Override
    public Election getElectionById(Long id) {
        return electionRepository.getReferenceById(id);
    }
}
