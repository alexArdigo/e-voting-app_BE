package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.Parish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParishRepository extends JpaRepository<Parish, Long> {
    Parish findByParishName(String parishName);

    @Query("SELECT p FROM Parish p WHERE p.municipality.municipalityName = :municipalityName")
    List<Parish> findByMunicipalityName(@Param("municipalityName") String municipalityName);

    @Query("SELECT p FROM Parish p WHERE p.parishName LIKE %:name%")
    List<Parish> findByParishNameContaining(@Param("name") String name);

    List<Parish> findByParishNameContainingIgnoreCase(String municipality);
}
