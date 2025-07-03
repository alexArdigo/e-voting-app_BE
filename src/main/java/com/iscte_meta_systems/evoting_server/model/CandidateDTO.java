package com.iscte_meta_systems.evoting_server.model;

import com.iscte_meta_systems.evoting_server.entities.Candidate;

public class CandidateDTO {

    private String name;
    private String imageUrl;

    public CandidateDTO() {}

    public CandidateDTO(Candidate candidate) {
        this.name = candidate.getName();
        this.imageUrl = candidate.getImageUrl();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
