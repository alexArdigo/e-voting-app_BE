package com.iscte_meta_systems.evoting_server.model;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;
import com.iscte_meta_systems.evoting_server.enums.OrganisationType;

public class OrganisationDTO {
    private Long id;
    private OrganisationType organisationType;
    private String name;
    private Long ElectionId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganisationType getOrganisationType() {
        return organisationType;
    }

    public void setOrganisationType(OrganisationType organisationType) {
        this.organisationType = organisationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getElectionId() {
        return ElectionId;
    }
    
    public void setElectionId(Long electionId) {
        this.ElectionId = electionId;
    }

}
