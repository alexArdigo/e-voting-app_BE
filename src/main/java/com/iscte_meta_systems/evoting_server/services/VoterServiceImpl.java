package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import com.iscte_meta_systems.evoting_server.repositories.DistrictRepository;
import com.iscte_meta_systems.evoting_server.repositories.ElectionRepository;
import com.iscte_meta_systems.evoting_server.repositories.MunicipalityRepository;
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

    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private MunicipalityRepository municipalityRepository;
    @Autowired
    private ParishRepository parishRepository;

    @Override
    public void saveVoterAuthenticated(VoterDTO voterDTO) {
        if (voterDTO == null)
            throw new NullPointerException("No voter sent over");

        String hashIdentification = passwordEncoder.encode(voterDTO.getNif().toString());
        if (!voterRepository.existsByHashIdentification(hashIdentification)) {
            try {
                // Find distinct district
                List<District> districts = districtRepository.findByDistrictNameContainingIgnoreCase(voterDTO.getDistrict());
                District district = districts.stream()
                    .filter(d -> normalizeString(d.getDistrictName()).equalsIgnoreCase(normalizeString(voterDTO.getDistrict())))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("District not found: " + voterDTO.getDistrict()));

                // Find municipality belonging to the found district
                List<Municipality> municipalities = municipalityRepository.findByMunicipalityNameContainingIgnoreCase(voterDTO.getMunicipality());
                Municipality municipality = municipalities.stream()
                    .filter(m -> normalizeString(m.getMunicipalityName()).equalsIgnoreCase(normalizeString(voterDTO.getMunicipality()))
                           && m.getDistrict().equals(district))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Municipality not found in district: " + voterDTO.getMunicipality() +
                                  ". Please check spelling and accents."));

                // Find parish belonging to the found municipality
                List<Parish> parishes = parishRepository.findByParishNameContainingIgnoreCase(voterDTO.getParish());
                Parish parish = parishes.stream()
                    .filter(p -> normalizeString(p.getParishName()).equalsIgnoreCase(normalizeString(voterDTO.getParish()))
                           && p.getMunicipality().equals(municipality))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Parish not found in municipality: " + voterDTO.getParish() +
                                  ". Please check spelling and accents."));

                voterRepository.save(
                    new Voter(
                        hashIdentification,
                        district,
                        municipality,
                        parish
                    )
                );
            } catch (Exception e) {
                throw new RuntimeException("Error processing voter data: " + e.getMessage(), e);
            }
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
