package com.iscte_meta_systems.evoting_server.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.iscte_meta_systems.evoting_server.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Voter {

    @Id
    @GeneratedValue
    private Long id;
    private String telephoneNumber;
    private Long pin;
    private Long nif;
    private String firstName;
    private String lastName;
    private String district;
    private String municipality;
    private String parish;
    private Role role = Role.VOTER;

    public Voter() {
    }

    public Voter(JsonNode jsonNode) {
        if (jsonNode == null) {
            throw new NullPointerException("JsonNode cannot be null");
        }
        this.nif = jsonNode.path("nif").asLong();
        this.telephoneNumber = jsonNode.path("telephoneNumber").asText();
        this.firstName = jsonNode.path("firstName").asText();
        this.lastName = jsonNode.path("lastName").asText();
        this.district = jsonNode.path("district").asText();
        this.municipality = jsonNode.path("municipality").asText();
        this.parish = jsonNode.path("parish").asText();
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

    public Long getPin() {
        return pin;
    }

    public void setPin(Long pin) {
        this.pin = pin;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
