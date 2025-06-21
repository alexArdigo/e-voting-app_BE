package com.iscte_meta_systems.evoting_server.services;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public interface VoterService {

    Boolean hasAlreadyVoted(Long voter);
}
