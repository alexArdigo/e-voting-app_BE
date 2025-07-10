package com.iscte_meta_systems.evoting_server.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

@Service
public class PartiesAndCandidatesServiceImpl implements PartiesAndCandidatesService {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ElectoralCircleRepository electoralCircleRepository;

    @Override
    public void populatePartiesAndCandidatesFromJSON(ElectoralCircle electoralCircle) {
        if (electoralCircle.getDistricts() == null) {
            throw new IllegalArgumentException("ElectoralCircle must have a district assigned");
        }

        String districtName = electoralCircle.getDistricts().getDistrictName();

        try {
            Map<String, PartyData> partiesData = readPartiesFromJSON(districtName);

            List<Organisation> organisations = new ArrayList<>();

            for (PartyData partyData : partiesData.values()) {
                Party party = new Party();
                party.setOrganisationName(partyData.name);
                party.setName(partyData.name);
                party.setColor(partyData.color);
                party.setLogoUrl(partyData.logoUrl);
                party.setDescription(partyData.description);
                party.setElectoralCircle(electoralCircle);

                List<Candidate> candidates = new ArrayList<>();
                for (CandidateData candidateData : partyData.candidates) {
                    Candidate existingCandidate = candidateRepository.findByName(candidateData.name);
                    Candidate candidate;

                    if (existingCandidate != null) {
                        candidate = existingCandidate;
                    } else {
                        candidate = new Candidate();
                        candidate.setName(candidateData.name);
                        candidate.setImageUrl(candidateData.imageUrl);
                        candidateRepository.save(candidate);
                    }
                    candidates.add(candidate);
                }

                party.setCandidates(candidates);
                partyRepository.save(party);
                organisations.add(party);
            }

            if (electoralCircle.getOrganisations() == null) {
                electoralCircle.setOrganisations(new ArrayList<>());
            }
            electoralCircle.getOrganisations().addAll(organisations);
            electoralCircleRepository.save(electoralCircle);

            System.out.println("Successfully populated " + organisations.size() +
                    " parties for district: " + districtName);

        } catch (Exception e) {
            System.err.println("Error populating parties and candidates for district " +
                    districtName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Map<String, PartyData> readPartiesFromJSON(String districtName) throws Exception {
        ClassPathResource resource = new ClassPathResource("PartiesAndCandidates.json");
        InputStream inputStream = resource.getInputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputStream);

        Map<String, PartyData> partiesData = new HashMap<>();

        JsonNode districtNode = rootNode.get(districtName);
        if (districtNode == null) {
            System.out.println("No data found for district: " + districtName);
            return partiesData;
        }

        for (JsonNode partyNode : districtNode) {
            String partyName = partyNode.get("partyName").asText();
            String partyColor = partyNode.get("color").asText();
            String partyLogoUrl = partyNode.get("logoUrl").asText();
            String partyDescription = partyNode.get("description").asText();

            PartyData partyData = new PartyData();
            partyData.name = partyName;
            partyData.color = partyColor;
            partyData.logoUrl = partyLogoUrl;
            partyData.description = partyDescription;
            partyData.candidates = new ArrayList<>();

            JsonNode candidatesNode = partyNode.get("candidates");
            if (candidatesNode != null && candidatesNode.isArray()) {
                for (JsonNode candidateNode : candidatesNode) {
                    CandidateData candidateData = new CandidateData();
                    candidateData.name = candidateNode.get("name").asText();
                    candidateData.imageUrl = candidateNode.get("imageUrl").asText();
                    partyData.candidates.add(candidateData);
                }
            }

            partiesData.put(partyName, partyData);
        }

        inputStream.close();
        return partiesData;
    }

    private static class PartyData {
        String name;
        String color;
        String logoUrl;
        String description;
        List<CandidateData> candidates;
    }

    private static class CandidateData {
        String name;
        String imageUrl;
    }
}