package com.iscte_meta_systems.evoting_server.model;

import com.fasterxml.jackson.databind.JsonNode;

public class VoterDTO {

    Long nif;
    String telephoneNumber;
    String firstName;
    String lastName;
    String district;
    String municipality;
    String parish;

    public VoterDTO(JsonNode jsonNode) {
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


    public Long getNif() {
        return nif;
    }

    public void setNif(Long nif) {
        this.nif = nif;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
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
}
