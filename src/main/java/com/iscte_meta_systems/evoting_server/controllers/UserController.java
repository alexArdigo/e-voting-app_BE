package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Viewer;
import com.iscte_meta_systems.evoting_server.model.LoggedUserDTO;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import com.iscte_meta_systems.evoting_server.repositories.ViewerRepository;
import com.iscte_meta_systems.evoting_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping ("/registerAdmin")
    public ResponseEntity<String> registerAdmin(UserRegisterDTO userRegisterDTO) {
        try {
            String result = userService.registerAdmin(userRegisterDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering admin: " + e.getMessage());
        }
    }

    @PostMapping ("/registerViewer")
    public ResponseEntity<String> registerViewer(UserRegisterDTO userRegisterDTO) {
        try {
        String result = userService.registerViewer(userRegisterDTO);
        return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering viewer: " + e.getMessage());
        }
    }

    @GetMapping ("/pendingAuthorization")
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
            userService.deleteApprovedViewer(id);
            return ResponseEntity.ok("Viewer deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting viewer: " + e.getMessage());
        }
    }

    @GetMapping("/findUserByUsername")
    public User findUserByUsername(@RequestParam("username") String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping ("/loggedUser")
    public LoggedUserDTO getLoggedUser() {
        User user = userService.getLoggedUser();
        return (user != null) ? new LoggedUserDTO(user) : null;
    }

    @GetMapping("/approvedViewers")
    public List<User> approvedViewers() {
        return userService.approvedViewers();
    }
}
