package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    public Admin() {
    }

    public Admin(String username, String password, String name) {
        super(username, password, name);
    }
}

