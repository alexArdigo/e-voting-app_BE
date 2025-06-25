package com.iscte_meta_systems.evoting_server.entities;

import com.iscte_meta_systems.evoting_server.enums.Role;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    public Admin() {
    }

    public Admin(UserRegisterDTO userRegisterDTO) {
        super(userRegisterDTO);
        this.setRole(Role.ADMIN);
        this.setIsAuthorized(true);
    }
}

