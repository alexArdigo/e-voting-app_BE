package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.Municipalities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipalitiesRepository extends JpaRepository<Municipalities, Long> {
    Optional<Municipalities> findByMunicipalityName(String municipalityName);

    @Query("SELECT m FROM Municipalities m WHERE m.district.districtName = :districtName")
    List<Municipalities> findByDistrictName(@Param("districtName") String districtName);

    @Query("SELECT m FROM Municipalities m LEFT JOIN FETCH m.parishes WHERE m.municipalityName = :municipalityName")
    Optional<Municipalities> findByMunicipalityNameWithParishes(@Param("municipalityName") String municipalityName);
}
