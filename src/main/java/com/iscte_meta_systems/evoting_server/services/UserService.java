package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Viewer;
import com.iscte_meta_systems.evoting_server.model.LoginDTO;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    String registerAdmin(UserRegisterDTO userRegisterDTO);

    String registerViewer(UserRegisterDTO userRegisterDTO);

    User getUserByUsername(String username);

    boolean userExists(String username);

    List<Viewer> pendingAuthorization();

    public boolean approveViewer(Long id);

    User getCurrentUser();

    User login(LoginDTO loginDTO);

    User getLoggedUser();

    public List<Viewer> approvedViewers();

    boolean deleteApprovedViewer(Long id);

    String getProfilePicture(Long id);

    boolean updateProfilePicture(Long id, String profilePicture);

    String uploadProfileImage(MultipartFile file);

    String getProfileImagePath(Long userId);

    boolean updateViewerInfo(Long id, String name, String institution, String username);
}
