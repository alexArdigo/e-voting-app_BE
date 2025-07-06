package com.iscte_meta_systems.evoting_server.services.InitializeDBInjection;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Party;

public interface InitializeDBInjection {

    void initializeElections();

    void initializeParties();

    Party createParty(
            String shortName,
            String fullName,
            String color,
            String logoUrl,
            Election election
    );

    void initializeElectoralCircles();


    //void initializeVotes();
}
