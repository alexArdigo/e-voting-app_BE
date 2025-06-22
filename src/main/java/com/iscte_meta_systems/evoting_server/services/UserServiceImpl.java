package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Admin;
import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Viewer;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import com.iscte_meta_systems.evoting_server.repositories.UserRepository;
import com.iscte_meta_systems.evoting_server.repositories.ViewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ViewerRepository viewerRepository;

    @Override
    public String registerAdmin(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null || userRegisterDTO.getUsername() == null || userRegisterDTO.getPassword() == null) {
            return "Invalid data provided";
        }
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            return "Username already exists";
        }
        Admin admin = new Admin(userRegisterDTO);
        admin.setRole("ADMIN");
        admin.setIsAuthorized(true);
        userRepository.save(admin);
        return "Admin registered successfully";
    }

    @Override
    public String registerViewer(UserRegisterDTO userRegisterDTO){
        if(userRegisterDTO == null || userRegisterDTO.getUsername() == null || userRegisterDTO.getPassword() == null) {
            return "Invalid data provided";
        }
        if(userRepository.existsByUsername(userRegisterDTO.getUsername())){
            return "Username already exists";
        }
        Viewer viewer = new Viewer(userRegisterDTO);
        viewer.setRole("VIEWER");
        viewerRepository.save(viewer);
        return "Viewer registered successfully";
    }


    @Override
    public User getUserByUsername(String username) {
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is either null or empty");
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean userExists(String username) {
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is either null or empty");
        }
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<Viewer> pendingAuthorization() {
        if(viewerRepository.findByIsAuthorizedFalse().isEmpty()) {
            throw new RuntimeException("No pending viewers found");
        }
        List<Viewer> pendingViewers = viewerRepository.findByIsAuthorizedFalse();
        return pendingViewers;
    }

    @Override
    public boolean approveViewer(Long viewerId) {
        Viewer viewer = viewerRepository.findById(viewerId).orElse(null);
        if (viewer != null) {
            viewer.setIsAuthorized(true);
            viewerRepository.save(viewer);
            return true;
        }
        return false;
    }
}
