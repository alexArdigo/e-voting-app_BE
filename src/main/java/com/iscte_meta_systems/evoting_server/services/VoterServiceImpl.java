package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.repositories.*;
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
    private VoterHashRepository voterHashRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private ParishRepository parishRepository;


    @Override
    public void saveVoterHash(Voter voter) {
        if (voter == null)
            throw new NullPointerException("No voter sent over");

        String hashIdentification = getHashIdentification(voter.getNif());

        if (!voterHashRepository.existsByHashIdentification(hashIdentification)) {
            try {


                voterHashRepository.save(
                    new VoterHash(
                        hashIdentification,
                        voter.getDistrict(),
                        voter.getMunicipality(),
                        voter.getParish()
                    )
                );
            } catch (Exception e) {
                throw new RuntimeException("Error processing voter data: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public String getHashIdentification(Long nif) {
        return passwordEncoder.encode(nif.toString());
    }


    @Override
    public District getDistrict(String districtName) {
        List<District> districts = districtRepository.findByDistrictNameContainingIgnoreCase(districtName);
        return districts.stream()
                .filter(d -> normalizeString(d.getDistrictName()).equalsIgnoreCase(normalizeString(districtName)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("District not found: " + districtName));
    }

    @Override
    public Municipality getMunicipality(String municipalityName, District district) {
        List<Municipality> municipalities = municipalityRepository.findByMunicipalityNameContainingIgnoreCase(municipalityName);
        return municipalities.stream()
            .filter(m -> normalizeString(m.getMunicipalityName()).equalsIgnoreCase(normalizeString(municipalityName))
                   && m.getDistrict().equals(district))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Municipality not found in district: " + municipalityName +
                          ". Please check spelling and accents."));
    }

    @Override
    public Parish getParish(String parishName, Municipality municipality) {
        List<Parish> parishes = parishRepository.findByParishNameContainingIgnoreCase(parishName);
        return parishes.stream()
                .filter(p -> normalizeString(p.getParishName()).equalsIgnoreCase(normalizeString(parishName))
                        && p.getMunicipality().equals(municipality))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Parish not found in municipality: " + parishName +
                        ". Please check spelling and accents."));
    }

    @Override
    public Boolean hasAlreadyVoted(String hash, Long electionId) {
        if (hash == null || electionId == null)
            throw new NullPointerException();

        Optional<Election> optional = electionRepository.findById(electionId);
        List<VoterHash> votersVoted = optional.orElseThrow().getVotersVoted();

        return votersVoted.contains(hash);
    }

    @Override
    public Voter getLoggedVoter() {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println("getloggedvoter = " + id);

        Voter voter = voterRepository.findVoterById(Long.valueOf(id));
        if (voter == null)
            throw new RuntimeException("Voter not found with ID: " + id);

        return voter;
    }

    /**
     * Normalizes a string by removing diacritical marks (accents) and converting to lowercase
     * This helps with comparison when user input doesn't match exact characters in database
     * @param input The string to normalize
     * @return Normalized string without accents and in lowercase
     */
    private String normalizeString(String input) {
        if (input == null) {
            return "";
        }
        String result = input.toLowerCase();
        // Remove diacritical marks (accents)
        result = java.text.Normalizer.normalize(result, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return result;
    }
}
