package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Legislative {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "legislative", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElectoralCircle> electoralCircles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ElectoralCircle> getElectoralCircles() {
        return electoralCircles;
    }

    public void setElectoralCircles(List<ElectoralCircle> electoralCircles) {
        this.electoralCircles = electoralCircles;
    }
}
