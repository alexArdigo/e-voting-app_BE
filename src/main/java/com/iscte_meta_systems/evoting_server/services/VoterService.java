package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

public interface VoterService {

    Boolean hasAlreadyVoted(String voter, Long electionId);
}
