package com.iscte_meta_systems.evoting_server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "parishes")
public class Parish {

    @Id
    @GeneratedValue
    private Long id;
    private String parishName;
    @ManyToOne
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    public Parish() {
    }

    public Parish(String parishName) {
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

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }
}
