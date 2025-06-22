package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.Entity;

@Entity
public class Viewer extends User {

    private String institutionName;

    public Viewer() {
    }

    public Viewer(String username, String password, String name, String institutionName) {
        super(username, password, name);
        this.institutionName = institutionName;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}
