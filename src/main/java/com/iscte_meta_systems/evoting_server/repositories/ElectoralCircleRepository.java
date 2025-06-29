package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectoralCircleRepository extends JpaRepository<ElectoralCircle, Long> {

    ElectoralCircle findByDistricts_DistrictName(String districtName);
}
