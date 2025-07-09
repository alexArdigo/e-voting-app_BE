package com.iscte_meta_systems.evoting_server.model;

import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Viewer;
import com.iscte_meta_systems.evoting_server.enums.Role;

import java.time.LocalDateTime;

public class UserRegisterDTO {
    private String username;
    private String password;
    private String name;
    private String institutionName;
    private LocalDateTime lastLogin;
    private Role role;


    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String username, String password, String name, String institutionName) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.institutionName = institutionName;
    }

    public UserRegisterDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.role = user.getRole();

        if (user instanceof Viewer viewer) {
            this.institutionName = viewer.getInstitutionName();
            this.lastLogin = viewer.getLastLogin();

        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
