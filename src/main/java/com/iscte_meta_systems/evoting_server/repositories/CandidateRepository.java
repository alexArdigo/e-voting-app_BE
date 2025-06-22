package com.iscte_meta_systems.evoting_server.repositories;
import com.iscte_meta_systems.evoting_server.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
