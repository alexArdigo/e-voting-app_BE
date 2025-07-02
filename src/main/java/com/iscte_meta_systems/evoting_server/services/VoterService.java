package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

public interface VoterService {

    District getDistrict(VoterDTO voterDTO);

    Municipality getMunicipality(VoterDTO voterDTO, District district);

    Parish getParish(VoterDTO voterDTO, Municipality municipality);

    Boolean hasAlreadyVoted(String voter, Long electionId);

    Voter getLoggedVoter();

    void saveVoterAuthenticated(VoterDTO voterDTO);

    VoterDTO getInfo();

}
