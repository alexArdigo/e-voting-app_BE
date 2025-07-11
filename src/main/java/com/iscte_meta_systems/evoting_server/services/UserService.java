package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.entities.Viewer;
import com.iscte_meta_systems.evoting_server.model.LoginDTO;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;

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

    public List<User> approvedViewers();

    boolean deleteApprovedViewer(Long id);
}
