package com.iscte_meta_systems.evoting_server.repositories;

import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectoralCircleRepository extends JpaRepository<ElectoralCircle, Long> {

    ElectoralCircle findByDistricts_DistrictName(String districtName);

    @Query("SELECT ec FROM ElectoralCircle ec WHERE ec.legislative.id = :electionId")
    List<ElectoralCircle> findElectoralCirclesByLegislativeElectionId(@Param("electionId") Long electionId);
}
