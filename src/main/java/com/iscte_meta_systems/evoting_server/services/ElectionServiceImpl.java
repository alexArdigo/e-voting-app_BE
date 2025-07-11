package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.enums.VoteType;
import com.iscte_meta_systems.evoting_server.model.*;
import com.iscte_meta_systems.evoting_server.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ElectionServiceImpl implements ElectionService {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private PartiesAndCandidatesService partiesAndCandidatesService;

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private VoterService voterService;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private LegislativeRepository legislativeRepository;

    @Autowired
    private VoterHashRepository voterHashRepository;

    @Autowired
    private ParishRepository parishRepository;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private ElectoralCircleRepository electoralCircleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UploadFileService upLoadFileService;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private UniPartyRepository uniPartyRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public List<ElectionDTO> getPresidentialOrElectoralCircle(String electionType, Integer electionYear, Boolean isActive) {
        List<Election> elections = Optional.ofNullable(electionType)
                .map(type -> electionRepository.findAllByType(ElectionType.valueOf(type.toUpperCase())))
                .orElseGet(electionRepository::findAll);
        return processElections(elections, electionYear, isActive);
    }

    private List<ElectionDTO> processElections(List<Election> elections, Integer electionYear, Boolean isActive) {
        return elections.stream()
                .filter(e -> filterByYear(e, electionYear))
                .filter(e -> filterByActive(e, isActive))
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<Legislative> getLegislativeElections(Integer electionYear, Boolean isActive) {
        List<Legislative> elections = legislativeRepository.findAll();

        return elections.stream()
                .filter(e -> electionYear == null || e.getYear() == electionYear)
                .filter(e -> isActive == null || e.isStarted() == isActive)
                .collect(Collectors.toList());
    }


    private ElectionDTO convertToDTO(Election election) {
        ElectionDTO dto = new ElectionDTO();
        dto.setId(election.getId());
        dto.setName(election.getName());
        dto.setDescription(election.getDescription());
        dto.setStartDate(election.getStartDate() != null ? election.getStartDate().toString() : null);
        dto.setEndDate(election.getEndDate() != null ? election.getEndDate().toString() : null);
        dto.setElectionType(election.getType());
        dto.setStarted(election.isStarted());

        if (election.getOrganisations() != null) {
            dto.setOrganisations(election.getOrganisations()
                    .stream()
                    .map(this::convertOrgToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private OrganisationDTO convertOrgToDTO(Organisation org) {
        OrganisationDTO dto = new OrganisationDTO();
        dto.setName(org.getOrganisationName());
        dto.setOrganisationType(org.getOrganisationType());
        dto.setElectionId(org.getElection() != null ? org.getElection().getId() : null);
        return dto;
    }

    @Override
    public Election getElectionById(Long id) {
        return electionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Election with ID " + id + " was not found."));
    }

    @Override
    public ElectionDTO createElection(ElectionDTO dto) {
        if (dto.getName() == null) {
            throw new IllegalArgumentException("Election name is required.");
        }
        if (dto.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required.");
        }

        LocalDateTime startDate = parseDateTimeFlexible(dto.getStartDate());
        LocalDateTime endDate = parseDateTimeFlexible(dto.getEndDate());

        if (dto.getElectionType() == ElectionType.PRESIDENTIAL) {
            if (dto.getEndDate() == null || dto.getEndDate().trim().isEmpty()) {
                throw new IllegalArgumentException("End date is required for presidential elections.");
            }
            endDate = parseDateTimeFlexible(dto.getEndDate());
            if (endDate == null || endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("End date cannot be before start date or is invalid.");
            }
        }
        Election election;
        switch (dto.getElectionType()) {
            case PRESIDENTIAL:
                election = new Presidential();
                election.setType(ElectionType.PRESIDENTIAL);
                election.setName(dto.getName());
                election.setDescription(dto.getDescription());
                election.setStartDate(startDate);
                election.setEndDate(endDate);
                election = electionRepository.save(election);

                ElectionDTO presidentialResult = new ElectionDTO();
                presidentialResult.setId(election.getId());
                presidentialResult.setName(election.getName());
                presidentialResult.setDescription(election.getDescription());
                presidentialResult.setStartDate(election.getStartDate().toString());
                presidentialResult.setEndDate(election.getEndDate().toString());
                presidentialResult.setElectionType(election.getType());
                return presidentialResult;

            case LEGISLATIVE:
                Legislative legislative = new Legislative();
                legislative.setName(dto.getName());
                legislative.setDescription(dto.getDescription());
                legislative.setStartDate(startDate);
                legislative.setEndDate(endDate);
                legislative = legislativeRepository.save(legislative);

                List<String> distritos = List.of(
                        "Viana do Castelo", "Braga", "Vila Real", "Bragança", "Porto", "Aveiro", "Viseu", "Guarda",
                        "Coimbra", "Leiria", "Castelo Branco", "Santarém", "Lisboa", "Portalegre", "Évora", "Setúbal",
                        "Beja", "Faro", "Madeira", "Açores", "Europa", "Fora da Europa"
                );
                int[] seatsDistritos = {6, 19, 5, 3, 40, 16, 8, 3, 9, 10, 4, 9, 48, 2, 3, 18, 3, 9, 6, 5, 2, 2};

                List<ElectoralCircle> circles = new ArrayList<>();
                for (int i = 0; i < distritos.size(); i++) {
                    ElectoralCircle circle = new ElectoralCircle();
                    circle.setName(dto.getName() + " - " + distritos.get(i));
                    circle.setSeats(seatsDistritos[i]);
                    circle.setElectoralCircleType(ElectoralCircleType.NATIONAL);
                    circle.setStartDate(startDate);
                    circle.setEndDate(endDate);
                    circle.setDescription(dto.getDescription());
                    circle.setType(ElectionType.LEGISLATIVE);

                    District district = districtRepository.findByDistrictName(distritos.get(i));
                    if (district != null) {
                        circle.setDistricts(district);
                    }

                    circle.setLegislative(legislative);
                    circle = electoralCircleRepository.save(circle);

                    try {
                        partiesAndCandidatesService.populatePartiesAndCandidatesFromJSON(circle);
                    } catch (Exception e) {
                        System.err.println("Error populating parties for circle " + circle.getName() + ": " + e.getMessage());
                    }

                    circles.add(circle);
                }

                ElectionDTO legislativeResult = new ElectionDTO();
                legislativeResult.setId(legislative.getId());
                legislativeResult.setName(legislative.getName());
                legislativeResult.setDescription(legislative.getDescription());
                legislativeResult.setStartDate(legislative.getStartDate().toString());
                legislativeResult.setElectionType(ElectionType.LEGISLATIVE);
                legislativeResult.setElectoralCircleType(ElectoralCircleType.NATIONAL);
                return legislativeResult;

            default:
                throw new IllegalArgumentException("Unknown election type: " + dto.getElectionType());
        }
    }

    @Override
    public List<Organisation> getBallotByElectionId(Long id) {
        Election election = getElectionById(id);
        return election.getOrganisations();
    }

    @Override
    public Vote castVote(Long electionId, VoteRequestModel voteRequest) {
        Election election = getElectionById(electionId);
        Voter voter = voterService.getLoggedVoter();

        String voterNif = voter.getNif().toString();
        if (election.getVotersVoted() != null) {
            for (VoterHash storedHash : election.getVotersVoted()) {
                if (passwordEncoder.matches(voterNif, storedHash.getHashIdentification())) {
                    throw new IllegalStateException("Voter has already voted in this election.");
                }
            }
        }

        if (!election.isStarted()) {
            throw new IllegalStateException("Election has not started.");
        }

        Parish parish = voter.getParish();
        Municipality municipality = voter.getMunicipality();

        Organisation organisation = organisationRepository.findOrganisationsById(voteRequest.getOrganisationId());

        Vote vote = new Vote();
        vote.setOrganisation(organisation);
        vote.setMunicipality(municipality);
        vote.setParish(parish);

        if (voteRequest.getOrganisationId() == -1) {
            vote.setVoteType(VoteType.BLANK);
        } else if (organisation.getId() == null) {
            vote.setVoteType(VoteType.INVALID);
        } else {
            vote.setVoteType(VoteType.VALID);
        }

        vote = voteRepository.save(vote);
        election.addVote(vote);

        if (election.getVotersVoted() == null) {
            election.setVotersVoted(new ArrayList<>());
        }

        String hashedNif = passwordEncoder.encode(voterNif);
        VoterHash newVoterHash = new VoterHash();
        newVoterHash.setHashIdentification(hashedNif);
        election.getVotersVoted().add(newVoterHash);

        electionRepository.save(election);

        return vote;
    }

    @Override
    public Boolean isStarted(Long id) {
        Election election = getElectionById(id);
        return election.isStarted();
    }

    @Transactional
    @Override
    public List<Vote> generateTestVotes(int numberOfVotes, Long electionId) {
        List<Organisation> organisations = organisationRepository.findAll();

        ElectoralCircle electoralCircle = electoralCircleRepository.findById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Electoral Circle with ID " + electionId + " was not found."));

        List<Municipality> municipalities = municipalityRepository.findAll().stream()
                .filter(m -> m.getDistrict().getDistrictName()
                        .equals(electoralCircle.getDistricts().getDistrictName()))
                .toList();

        List<Vote> votes = new ArrayList<>();

        for (int i = 0; i < numberOfVotes; i++) {
            Municipality municipality = municipalities.get((int) (Math.random() * municipalities.size()));

            List<Parish> parishesFromMunicipality = parishRepository.findByMunicipalityId(municipality.getId());
            if (parishesFromMunicipality == null || parishesFromMunicipality.isEmpty()) {
                continue;
            }

            Parish parish = parishesFromMunicipality.get((int) (Math.random() * parishesFromMunicipality.size()));
            Organisation organisation = organisations.get((int) (Math.random() * organisations.size()));

            Vote vote = new Vote();
            vote.setParish(parish);
            vote.setMunicipality(municipality);
            vote.setOrganisation(organisation);

            votes.add(vote);
            electoralCircle.addVote(vote);
        }

        voteRepository.saveAll(votes);
        electoralCircleRepository.save(electoralCircle);

        return votes;
    }

    @Override
    public Legislative getLegislativeById(Long legislativeID) {
        return legislativeRepository.findById(legislativeID).orElseThrow(() -> new IllegalArgumentException("Legislative with ID " + legislativeID + " was not found."));
    }

    @Override
    public List<Legislative> getLegislatives() {
        return legislativeRepository.findAll();
    }

    @Override
    @Transactional
    public ElectionDTO updatePresidentialElection(Long id, ElectionDTO electionDTO) {
        Election election = getElectionById(id);

        if (!(election instanceof Presidential)) {
            throw new IllegalArgumentException("The election with ID " + id + " is not a presidential election.");
        }

        Presidential presidential = (Presidential) election;

        if (presidential.isStarted()) {
            throw new IllegalStateException("Cannot edit a presidential election that has already started.");
        }

        validateElectionDTO(electionDTO);

        LocalDateTime startDate = parseDateTimeFlexible(electionDTO.getStartDate());
        LocalDateTime endDate = parseDateTimeFlexible(electionDTO.getEndDate());

        validateDates(startDate, endDate);

        updateElectionFields(presidential, electionDTO, startDate, endDate);

        Presidential updatedElection = (Presidential) electionRepository.save(presidential);
        return convertToDTO(updatedElection);
    }

    @Override
    @Transactional
    public void deletePresidentialElection(Long id) {
        Election election = getElectionById(id);

        if (!(election instanceof Presidential)) {
            throw new IllegalArgumentException("The election with ID " + id + " is not a presidential election.");
        }

        Presidential presidential = (Presidential) election;

        validateElectionForDeletion(presidential);

        clearCollections(presidential.getCandidates(), presidential.getOrganisations(), presidential.getVotersVoted());

        electionRepository.delete(presidential);
    }

    @Override
    @Transactional
    public Legislative updateLegislativeElection(Long id, LegislativeDTO legislativeDTO) {
        Legislative legislative = getLegislativeById(id);

        if (legislative.isStarted()) {
            throw new IllegalStateException("Cannot edit a legislative election that has already started.");
        }

        validateLegislativeDTO(legislativeDTO);

        LocalDateTime startDate = parseDateTimeFlexible(legislativeDTO.getStartDate());
        LocalDateTime endDate = null;

        if (legislativeDTO.getEndDate() != null && !legislativeDTO.getEndDate().trim().isEmpty()) {
            endDate = parseDateTimeFlexible(legislativeDTO.getEndDate());
            validateDates(startDate, endDate);
        }

        legislative.setName(legislativeDTO.getName().trim());
        legislative.setDescription(legislativeDTO.getDescription() != null ?
                legislativeDTO.getDescription().trim() : null);
        legislative.setStartDate(startDate);

        if (legislative.getElectoralCircles() != null) {
            for (ElectoralCircle circle : legislative.getElectoralCircles()) {
                String districtName = circle.getDistricts() != null ? circle.getDistricts().getDistrictName() : "";
                circle.setName(legislativeDTO.getName() + " - " + districtName);
                circle.setDescription(legislativeDTO.getDescription());
                circle.setStartDate(startDate);
                circle.setEndDate(endDate);
                electoralCircleRepository.save(circle);
            }
        }

        return legislativeRepository.save(legislative);
    }

    @Override
    @Transactional
    public void deleteLegislativeElection(Long id) {
        Legislative legislative = getLegislativeById(id);

        if (legislative.isStarted()) {
            throw new IllegalStateException("Cannot delete a legislative election that has already started.");
        }

        if (legislative.getElectoralCircles() != null) {
            for (ElectoralCircle circle : legislative.getElectoralCircles()) {
                if (circle.getVotes() != null && !circle.getVotes().isEmpty()) {
                    throw new IllegalStateException("Cannot delete a legislative election that already has votes.");
                }
            }
        }

        if (legislative.getElectoralCircles() != null) {
            List<ElectoralCircle> circlesToDelete = new ArrayList<>(legislative.getElectoralCircles());
            for (ElectoralCircle circle : circlesToDelete) {
                deleteElectoralCircleInternal(circle);
            }
        }

        legislativeRepository.delete(legislative);
    }

    @Override
    @Transactional
    public ElectoralCircle updateElectoralCircle(Long id, ElectoralCircleDTO electoralCircleDTO) {
        ElectoralCircle circle = electoralCircleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Electoral circle with ID " + id + " not found."));

        if (circle.isStarted()) {
            throw new IllegalStateException("Cannot edit an electoral circle that has already started.");
        }

        validateElectoralCircleDTO(electoralCircleDTO);

        LocalDateTime startDate = parseDateTimeFlexible(electoralCircleDTO.getStartDate());
        LocalDateTime endDate = parseDateTimeFlexible(electoralCircleDTO.getEndDate());

        validateDates(startDate, endDate);

        circle.setName(electoralCircleDTO.getName().trim());
        circle.setDescription(electoralCircleDTO.getDescription() != null ?
                electoralCircleDTO.getDescription().trim() : null);
        circle.setStartDate(startDate);
        circle.setEndDate(endDate);
        circle.setSeats(electoralCircleDTO.getSeats());

        if (electoralCircleDTO.getDistrictName() != null && !electoralCircleDTO.getDistrictName().trim().isEmpty()) {
            District district = districtRepository.findByDistrictName(electoralCircleDTO.getDistrictName());
            if (district == null) {
                throw new IllegalArgumentException("District with name '" + electoralCircleDTO.getDistrictName() + "' not found.");
            }
            circle.setDistricts(district);
        }

        return electoralCircleRepository.save(circle);
    }

    @Override
    @Transactional
    public void deleteElectoralCircle(Long id) {
        ElectoralCircle circle = electoralCircleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Electoral circle with ID " + id + " not found."));

        deleteElectoralCircleInternal(circle);
    }

    @Override
    public void uploadCSV(MultipartFile file, Long electionId) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        Election election = getElectionById(electionId);
        if (election == null) {
            throw new IllegalArgumentException("Election with ID " + electionId + " not found.");
        }

        try {
            if (election.getType() == ElectionType.PRESIDENTIAL) {
                // For presidential elections, parse and add UniParty entities directly to the election
                List<Organisation> organisations = upLoadFileService.parceCSVFile(file, election);
                for (Organisation org : organisations) {
                    org.setElection(election);
                    if (org instanceof UniParty) {
                        uniPartyRepository.save((UniParty) org);
                    }
                    election.addOrganisation(org);
                }
                electionRepository.save(election);

            } else if (election.getType() == ElectionType.LEGISLATIVE) {
                // For legislative elections, follow the same pattern as createElection:
                // Get the Legislative entity and add parties to each of its electoral circles
                Legislative legislative = legislativeRepository.findById(electionId)
                        .orElseThrow(() -> new IllegalArgumentException("Legislative election with ID " + electionId + " not found."));

                // Parse the CSV to get the template parties and candidates
                List<Organisation> templateOrganisations = upLoadFileService.parceCSVFile(file, election);

                if (legislative.getElectoralCircles() != null) {
                    for (ElectoralCircle circle : legislative.getElectoralCircles()) {
                        // For each electoral circle, create copies of the template parties
                        for (Organisation templateOrg : templateOrganisations) {
                            if (templateOrg instanceof Party) {
                                Party templateParty = (Party) templateOrg;

                                // Create a new party instance for this electoral circle
                                Party partyForCircle = new Party();
                                partyForCircle.setOrganisationName(templateParty.getOrganisationName());
                                partyForCircle.setName(templateParty.getName());
                                partyForCircle.setColor(templateParty.getColor());
                                partyForCircle.setLogoUrl(templateParty.getLogoUrl());
                                partyForCircle.setDescription(templateParty.getDescription());
                                partyForCircle.setElection(election); // Set election reference

                                // Copy candidates for this party
                                List<Candidate> candidatesForCircle = new ArrayList<>();
                                if (templateParty.getCandidates() != null) {
                                    for (Candidate templateCandidate : templateParty.getCandidates()) {
                                        Candidate candidateForCircle = new Candidate();
                                        candidateForCircle.setName(templateCandidate.getName());
                                        candidateForCircle.setImageUrl(templateCandidate.getImageUrl());
                                        candidateForCircle = candidateRepository.save(candidateForCircle);
                                        candidatesForCircle.add(candidateForCircle);
                                    }
                                }
                                partyForCircle.setCandidates(candidatesForCircle);

                                // Save the party and add it to the electoral circle
                                partyForCircle = partyRepository.save(partyForCircle);

                                if (circle.getParties() == null) {
                                    circle.setParties(new ArrayList<>());
                                }
                                circle.getParties().add(partyForCircle);
                            }
                        }
                        // Save the electoral circle with its new parties
                        electoralCircleRepository.save(circle);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file: " + e.getMessage(), e);
        }
    }

    private void deleteElectoralCircleInternal(ElectoralCircle circle) {
        if (circle.isStarted()) {
            throw new IllegalStateException("Cannot delete an electoral circle that has already started.");
        }

        if (circle.getVotes() != null && !circle.getVotes().isEmpty()) {
            throw new IllegalStateException("Cannot delete an electoral circle that already has votes.");
        }

        clearCollections(circle.getParties(), circle.getMunicipalities(),
                circle.getParish(), circle.getOrganisations(), circle.getVotersVoted());

        Legislative legislative = circle.getLegislative();
        if (legislative != null) {
            legislative.getElectoralCircles().remove(circle);
            legislativeRepository.save(legislative);
        }

        electoralCircleRepository.delete(circle);
    }

    private void validateLegislativeDTO(LegislativeDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Legislative election name is required.");
        }
        if (dto.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required.");
        }
    }

    private void validateElectionDTO(ElectionDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Election name is required.");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }
    }

    private void validateElectoralCircleDTO(ElectoralCircleDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Electoral circle name is required.");
        }
        if (dto.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required.");
        }
        if (dto.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required.");
        }
        if (dto.getSeats() <= 0) {
            throw new IllegalArgumentException("Number of seats must be greater than zero.");
        }
    }

    private LocalDateTime parseDateTimeFlexible(String dateStr) {
        if (dateStr == null) return null;
        try {
            return LocalDateTime.parse(dateStr);
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException ex) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return LocalDate.parse(dateStr, formatter).atStartOfDay();
                } catch (DateTimeParseException exc) {
                    throw new IllegalArgumentException("Invalid date format: " + dateStr);
                }
            }
        }
    }

    private boolean filterByYear(Election election, Integer year) {
        return year == null || (election.getStartDate() != null && election.getStartDate().getYear() == year);
    }

    private boolean filterByActive(Election election, Boolean isActive) {
        return isActive == null || election.isStarted() == isActive;
    }

    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
    }

    private void updateElectionFields(Election election, ElectionDTO dto, LocalDateTime startDate, LocalDateTime endDate) {
        election.setName(dto.getName().trim());
        election.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        election.setStartDate(startDate);
        election.setEndDate(endDate);
    }

    private void validateElectionForDeletion(Election election) {
        if (election.isStarted()) {
            throw new IllegalStateException("Cannot delete an election that has already started.");
        }

        if (election.getVotes() != null && !election.getVotes().isEmpty()) {
            throw new IllegalStateException("Cannot delete an election that already has votes.");
        }
    }

    private void clearCollections(List<?>... collections) {
        for (List<?> collection : collections) {
            if (collection != null) {
                collection.clear();
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void scheduledStartElections() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Lisbon")).withSecond(0).withNano(0);
        List<Election> electionsToStart = electionRepository.findAll().stream()
                .filter(e -> !e.isStarted() &&
                        e.getStartDate() != null &&
                        ZonedDateTime.of(e.getStartDate().withSecond(0).withNano(0), ZoneId.of("Europe/Lisbon")).isEqual(now))
                .toList();
        for (Election election : electionsToStart) {
            election.setStarted(true);
            election.startElection();
            electionRepository.save(election);
        }
        List<Legislative> legislativesToStart = legislativeRepository.findAll().stream()
                .filter(l -> !l.isStarted() &&
                        l.getStartDate() != null &&
                        ZonedDateTime.of(l.getStartDate().withSecond(0).withNano(0), ZoneId.of("Europe/Lisbon")).isEqual(now))
                .toList();
        for (Legislative legislative : legislativesToStart) {
            legislative.setStarted(true);
            legislativeRepository.save(legislative);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void scheduledEndElections() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Lisbon")).withSecond(0).withNano(0);
        List<Election> electionsToEnd = electionRepository.findAll().stream()
                .filter(e -> e.isStarted() &&
                        e.getEndDate() != null &&
                        ZonedDateTime.of(e.getEndDate().withSecond(0).withNano(0), ZoneId.of("Europe/Lisbon")).isEqual(now))
                .toList();
        for (Election election : electionsToEnd) {
            election.endElection();
            electionRepository.save(election);
        }
        List<Legislative> legislativesToEnd = legislativeRepository.findAll().stream()
                .filter(l -> l.isStarted() &&
                        l.getEndDate() != null &&
                        ZonedDateTime.of(l.getEndDate().withSecond(0).withNano(0), ZoneId.of("Europe/Lisbon")).isEqual(now))
                .toList();
        for (Legislative legislative : legislativesToEnd) {
            legislative.setStarted(false);
            legislativeRepository.save(legislative);
        }
    }
}

