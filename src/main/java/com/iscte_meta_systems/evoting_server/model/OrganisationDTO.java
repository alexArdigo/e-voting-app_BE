package com.iscte_meta_systems.evoting_server.model;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;

public class OrganisationDTO {
    private Long id;
    private String organisationType;
    private String name;
    private ElectoralCircle electoralCircle;
    private Long ElectionId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganisationType() {
        return organisationType;
    }

    public void setOrganisationType(String organisationType) {
        this.organisationType = organisationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElectoralCircle getElectoralCircle() {
        return electoralCircle;
    }

    public void setElectoralCircle(ElectoralCircle electoralCircle) {
        this.electoralCircle = electoralCircle;
    }

    public Long getElectionId() {
        return ElectionId;
    }
    
    public void setElectionId(Long electionId) {
        this.ElectionId = electionId;
    }

}
