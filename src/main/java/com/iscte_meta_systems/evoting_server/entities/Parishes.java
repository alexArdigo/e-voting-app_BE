package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "parishes")
public class Parishes {

    @Id
    @GeneratedValue
    private Long id;
    private String parishName;
    @ManyToOne
    @JoinColumn(name = "municipality_id")
    private Municipalities municipality;

    public Parishes() {
    }

    public Parishes(String parishName) {
        this.parishName = parishName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParishName() {
        return parishName;
    }

    public void setParishName(String parishName) {
        this.parishName = parishName;
    }

    public Municipalities getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipalities municipality) {
        this.municipality = municipality;
    }
}
