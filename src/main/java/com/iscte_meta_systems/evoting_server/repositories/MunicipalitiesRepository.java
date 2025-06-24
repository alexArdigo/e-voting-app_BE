package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipalitiesRepository extends JpaRepository<Municipality, Long> {
    Optional<Municipality> findByMunicipalityName(String municipalityName);

    @Query("SELECT m FROM Municipality m WHERE m.district.districtName = :districtName")
    List<Municipality> findByDistrictName(@Param("districtName") String districtName);

    @Query("SELECT m FROM Municipality m LEFT JOIN FETCH m.parishes WHERE m.municipalityName = :municipalityName")
    Optional<Municipality> findByMunicipalityNameWithParishes(@Param("municipalityName") String municipalityName);
}
