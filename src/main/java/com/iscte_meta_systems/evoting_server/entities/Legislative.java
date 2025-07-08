package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Legislative {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    private boolean started = false;

    @OneToMany(mappedBy = "legislative", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElectoralCircle> electoralCircles;

    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
