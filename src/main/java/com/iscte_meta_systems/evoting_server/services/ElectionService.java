package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.ElectoralCircleDTO;
import com.iscte_meta_systems.evoting_server.model.LegislativeDTO;
import com.iscte_meta_systems.evoting_server.model.VoteRequestModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ElectionService {
    List<Organisation> getBallotByElectionId(Long id);

    List<ElectionDTO> getPresidentialOrElectoralCircle(String electionType, Integer electionYear, Boolean isActive);

    List<Legislative> getLegislativeElections(Integer electionYear, Boolean isActive);

    Election getElectionById(Long id);

    ElectionDTO createElection(ElectionDTO electionDTO);

    Vote castVote(Long id, VoteRequestModel vote);

    Boolean isStarted(Long id);

    List<Vote> generateTestVotes(int numberOfVotes, Long electionId);

    Legislative getLegislativeById(Long legislativeID);

    List<Legislative> getLegislatives();

    ElectionDTO updatePresidentialElection(Long id, ElectionDTO electionDTO);
    void deletePresidentialElection(Long id);

    Legislative updateLegislativeElection(Long id, LegislativeDTO legislativeDTO);
    void deleteLegislativeElection(Long id);

    ElectoralCircle updateElectoralCircle(Long id, ElectoralCircleDTO electoralCircleDTO);
    void deleteElectoralCircle(Long id);

    void uploadCSV(MultipartFile file, Long electionId);

    List<Long> getAllElectoralCircleIds(Long id);
}
