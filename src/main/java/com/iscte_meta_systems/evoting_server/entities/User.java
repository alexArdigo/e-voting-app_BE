package com.iscte_meta_systems.evoting_server.entities;
import com.iscte_meta_systems.evoting_server.enums.Role;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private Boolean isAuthorized;
    @Enumerated(EnumType.STRING)
    private Role role;  //"ADMIN", "VIEWER" em caps

    public User() {
    }

    public User(UserRegisterDTO userRegisterDTO) {
        this.username = userRegisterDTO.getUsername();
        this.password = userRegisterDTO.getPassword();
        this.name = userRegisterDTO.getName();
        this.role = userRegisterDTO.getRole();
        this.isAuthorized = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getIsAuthorized() {
        return isAuthorized;
    }

    public void setIsAuthorized(Boolean authorized) {
        isAuthorized = authorized;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

