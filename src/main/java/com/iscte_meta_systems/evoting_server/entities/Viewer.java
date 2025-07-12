package com.iscte_meta_systems.evoting_server.entities;

import com.iscte_meta_systems.evoting_server.enums.Role;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Viewer extends User {

    private String institutionName;
    private LocalDateTime lastLogin;
    private String profilePicture;

    public Viewer() {
    }

    public Viewer(UserRegisterDTO userRegisterDTO) {
        super(userRegisterDTO);
        this.institutionName = userRegisterDTO.getInstitutionName();
        this.setRole(Role.VIEWER);
        this.profilePicture = "https://cdn-icons-png.flaticon.com/512/10109/10109817.png";
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


}
