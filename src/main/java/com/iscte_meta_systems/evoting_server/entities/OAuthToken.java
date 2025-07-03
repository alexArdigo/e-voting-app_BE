package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class OAuthToken {

    @Id
    @GeneratedValue
    private Long id;
    private String token;

    public OAuthToken() {
    }

    public OAuthToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
