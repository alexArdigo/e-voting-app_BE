package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Parish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface ParishRepository extends JpaRepository<Parish, Long> {

    List<Parish> findByParishNameContainingIgnoreCase(String municipality);


}
