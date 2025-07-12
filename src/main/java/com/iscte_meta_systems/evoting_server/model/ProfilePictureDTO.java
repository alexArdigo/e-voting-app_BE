package com.iscte_meta_systems.evoting_server.model;

public class ProfilePictureDTO {

    private String profilePicture;

    public ProfilePictureDTO() {
    }

    public ProfilePictureDTO(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
