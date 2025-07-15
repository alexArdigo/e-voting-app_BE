package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;

import java.util.List;
import java.util.Map;

public interface VoterService {

    District getDistrict(String districtName);

    Municipality getMunicipality(String municipalityName, District district);

    Parish getParish(String parishName, Municipality municipality);

    List<Long> hasAlreadyVotedList(Long nif);

    Voter getLoggedVoter();

    void saveVoterHash(Voter voter);

    String getHashIdentification(Long nif);

    Map<String, String> startVoting(Long electionId, Long userId);

    Map<String, Object> votingStatus(Long id);

    void stopVoting(Long electionId, Long userId);

    void removeLikeFromComment(VoterHash voterHash, HelpComment comment);

    Object hasAlreadyThisElection(Long electionId, Long voterId);
}
