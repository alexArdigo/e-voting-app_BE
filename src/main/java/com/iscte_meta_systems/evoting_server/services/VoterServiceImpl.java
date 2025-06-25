package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Voter;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import com.iscte_meta_systems.evoting_server.repositories.ElectionRepository;
import com.iscte_meta_systems.evoting_server.repositories.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoterServiceImpl implements VoterService {

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void saveVoterAuthenticated(VoterDTO voterDTO) {
        if (voterDTO == null)
            throw new NullPointerException("No voter sent over");

        String hashIdentification = passwordEncoder.encode(voterDTO.getNif().toString());
        if (!voterRepository.existsByHashIdentification(hashIdentification)) {
            voterRepository.save(
                    new Voter(
                            hashIdentification,
                            voterDTO.getDistrict(),
                            voterDTO.getMunicipality(),
                            voterDTO.getParish()
                    ));
        }

    }

    @Override
    public Boolean hasAlreadyVoted(String voter, Long electionId) {
        if (voter == null || electionId == null)
            throw new NullPointerException();

        Optional<Election> optional = electionRepository.findById(electionId);
        List<String> votersVoted = optional.orElseThrow().getVotersVoted();

        return votersVoted.stream().allMatch(hash -> hash.equals(voter));
    }

    @Override
    public Voter getLoggedVoter() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (voterRepository.findByHashIdentification(username) == null)
            return null;
        return voterRepository.findByHashIdentification(username);
    }
}
