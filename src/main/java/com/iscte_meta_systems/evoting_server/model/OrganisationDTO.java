package com.iscte_meta_systems.evoting_server.model;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;
import com.iscte_meta_systems.evoting_server.enums.OrganisationType;
import org.antlr.v4.runtime.misc.NotNull;

public class OrganisationDTO {
    private Long id;
    @NotNull
    private OrganisationType type;
    private String name;
    private Long electionId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganisationType getType() {
        return type;
    }

    public void setType(OrganisationType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getElectionId() {
        return electionId;
    }
    
    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

}
