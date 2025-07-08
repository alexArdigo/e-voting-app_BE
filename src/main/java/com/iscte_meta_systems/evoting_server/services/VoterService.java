package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;

import java.util.ArrayList;
import java.util.List;

public interface VoterService {

    District getDistrict(String districtName);

    Municipality getMunicipality(String municipalityName, District district);

    Parish getParish(String parishName, Municipality municipality);

    ArrayList<Long> hasAlreadyVoted(Long nif);

    Voter getLoggedVoter();

    void saveVoterHash(Voter voter);

    String getHashIdentification(Long nif);

    void removeLikeFromComment(String voterHash, HelpComment comment);

    boolean startVoting(Long electionId, Long userId);

    void stopVoting(Long electionId, Long userId);

    boolean isVoting(Long id);
}
