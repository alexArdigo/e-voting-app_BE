package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
