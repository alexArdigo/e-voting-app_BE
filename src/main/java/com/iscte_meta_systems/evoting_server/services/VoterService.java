package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;

public interface VoterService {

    District getDistrict(String districtName);

    Municipality getMunicipality(String municipalityName, District district);

    Parish getParish(String parishName, Municipality municipality);

    Boolean hasAlreadyVoted(String voter, Long electionId);

    Voter getLoggedVoter();

    void saveVoterHash(Voter voter);

    String getHashIdentification(Long nif);

    boolean startVoting(Long electionId, Long userId);

    void stopVoting(Long electionId, Long userId);

    boolean isVoting(Long id);

    void removeLikeFromComment(String voterHash, HelpComment comment);
}
