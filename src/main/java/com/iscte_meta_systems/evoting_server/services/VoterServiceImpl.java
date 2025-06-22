package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.repositories.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoterServiceImpl implements VoterService {

    @Autowired
    private ElectionRepository electionRepository;

    @Override
    public Boolean hasAlreadyVoted(String voter, Long electionId) {
        if (voter == null || electionId == null)
            throw new NullPointerException();
        /*
        Election election = electionRepository.findElectionById(electionId);

        return election.getVotedList().stream().allMatch(hash -> hash.equals(voter));
        */
        return null;
    }
}
