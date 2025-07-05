package com.iscte_meta_systems.evoting_server.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.iscte_meta_systems.evoting_server.enums.Role;
import com.iscte_meta_systems.evoting_server.services.VoterService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Voter {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique=true)
    private String telephoneNumber;
    @Column(unique=true)
    private Long nif;
    private String firstName;
    private String lastName;
    @ManyToOne
    private District district;
    @ManyToOne
    private Municipality municipality;
    @ManyToOne
    private Parish parish;
    private Role role = Role.VOTER;

    public Voter() {
    }

    public Voter(JsonNode jsonNode, District district, Municipality municipality, Parish parish) {
        if (jsonNode == null) {
            throw new NullPointerException("JsonNode cannot be null");
        }

        this.nif = jsonNode.path("nif").asLong();
        this.telephoneNumber = jsonNode.path("telephoneNumber").asText();
        this.firstName = jsonNode.path("firstName").asText();
        this.lastName = jsonNode.path("lastName").asText();
        this.district = district;
        this.municipality = municipality;
        this.parish = parish;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public Long getNif() {
        return nif;
    }

    public void setNif(Long nif) {
        this.nif = nif;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public Parish getParish() {
        return parish;
    }

    public void setParish(Parish parish) {
        this.parish = parish;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
