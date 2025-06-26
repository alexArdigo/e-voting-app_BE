package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface DistrictRepository extends JpaRepository<District, Long> {

    List<District> findByDistrictNameContainingIgnoreCase(String district);
}
