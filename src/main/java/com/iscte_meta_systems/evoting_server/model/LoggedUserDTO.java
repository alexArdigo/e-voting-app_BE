package com.iscte_meta_systems.evoting_server.model;

import java.time.LocalDateTime;

public class LoggedUserDTO {
    private String username;
    private String name;
    private String institutionName;
    private LocalDateTime lastLogin;

    public LoggedUserDTO() {
    }

    public LoggedUserDTO(String username, String name, String institutionName, LocalDateTime lastLogin) {
        this.username = username;
        this.name = name;
        this.institutionName = institutionName;
        this.lastLogin = lastLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }


}
