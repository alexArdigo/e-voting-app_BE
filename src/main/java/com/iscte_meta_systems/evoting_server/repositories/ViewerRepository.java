package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewerRepository extends JpaRepository<Viewer, Long> {
    List<Viewer> findByIsAuthorizedTrue();
    List<Viewer> findByIsAuthorizedFalse();

    Viewer findByUsername(String username);
}
