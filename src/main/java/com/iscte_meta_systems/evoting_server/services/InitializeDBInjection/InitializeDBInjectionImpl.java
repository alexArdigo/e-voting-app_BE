package com.iscte_meta_systems.evoting_server.services.InitializeDBInjection;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.enums.Role;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
import com.iscte_meta_systems.evoting_server.repositories.*;
import com.iscte_meta_systems.evoting_server.services.ElectionService;
import com.iscte_meta_systems.evoting_server.services.ElectionServiceImpl;
import com.iscte_meta_systems.evoting_server.services.PartiesAndCandidatesService;
import com.iscte_meta_systems.evoting_server.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class InitializeDBInjectionImpl implements InitializeDBInjection {

    @Autowired
    private ElectionRepository electionRepository;
    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private ParishRepository parishRepository;
    @Autowired
    private MunicipalityRepository municipalityRepository;
    @Autowired
    private ElectoralCircleRepository electoralCircleRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private ElectionService electionService;
    @Autowired
    private PartiesAndCandidatesService partiesAndCandidatesService;
    @Autowired
    private UserService userService;
    @Autowired
    private ViewerRepository viewerRepository;

    @PostConstruct
    public void init() {
        initializeElections();
        initializeVotes();
//        initializeTestusers();
    }

    @Override
    public void initializeElections() {
        if (electionRepository.count() == 0) {
            ElectionDTO election1 = new ElectionDTO();
            election1.setName("Eleições Legislativas 2026");
            election1.setElectionType(ElectionType.LEGISLATIVE);
            election1.setElectoralCircleType(ElectoralCircleType.NATIONAL);
            election1.setDescription("Eleições para a Assembleia da República");
            election1.setStartDate("2026-07-09T11:10");
            election1.setEndDate("2026-07-09T11:15");
            electionService.createElection(election1);

            ElectionDTO election3 = new ElectionDTO();
            election3.setName("Eleições Portuguesas Legislativas 2025");
            election3.setElectionType(ElectionType.LEGISLATIVE);
            election3.setElectoralCircleType(ElectoralCircleType.NATIONAL);
            election3.setDescription("Eleições para a Assembleia da República");
            election3.setStartDate("2025-03-10T08:00");
            election3.setEndDate("2025-03-11T20:00");
            electionService.createElection(election3);


            ElectionDTO election2 = new ElectionDTO();
            election2.setName("Presidenciais 2026");
            election2.setElectionType(ElectionType.PRESIDENTIAL);
            election2.setDescription("Eleições para Presidente da República");
            election2.setStartDate("2025-07-09T12:22");
            election2.setEndDate("2025-07-09T12:24");

            electionService.createElection(election2);

            for (int i = 2; i <= 23; i++) {
                electionService.generateTestVotes(100, (long) i);

            }
        }
    }

    @Override
    public void initializeVotes() {
        for (int i = 2; i <= 23; i++) {
            electionService.generateTestVotes(10, (long) i);
        }
    }

    @Override
    public void initializeTestusers() {
        for (int i = 1; i <= 30; i++) {
            UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
            userRegisterDTO.setPassword("password" + i);
            userRegisterDTO.setRole(Role.VIEWER);
            userRegisterDTO.setUsername("viewer" + i);
            userRegisterDTO.setName("Viewer User " + i);
            userRegisterDTO.setInstitutionName("Test Institution " + i);
            userService.registerViewer(userRegisterDTO);
        }
    }
}
