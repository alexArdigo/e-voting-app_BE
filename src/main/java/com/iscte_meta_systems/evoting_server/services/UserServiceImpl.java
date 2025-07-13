package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Admin;
import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Viewer;
import com.iscte_meta_systems.evoting_server.enums.Role;
import com.iscte_meta_systems.evoting_server.model.LoginDTO;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import com.iscte_meta_systems.evoting_server.repositories.UserRepository;
import com.iscte_meta_systems.evoting_server.repositories.ViewerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.System.out;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ViewerRepository viewerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if (user == null || user.getId() == null) {
            return null;
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
            return null;
        return user;
    }

    @Override
    public User getLoggedUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userRepository.existsByUsername(username)) {
            return null;
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public String registerAdmin(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null || userRegisterDTO.getUsername() == null || userRegisterDTO.getPassword() == null) {
            return "Invalid data provided";
        }
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            return "Username already exists";
        }
        Admin admin = new Admin(userRegisterDTO);
        admin.setRole(Role.ADMIN);
        admin.setIsAuthorized(true);
        userRepository.save(admin);
        return "Admin registered successfully";
    }

    @Override
    public String registerViewer(UserRegisterDTO userRegisterDTO){
        if(userRegisterDTO == null || userRegisterDTO.getUsername() == null || userRegisterDTO.getPassword() == null) {
            throw new IllegalArgumentException("Invalid data provided");
        }
        if(userRepository.existsByUsername(userRegisterDTO.getUsername())){
            throw new IllegalArgumentException("Username already exists");
        }
        Viewer viewer = new Viewer(userRegisterDTO);
        viewer.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        viewer.setRole(Role.VIEWER);
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
        return viewerRepository.findByIsAuthorizedFalse();
    }

    @Override
    public boolean approveViewer(Long id) {
        Viewer viewer = viewerRepository.findById(id).orElse(null);
        if (viewer != null) {
            viewer.setIsAuthorized(true);
            viewerRepository.save(viewer);
            return true;
        }
        return false;
    }

    @Override
    public List<Viewer> approvedViewers() {
        return viewerRepository.findByIsAuthorizedTrue();
    }

    @Override
    public boolean deleteApprovedViewer(Long id) {
        Viewer viewer = viewerRepository.findById(id).orElse(null);
        if (viewer != null && viewer.getIsAuthorized()) {
            viewerRepository.delete(viewer);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateProfilePicture(Long id, String profilePicture) {
        User user = userRepository.findById(id).orElse(null);
        if (!(user instanceof Viewer viewer)) return false;

        List<String> allowedImages = List.of("https://play-lh.googleusercontent.com/JZYM9BfoFZxY-NYrjmQr6BPpireEvDvcVliADoG-XpESbjQC3tu170Qjb-wgdWGwfUC3=s188-rw",
                "https://upload.wikimedia.org/wikipedia/commons/7/7b/Logo_impresa.gif",
                "https://eco.imgix.net/uploads/2017/06/cropped-mediacapital.png?w=372&q=60&auto=compress,format",
                "https://cdn-icons-png.flaticon.com/512/10109/10109817.png",
                "https://apradiodifusao.pt/wp-content/uploads/2025/04/imagem-2.jpg");
        if (!allowedImages.contains(profilePicture)) return false;

        viewer.setProfilePicture(profilePicture);
        userRepository.save(viewer);
        return true;
    }

    @Override
    public String getProfilePicture(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user instanceof Viewer) {
            return ((Viewer) user).getProfilePicture();
        }
        return null;
    }

    @Override
    public User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userRepository.existsByUsername(username))
            return null;
        return userRepository.findByUsername(username);
    }

    @PostConstruct
    public void init() {
        User existingAdmin = userRepository.findByUsername("Admin");
        String encodedPassword = passwordEncoder.encode("123456");
        if (existingAdmin == null) {
            User admin = new Admin( new UserRegisterDTO(
                    "Admin",
                    encodedPassword,
                    "Admin",
                    "Admin"
            )
            );
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            out.println("Admin registered");
        }
    }

    @Override
    public boolean updateViewerInfo(Long id, String name, String institution, String username) {
        Viewer viewer = viewerRepository.findById(id).orElse(null);
        if (viewer != null) {
            viewer.setName(name);
            viewer.setUsername(username);
            viewer.setInstitutionName(institution);
            viewerRepository.save(viewer);
            return true;
        }
        return false;
    }
}