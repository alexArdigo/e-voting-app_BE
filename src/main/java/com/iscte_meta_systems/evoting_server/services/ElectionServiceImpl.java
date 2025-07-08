package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import com.iscte_meta_systems.evoting_server.model.VoteRequestModel;
import com.iscte_meta_systems.evoting_server.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectionServiceImpl implements ElectionService {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private PartiesAndCandidatesService partiesAndCandidatesService;

    @Autowired
    private VoterService voterService;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private LegislativeRepository legislativeRepository;

    @Autowired
    private ParishRepository parishRepository;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private ElectoralCircleRepository electoralCircleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public List<ElectionDTO> getElections(String electionType, Integer electionYear) {
        List<Election> elections = electionRepository.findAllByType(ElectionType.PRESIDENTIAL);

         return elections.stream()
                .filter(e -> electionType == null || e.getClass().getSimpleName().equalsIgnoreCase(electionType))
                .filter(e -> electionYear == null || (e.getStartDate() != null && e.getStartDate().getYear() == electionYear))
                .map(e -> {
                    ElectionDTO dto = new ElectionDTO();
                    dto.setName(e.getName());
                    dto.setDescription(e.getDescription());
                    dto.setStartDate(e.getStartDate() != null ? e.getStartDate().toString() : null);
                    dto.setEndDate(e.getEndDate() != null ? e.getEndDate().toString() : null);
                    dto.setElectionType(e.getType());

                    if (e.getOrganisations() != null) {
                        List<OrganisationDTO> orgDtos = e.getOrganisations().stream()
                                .map(org -> {
                                    OrganisationDTO orgDto = new OrganisationDTO();
                                    orgDto.setName(org.getOrganisationName());
                                    orgDto.setOrganisationType(org.getOrganisationType());
                                    orgDto.setElectionId(org.getElection() != null ? org.getElection().getId() : null);
                                    return orgDto;
                                })
                                .collect(Collectors.toList());
                        dto.setOrganisations(orgDtos);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
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
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }

        LocalDateTime startDate = parseDateTimeFlexible(dto.getStartDate());
        LocalDateTime endDate = parseDateTimeFlexible(dto.getEndDate());

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
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
                presidentialResult.setName(election.getName());
                presidentialResult.setDescription(election.getDescription());
                presidentialResult.setStartDate(election.getStartDate().toString());
                presidentialResult.setEndDate(election.getEndDate().toString());
                presidentialResult.setElectionType(election.getType());
                return presidentialResult;

            case LEGISLATIVE:
                Election baseElection = new Election();
                baseElection.setType(ElectionType.LEGISLATIVE);
                baseElection.setName(dto.getName());
                baseElection.setDescription(dto.getDescription());
                baseElection.setStartDate(startDate);
                baseElection.setEndDate(endDate);
                baseElection = electionRepository.save(baseElection);

                Legislative legislative = new Legislative();
                legislative.setDateTime(startDate);
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
                    partiesAndCandidatesService.populatePartiesAndCandidatesFromJSON(circle);
                    circles.add(circle);
                }

                legislative.setElectoralCircles(circles);
                legislativeRepository.save(legislative);

                ElectionDTO legislativeResult = new ElectionDTO();
                legislativeResult.setName(baseElection.getName());
                legislativeResult.setDescription(baseElection.getDescription());
                legislativeResult.setStartDate(startDate.toString());
                legislativeResult.setEndDate(endDate.toString());
                legislativeResult.setElectionType(ElectionType.LEGISLATIVE);
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

        Organisation organisation = organisationRepository.findById(voteRequest.getOrganisationId())
                .orElseThrow(() -> new RuntimeException("Organização não encontrada"));

        Vote vote = new Vote();
        vote.setOrganisation(organisation);
        vote.setMunicipality(municipality);
        vote.setParish(parish);

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

    @Override
    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    @Override
    public List<ElectionDTO> getActiveElections() {
        List<Election> elections = electionRepository.findAll();
        List<Election> activeElection = new ArrayList<>();
        for (Election election : elections) {
            if (election.isStarted()) {
                activeElection.add(election);
            }
        }

        return activeElection.stream().map(e -> {
            ElectionDTO dto = new ElectionDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setDescription(e.getDescription());
            dto.setStartDate(e.getStartDate() != null ? e.getStartDate().toString() : null);
            dto.setEndDate(e.getEndDate() != null ? e.getEndDate().toString() : null);
            dto.setElectionType(e.getType());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Election> getNotActiveElections() {
        List<Election> elections = electionRepository.findAll();
        List<Election> notActiveElections = new ArrayList<>();
        for (Election election : elections) {
            if (!election.isStarted()) {
                notActiveElections.add(election);
            }
        }
        return notActiveElections;
    }

    @Override
    public Election startElection(Long id) {
        Election election = getElectionById(id);
        election.startElection();
        return electionRepository.save(election);
    }

    @Override
    public Election endElection(Long id) {
        Election election = getElectionById(id);
        if (!election.isStarted()) {
            throw new IllegalArgumentException("The election with the " + id + " was not found.");
        }
        election.endElection();
        return electionRepository.save(election);
    }

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
    public ElectionDTO updateElection(Long id, ElectionDTO electionDTO) {
        Election existingElection = getElectionById(id);

        if (existingElection.isStarted()) {
            throw new IllegalStateException("Cannot edit an election that has already started.");
        }

        validateElectionDTO(electionDTO);

        LocalDateTime startDate = parseDateTimeFlexible(electionDTO.getStartDate());
        LocalDateTime endDate = parseDateTimeFlexible(electionDTO.getEndDate());

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        existingElection.setName(electionDTO.getName().trim());
        existingElection.setDescription(electionDTO.getDescription() != null ? electionDTO.getDescription().trim() : null);
        existingElection.setStartDate(startDate);
        existingElection.setEndDate(endDate);

        Election updatedElection = electionRepository.save(existingElection);

        ElectionDTO resultDTO = new ElectionDTO();
        resultDTO.setId(updatedElection.getId());
        resultDTO.setName(updatedElection.getName());
        resultDTO.setDescription(updatedElection.getDescription());
        resultDTO.setStartDate(updatedElection.getStartDate().toString());
        resultDTO.setEndDate(updatedElection.getEndDate().toString());
        resultDTO.setElectionType(updatedElection.getType());

        return resultDTO;
    }

    public void deleteElection(Long id) {
        Election election = getElectionById(id);

        if (election.isStarted()) {
            throw new IllegalStateException("Cannot delete an election that has already started.");
        }

        if (election.getVotes() != null && !election.getVotes().isEmpty()) {
            throw new IllegalStateException("Cannot delete an election that already has votes.");
        }

        try {
            if (election instanceof ElectoralCircle) {
                deleteElectoralCircle((ElectoralCircle) election);
            } else if (election instanceof Presidential) {
                deletePresidentialElection((Presidential) election);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting election: " + e.getMessage(), e);
        }
    }

    private void deletePresidentialElection(Presidential presidential) {
        if (presidential.getCandidates() != null) {
            presidential.getCandidates().clear();
        }
        if (presidential.getOrganisations() != null) {
            presidential.getOrganisations().clear();
        }
        if (presidential.getVotersVoted() != null) {
            presidential.getVotersVoted().clear();
        }

        electionRepository.delete(presidential);
    }

    private void deleteElectoralCircle(ElectoralCircle circle) {
        if (circle.getParties() != null) {
            circle.getParties().clear();
        }
        if (circle.getMunicipalities() != null) {
            circle.getMunicipalities().clear();
        }
        if (circle.getParish() != null) {
            circle.getParish().clear();
        }
        if (circle.getOrganisations() != null) {
            circle.getOrganisations().clear();
        }
        if (circle.getVotersVoted() != null) {
            circle.getVotersVoted().clear();
        }

        Legislative legislative = circle.getLegislative();
        if (legislative != null) {
            legislative.getElectoralCircles().remove(circle);
            legislativeRepository.save(legislative);
        }

        electionRepository.delete(circle);
    }

    private void validateElectionDTO(ElectionDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Election name is required.");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
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
                    throw new IllegalArgumentException("Formato de data inválido: " + dateStr);
                }
            }
        }
    }
}
