package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Viewer;
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

    @PostMapping("/approveViewer/{viewerId}")
    public String approveViewer(@PathVariable Long viewerId) {
        if (userService.approveViewer(viewerId)) {
            return "Viewer approved successfully";
        } else {
            return "Viewer approval failed";
        }
    }

    @GetMapping("/findUserByUsername")
    public User findUserByUsername(@RequestParam("username") String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping ("/loggedUser")
    public User getLoggedUser() {
        return userService.getLoggedUser();
    }
}
