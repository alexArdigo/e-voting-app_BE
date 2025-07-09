package com.iscte_meta_systems.evoting_server.model;

import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Viewer;
import com.iscte_meta_systems.evoting_server.enums.Role;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggedUserDTO {

    private Long id;
    private String username;
    private String name;
    private String institutionName;
    private String lastLogin;
    private Role role;


    public LoggedUserDTO(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.role = user.getRole();
        this.id = user.getId();

        if (user instanceof Viewer viewer) {
            this.institutionName = viewer.getInstitutionName();
            if (viewer.getLastLogin() != null) {
                this.lastLogin = viewer.getLastLogin()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else {
                this.lastLogin = null;
            }

        }
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


