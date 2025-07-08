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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean started = false;

    @OneToMany(mappedBy = "legislative", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElectoralCircle> electoralCircles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public boolean isStarted() { return started; }
    public void setStarted(boolean started) { this.started = started; }

    public List<ElectoralCircle> getElectoralCircles() { return electoralCircles; }
    public void setElectoralCircles(List<ElectoralCircle> electoralCircles) { this.electoralCircles = electoralCircles; }

    public void startElection() {
        this.started = true;
        electoralCircles.forEach(ElectoralCircle::startElection);
    }

    public void endElection() {
        this.started = false;
        electoralCircles.forEach(ElectoralCircle::endElection);
    }

    public void addElectoralCircle(ElectoralCircle circle) {
        electoralCircles.add(circle);
        circle.setLegislative(this);
    }
}