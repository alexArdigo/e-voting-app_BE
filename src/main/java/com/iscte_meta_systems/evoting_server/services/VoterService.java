package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;

public interface VoterService {

    District getDistrict(VoterDTO voterDTO);

    Municipality getMunicipality(VoterDTO voterDTO, District district);

    Parish getParish(VoterDTO voterDTO, Municipality municipality);

    Boolean hasAlreadyVoted(String voter, Long electionId);

    VoterHash getLoggedVoter();

    void saveVoterHash(VoterDTO voterDTO);

    VoterDTO getInfo();

    void saveVoter(VoterDTO voterDTO);
}
