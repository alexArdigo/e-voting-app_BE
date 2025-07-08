package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;

import java.util.ArrayList;

public interface VoterService {

    District getDistrict(String districtName);

    Municipality getMunicipality(String municipalityName, District district);

    Parish getParish(String parishName, Municipality municipality);

    ArrayList<Long> hasAlreadyVoted(String nif);

    Voter getLoggedVoter();

    void saveVoterHash(Voter voter);

    String getHashIdentification(Long nif);

    void removeLikeFromComment(String voterHash, HelpComment comment);
}
