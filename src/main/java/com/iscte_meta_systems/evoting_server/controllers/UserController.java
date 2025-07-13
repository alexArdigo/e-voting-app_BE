package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Viewer;
import com.iscte_meta_systems.evoting_server.model.LoggedUserDTO;
import com.iscte_meta_systems.evoting_server.model.ProfilePictureDTO;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import com.iscte_meta_systems.evoting_server.repositories.ViewerRepository;
import com.iscte_meta_systems.evoting_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/registerAdmin")
    public ResponseEntity<String> registerAdmin(UserRegisterDTO userRegisterDTO) {
        try {
            String result = userService.registerAdmin(userRegisterDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering admin: " + e.getMessage());
        }
    }

    @PostMapping("/registerViewer")
    public ResponseEntity<String> registerViewer(UserRegisterDTO userRegisterDTO) {
        try {
            String result = userService.registerViewer(userRegisterDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering viewer: " + e.getMessage());
        }
    }

    @GetMapping("/pendingAuthorization")
    public List<Viewer> pendingAuthorization() {
        return userService.pendingAuthorization();
    }

    @PostMapping("/approveViewer")
    public String approveViewer(@RequestParam Long id) {
        if (userService.approveViewer(id)) {
            return "Viewer approved successfully";
        } else {
            return "Viewer approval failed";
        }
    }

    @PostMapping("/deleteApprovedViewer")
    public ResponseEntity<String> deleteApprovedViewer(@RequestParam Long id) {
        try {
            if (userService.deleteApprovedViewer(id))
                return ResponseEntity.ok("Viewer deleted successfully");
            else
                return ResponseEntity.badRequest().body("Viewer deletion failed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting viewer: " + e.getMessage());
        }
    }

    @GetMapping("/findUserByUsername")
    public User findUserByUsername(@RequestParam("username") String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/loggedUser")
    public LoggedUserDTO getLoggedUser() {
        User user = userService.getLoggedUser();
        return (user != null) ? new LoggedUserDTO(user) : null;
    }

    @GetMapping("/approvedViewers")
    public List<Viewer> approvedViewers() {
        return userService.approvedViewers();
    }


    @PutMapping("/{id}/updateProfilePicture")
    public ResponseEntity<String> updateProfilePicture(@PathVariable Long id, @RequestBody ProfilePictureDTO dto) {
        boolean success = userService.updateProfilePicture(id, dto.getProfilePicture());

        if (success) {
            return ResponseEntity.ok("Profile picture updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update profile picture");
        }
    }

    @GetMapping("/{id}/profilePicture")
    public ResponseEntity<String> getProfilePicture(@PathVariable Long id) {
        String picture = userService.getProfilePicture(id);
        if (picture != null) {
            return ResponseEntity.ok(picture);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profile-image")
    public ResponseEntity<byte[]> getMyProfileImage() {

        try {
            User user = userService.getLoggedUser();
            if (user == null) {
                return ResponseEntity.status(401).build();
            }

            String imagePath = userService.getProfileImagePath(user.getId());
            if (imagePath == null) {
                return ResponseEntity.notFound().build();
            }

            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

            String contentType = "image/jpeg";
            String fileName = imageFile.getName().toLowerCase();
            if (fileName.endsWith(".png")) {
                contentType = "image/png";
            } else if (fileName.endsWith(".jpg")) {
                contentType = "image/jpeg";
            }

            return ResponseEntity.ok()
                    .header("content-type", contentType)
                    .header("Cache-Control", "no-cache")
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {

        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            User user = userService.getLoggedUser();
            if (user == null) {
                return ResponseEntity.status(401).body("User not logged in");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("File type not supported");
            }

            long maxSize = 5 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest().body("File is too large");
            }

            String fileName = userService.uploadProfileImage(file);
            return ResponseEntity.ok("image saved successfully" + fileName);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading profile picture");
        }
    }


}
