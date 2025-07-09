package com.iscte_meta_systems.evoting_server.services.InitializeDBInjection;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Party;

public interface InitializeDBInjection {

    void initializeElections();

    public void initializeVotes();

    //void initializeVotes();
}
