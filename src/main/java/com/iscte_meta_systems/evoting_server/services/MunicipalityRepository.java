package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface MunicipalityRepository extends JpaRepository<Municipality, Long> {

    List<Municipality> findByMunicipalityNameContainingIgnoreCase(String municipality);
}
