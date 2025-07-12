package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private HelpCommentRepository helpCommentRepository;
    @Autowired
    private VotingSessionRepository votingSessionRepository;


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
    public Map<String, String> startVoting(Long electionId, Long voterId) {
        checkElectionAndVoter(electionId, voterId);

        VotingSession existingSession = votingSessionRepository.findByElectionIdAndVoterId(electionId, voterId);
        if (existingSession != null) {
            return Map.of("timeLeft", existingSession.getRemainingTime().toString());
        }
        VotingSession votingSession = votingSessionRepository.save(new VotingSession(electionId, voterId));

        return Map.of("timeLeft", votingSession.getRemainingTime().toString());
    }

    @Override
    public void stopVoting(Long electionId, Long voterId) {
        checkElectionAndVoter(electionId, voterId);

        VotingSession votingSession = votingSessionRepository.findByElectionIdAndVoterId(electionId, voterId);
        if (votingSession == null) {
            throw new RuntimeException("Voting session not found for election ID: " + electionId + " and voter ID: " + voterId);
        }

        votingSessionRepository.delete(votingSession);
    }

    @Override
    public boolean isVoting(Long id) {
        return votingSessionRepository.existsById(id);
    }

    private void checkElectionAndVoter(Long electionId, Long voterId) {
        Election election = electionRepository.findElectionById(electionId);
        if (election == null || election.getId() == null) {
            throw new RuntimeException("Election not found with ID: " + electionId);
        }
        Voter voter = voterRepository.findVoterById(voterId);
        if (voter == null || voter.getId() == null) {
            throw new RuntimeException("Voter not found with ID: " + voterId);
        }
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
    public List<Long> hasAlreadyVotedList(Long voterId) {
        if (voterId == null)
            throw new NullPointerException();


        return votingSessionRepository.findAll().stream()
                .filter(votingSession -> votingSession.getVoterId().equals(voterId))
                .map(VotingSession::getElectionId).toList();
    }

    @Override
    public Map<String, Boolean> hasAlreadyThisElection(Long electionId, Long voterId) {
        VotingSession votingSession = votingSessionRepository.findByElectionIdAndVoterId(electionId, voterId);
        if (votingSession != null) {
            return Map.of("hasVoted", true);
        }
        return null;
    }


    @Override
    public Voter getLoggedVoter() {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (id == null || id.isEmpty() || id.equals("anonymousUser")) {
            return null;
        }
        if (!voterRepository.existsById(Long.parseLong(id))) {
            return null;
        }
        return voterRepository.findVoterById(Long.valueOf(id));
    }

    @Override
    public void removeLikeFromComment(VoterHash voterHash, HelpComment comment) {
        if (voterHash == null)
            throw new NullPointerException("Voter hash cannot be null or empty");

        if (comment == null)
            throw new NullPointerException("Comment cannot be null");

        if (!comment.hasLiked(voterHash)) {
            throw new RuntimeException("Voter has not liked this comment");
        }

        comment.removeLike(voterHash);
        helpCommentRepository.save(comment);
    }



    /**
     * Normalizes a string by removing diacritical marks (accents) and converting to lowercase
     * This helps with comparison when user input doesn't match exact characters in database
     *
     * @param input The string to normalize
     * @return Normalized string without accents and in lowercase
     */
    private String normalizeString(String input) {
        if (input == null) {
            return "";
        }
        String result = input.toLowerCase();
        // Remove diacritical marks (accents)
        result = Normalizer.normalize(result, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return result;
    }
}
