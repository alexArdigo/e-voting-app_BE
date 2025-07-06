package com.iscte_meta_systems.evoting_server.services.InitializeStuff;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.repositories.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class InitializeStuffImpl {

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

//    @PostConstruct
//    public void initializeData() {
//        initializeElections();
//        initializeParties();
//    }

    public void initializeElections() {
        if (electionRepository.count() == 0) {
            Election election1 = new Election();
            election1.setName("Eleições Legislativas 2025");
            election1.setDescription("Eleições para a Assembleia da República");
            election1.setStartDate(LocalDateTime.of(2025, 7, 1, 8, 0));
            election1.setEndDate(LocalDateTime.of(2025, 7, 1, 19, 0));
            election1.setStarted(true);
            election1.setType(ElectionType.LEGISLATIVE);

            Election election2 = new Election();
            election2.setName("Presidenciais 2026");
            election2.setDescription("Eleições para Presidente da República");
            election2.setStartDate(LocalDateTime.of(2026, 1, 20, 8, 0));
            election2.setEndDate(LocalDateTime.of(2026, 1, 20, 19, 0));
            election2.setStarted(false);
            election2.setType(ElectionType.PRESIDENTIAL);

            Election election3 = new Election();
            election2.setName("Legislativas 2024");
            election2.setDescription("Eleições para Presidente da República");
            election2.setStartDate(LocalDateTime.of(2024, 1, 20, 8, 0));
            election2.setEndDate(LocalDateTime.of(2024, 1, 20, 19, 0));
            election2.setStarted(false);
            election2.setType(ElectionType.LEGISLATIVE);

            electionRepository.save(election1);
            electionRepository.save(election2);
        }
    }

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

            for (Party party : parties) {
                organisationRepository.save(party);
            }

            UniParty unip = new UniParty();
            unip.setName("Candidato Independente");
            unip.setOrganisationName("Candidato Independente");
            unip.setImageUrl("https://img.candidato.pt");
            unip.setElection(election);
            organisationRepository.save(unip);
        }
    }

    private Party createParty(String shortName, String fullName, String color, String logoUrl, Election election) {
        Party party = new Party();
        party.setName(shortName);
        party.setOrganisationName(fullName);
        party.setDescription(fullName);
        party.setColor(color);
        party.setLogoUrl(logoUrl);
        party.setElection(election);
        return party;
    }


    @Transactional
    public void initializeVotes() {
        if (voteRepository.count() == 0) {
            Organisation organisation = organisationRepository.findAll().stream().findFirst().orElse(null);
            Parish parish = parishRepository.findAll().stream().findFirst().orElse(null);

            if (organisation == null || parish == null) {
                return;
            }

            Municipality municipality = parish.getMunicipality();

            Vote vote = new Vote();
            vote.setOrganisation(organisation);
            vote.setParish(parish);
            vote.setMunicipality(municipality);

            voteRepository.save(vote);
        }
    }

}
