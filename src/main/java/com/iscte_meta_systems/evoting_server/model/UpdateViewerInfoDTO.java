package com.iscte_meta_systems.evoting_server.model;

public class UpdateViewerInfoDTO {
    private Long id;
    private String name;
    private String institution;
    private String username;

    public UpdateViewerInfoDTO() {
    }

    public UpdateViewerInfoDTO(Long id, String name, String institution) {
        this.id = id;
        this.name = name;
        this.institution = institution;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

