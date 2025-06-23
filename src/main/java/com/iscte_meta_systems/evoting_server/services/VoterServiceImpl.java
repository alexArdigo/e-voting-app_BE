package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Voter;
import com.iscte_meta_systems.evoting_server.repositories.ElectionRepository;
import com.iscte_meta_systems.evoting_server.repositories.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class VoterServiceImpl implements VoterService {

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private VoterRepository voterRepository;

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

    @Override
    public Voter getLoggedVoter() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (voterRepository.findByHashIdentification(username) == null)
            return null;
        return voterRepository.findByHashIdentification(username);
    }


}
