package com.iscte_meta_systems.evoting_server.services.InitializeDBInjection;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import com.iscte_meta_systems.evoting_server.repositories.*;
import com.iscte_meta_systems.evoting_server.services.ElectionService;
import com.iscte_meta_systems.evoting_server.services.ElectionServiceImpl;
import com.iscte_meta_systems.evoting_server.services.PartiesAndCandidatesService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.entities.ElectoralCircle;
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

    @PostConstruct
    public void init() {
        initializeElections();
//        initializeLegislatives();
//        initializeElectoralCircles();
//        initializeParties();

    }

    private List<OrganisationDTO> organisations;



    @Override
    public void initializeElections() {
        if (electionRepository.count() == 0) {
            ElectionDTO election1 = new ElectionDTO();
            election1.setName("Eleições Legislativas 2025");
            election1.setElectionType(ElectionType.LEGISLATIVE);
            election1.setElectoralCircleType(ElectoralCircleType.NATIONAL);
            election1.setDescription("Eleições para a Assembleia da República");
            election1.setStartDate("2026-03-10");
            election1.setEndDate("2026-03-11");
            election1.setDistrictName("Aveiro");
            election1.setSeats(16);
            electionService.createElection(election1);
            partiesAndCandidatesService.populatePartiesAndCandidatesFromJSONWithDTO(election1);

            ElectionDTO election2 = new ElectionDTO();
            election2.setName("Presidenciais 2026");
            election2.setElectionType(ElectionType.PRESIDENTIAL);
            election2.setDescription("Eleições para Presidente da República");
            election2.setStartDate("2026-08-20");
            election2.setEndDate("2026-08-20");

            electionService.createElection(election2);

//            electionRepository.save(election1);
//            electionRepository.save(election2);
        }
    }

    @Override
    public void initializeParties() {
        Optional<Election> optionalElection = electionRepository.findAll().stream().findFirst();

        if (optionalElection.isEmpty()) {
            return;
        }

        Election election = optionalElection.get();

        if (organisationRepository.count() == 0) {
            List<Party> parties = new ArrayList<>();

            parties.add(createParty("PS", "Partido Socialista", "#FF0000", "https://partidos.pt/logos/ps.png", election));
            parties.add(createParty("PSD", "Partido Social Democrata", "#FF9900", "https://partidos.pt/logos/psd.png", election));
            parties.add(createParty("Chega", "Chega", "#000000", "https://partidos.pt/logos/chega.png", election));
            parties.add(createParty("IL", "Iniciativa Liberal", "#0047AB", "https://partidos.pt/logos/il.png", election));
            parties.add(createParty("CDU", "Coligação Democrática Unitária", "#00923F", "https://partidos.pt/logos/cdu.png", election));
            parties.add(createParty("BE", "Bloco de Esquerda", "#A91B4E", "https://partidos.pt/logos/be.png", election));
            parties.add(createParty("PAN", "Pessoas-Animais-Natureza", "#63B74A", "https://partidos.pt/logos/pan.png", election));
            parties.add(createParty("LIVRE", "LIVRE", "#00B140", "https://partidos.pt/logos/livre.png", election));
            parties.add(createParty("CDS-PP", "Centro Democrático Social – Partido Popular", "#003399", "https://partidos.pt/logos/cdspp.png", election));
            parties.add(createParty("RIR", "Reagir Incluir Reciclar", "#FFA500", "https://partidos.pt/logos/rir.png", election));

            organisationRepository.saveAll(parties);

            UniParty unip = new UniParty();
            unip.setName("Candidato Independente");
            unip.setOrganisationName("Candidato Independente");
            unip.setImageUrl("https://img.candidato.pt");
            unip.setElection(election);
            organisationRepository.save(unip);
        }
    }

    @Override
    public Party createParty(
            String shortName,
            String fullName,
            String color,
            String logoUrl,
            Election election
    ) {
        Party party = new Party();
        party.setName(shortName);
        party.setOrganisationName(fullName);
        party.setDescription(fullName);
        party.setColor(color);
        party.setLogoUrl(logoUrl);
        party.setElection(election);
        return party;
    }

    @Override
    public void initializeElectoralCircles() {
        if (electionRepository.count() == 0) {
            List<District> districts = districtRepository.findAll();
            if (districts.isEmpty()) throw new IllegalStateException("Distritos não inicializados");

            ElectoralCircle aveiro2025 = createElectoralCircle(
                    "Ciclo Eleitoral Aveiro 2025",
                    "Eleições para a Assembleia da República",
                    LocalDateTime.of(2025, 7, 1, 8, 0),
                    LocalDateTime.of(2025, 7, 1, 19, 0),
                    true,
                    ElectionType.LEGISLATIVE,
                    ElectoralCircleType.NATIONAL,
                    16,
                    findDistrictByName(districts, "Aveiro")
            );

            ElectoralCircle lisboa2024 = createElectoralCircle(
                    "Ciclo Eleitoral Lisboa 2024",
                    "Eleições para a Assembleia da República",
                    LocalDateTime.of(2024, 1, 20, 8, 0),
                    LocalDateTime.of(2024, 1, 20, 19, 0),
                    false,
                    ElectionType.LEGISLATIVE,
                    ElectoralCircleType.NATIONAL,
                    48,
                    findDistrictByName(districts, "Lisboa")
            );

            ElectoralCircle lisboa2025 = createElectoralCircle(
                    "Ciclo Eleitoral Lisboa 2025",
                    "Eleições para a Assembleia da República",
                    LocalDateTime.of(2025, 7, 1, 8, 0),
                    LocalDateTime.of(2025, 7, 1, 19, 0),
                    true,
                    ElectionType.LEGISLATIVE,
                    ElectoralCircleType.NATIONAL,
                    48,
                    findDistrictByName(districts, "Lisboa")
            );

            Election presidencial2026 = new Election();
            presidencial2026.setName("Presidenciais 2026");
            presidencial2026.setDescription("Eleições para Presidente da República");
            presidencial2026.setStartDate(LocalDateTime.of(2026, 1, 20, 8, 0));
            presidencial2026.setEndDate(LocalDateTime.of(2026, 1, 20, 19, 0));
            presidencial2026.setStarted(false);
            presidencial2026.setType(ElectionType.PRESIDENTIAL);

            electionRepository.saveAll(List.of(
                    aveiro2025, lisboa2024, lisboa2025, presidencial2026
            ));
        }
    }

    private ElectoralCircle createElectoralCircle(String name, String description, LocalDateTime startDate, LocalDateTime endDate, boolean started, ElectionType type, ElectoralCircleType circleType, int seats, District district) {
        ElectoralCircle circle = new ElectoralCircle();
        circle.setName(name);
        circle.setDescription(description);
        circle.setStartDate(startDate);
        circle.setEndDate(endDate);
        circle.setStarted(started);
        circle.setType(type);
        circle.setElectoralCircleType(circleType);
        circle.setSeats(seats);
        circle.setDistricts(district);
        return circle;
    }

    private District findDistrictByName(List<District> list, String name) {
        return list.stream()
                .filter(d -> d.getDistrictName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Distrito não encontrado: " + name));

    }

    //    @Transactional
//    public void initializeVotes() {
//        if (voteRepository.count() == 0) {
//            Organisation organisation = organisationRepository.findAll().stream().findFirst().orElse(null);
//            Parish parish = parishRepository.findAll().stream().findFirst().orElse(null);
//
//            if (organisation == null || parish == null) {
//                return;
//            }
//
//            Municipality municipality = parish.getMunicipality();
//
//            Vote vote = new Vote();
//            vote.setOrganisation(organisation);
//            vote.setParish(parish);
//            vote.setMunicipality(municipality);
//
//            voteRepository.save(vote);
//        }
//    }

    //    public void initializeElections() {
//        if (electionRepository.count() == 0) {
//            Election election1 = new Election();
//            election1.setName("Ciclo Eleitoral Aveiro 2025");
//            election1.setDescription("Eleições para a Assembleia da República");
//            election1.setStartDate(LocalDateTime.of(2025, 7, 1, 8, 0));
//            election1.setEndDate(LocalDateTime.of(2025, 7, 1, 19, 0));
//            election1.setStarted(true);
//            election1.setType(ElectionType.LEGISLATIVE);
//
//            Election election2 = new Election();
//            election2.setName("Presidenciais 2026");
//            election2.setDescription("Eleições para Presidente da República");
//            election2.setStartDate(LocalDateTime.of(2026, 1, 20, 8, 0));
//            election2.setEndDate(LocalDateTime.of(2026, 1, 20, 19, 0));
//            election2.setStarted(false);
//            election2.setType(ElectionType.PRESIDENTIAL);
//
//            Election election3 = new Election();
//            election3.setName("Cicle Eleitoral Lisboa 2024");
//            election3.setDescription("Eleições para Presidente da República");
//            election3.setStartDate(LocalDateTime.of(2024, 1, 20, 8, 0));
//            election3.setEndDate(LocalDateTime.of(2024, 1, 20, 19, 0));
//            election3.setStarted(false);
//            election3.setType(ElectionType.LEGISLATIVE);
//
//            Election election4 = new Election();
//            election4.setName("Ciclo Eleitoral Lisboa 2025");
//            election4.setDescription("Eleições para a Assembleia da República");
//            election4.setStartDate(LocalDateTime.of(2025, 7, 1, 8, 0));
//            election4.setEndDate(LocalDateTime.of(2025, 7, 1, 19, 0));
//            election4.setStarted(true);
//            election4.setType(ElectionType.LEGISLATIVE);
//
//            electionRepository.save(election1);
//            electionRepository.save(election2);
//        }
//    }

}
