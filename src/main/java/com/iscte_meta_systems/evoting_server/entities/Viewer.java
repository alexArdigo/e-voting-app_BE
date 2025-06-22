package com.iscte_meta_systems.evoting_server.entities;

import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import jakarta.persistence.Entity;

@Entity
public class Viewer extends User {

    private String institutionName;

    public Viewer() {
    }

    public Viewer(UserRegisterDTO userRegisterDTO) {
        super(userRegisterDTO);
        this.institutionName = userRegisterDTO.getInstitutionName();
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}
