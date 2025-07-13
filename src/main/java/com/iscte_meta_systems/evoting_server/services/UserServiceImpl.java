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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static java.lang.System.out;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ViewerRepository viewerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

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
    public String registerViewer(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null || userRegisterDTO.getUsername() == null || userRegisterDTO.getPassword() == null) {
            throw new IllegalArgumentException("Invalid data provided");
        }
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
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
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is either null or empty");
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean userExists(String username) {
        if (username == null || username.isEmpty()) {
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
    public String uploadProfileImage(MultipartFile file) {

        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            User user = userService.getLoggedUser();
            if (user == null) {
                throw new RuntimeException("User is not logged in");
            }

            String uploadDirPath = "uploads";
            File uploadDir = new File(uploadDirPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            } else {
                fileExtension = ".jpg";
            }

            String fileName = "profile_" + user.getId() + fileExtension;
            Path filePath = Paths.get(uploadDirPath, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Viewer viewer = viewerRepository.findByUsername(user.getUsername());

            viewer.setProfilePicture(fileName);
            viewerRepository.save(viewer);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image" + e.getMessage());
        }
    }

    @Override
    public String getProfileImagePath(Long userId) {

        try {
            User user = userRepository.findById(userId).orElse(null);
            assert user != null;
            Viewer viewer = viewerRepository.findByUsername(user.getUsername());
            if (viewer == null || viewer.getProfilePicture() == null) {
                return null;
            }
            return "uploads" + File.separator + viewer.getProfilePicture();
        } catch (Exception e) {

            return null;
        }
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
            User admin = new Admin(new UserRegisterDTO(
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
}