package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.Parishes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParishesRepository extends JpaRepository<Parishes, Long> {
    Optional<Parishes> findByParishName(String parishName);

    @Query("SELECT p FROM Parishes p WHERE p.municipality.municipalityName = :municipalityName")
    List<Parishes> findByMunicipalityName(@Param("municipalityName") String municipalityName);

    @Query("SELECT p FROM Parishes p WHERE p.parishName LIKE %:name%")
    List<Parishes> findByParishNameContaining(@Param("name") String name);
}
