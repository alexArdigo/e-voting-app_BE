package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import com.iscte_meta_systems.evoting_server.model.VoteRequestModel;
import com.iscte_meta_systems.evoting_server.repositories.*;
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
                        .equals(electoralCircle.getDistrict().getDistrictName()))
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
    public ElectionDTO updateElection(Long id, ElectionDTO electionDTO) {
        Election existingElection = getElectionById(id);

        if (existingElection.isStarted()) {
            throw new IllegalStateException("Não é possível editar uma eleição que já foi iniciada.");
        }

        if (electionDTO.getName() == null || electionDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da eleição é obrigatório.");
        }

        if (electionDTO.getStartDate() == null || electionDTO.getEndDate() == null) {
            throw new IllegalArgumentException("Data de início e fim são obrigatórias.");
        }

        LocalDateTime startDate = parseDateTimeFlexible(electionDTO.getStartDate());
        LocalDateTime endDate = parseDateTimeFlexible(electionDTO.getEndDate());

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Data de fim não pode ser anterior à data de início.");
        }

        existingElection.setName(electionDTO.getName().trim());
        existingElection.setDescription(electionDTO.getDescription() != null ? electionDTO.getDescription().trim() : null);
        existingElection.setStartDate(startDate);
        existingElection.setEndDate(endDate);

        if (electionDTO.getElectionType() != null && !electionDTO.getElectionType().equals(existingElection.getType())) {
            throw new IllegalArgumentException("Não é possível alterar o tipo de eleição após a criação.");
        }

        Election updatedElection = electionRepository.save(existingElection);

        if (existingElection.getType() == ElectionType.LEGISLATIVE) {
            updateLegislativeCircles(existingElection, startDate, endDate);
        }

        ElectionDTO resultDTO = new ElectionDTO();
        resultDTO.setId(updatedElection.getId());
        resultDTO.setName(updatedElection.getName());
        resultDTO.setDescription(updatedElection.getDescription());
        resultDTO.setStartDate(updatedElection.getStartDate().toString());
        resultDTO.setEndDate(updatedElection.getEndDate().toString());
        resultDTO.setElectionType(updatedElection.getType());

        return resultDTO;
    }

    @Override
    public void deleteElection(Long id) {
        Election election = getElectionById(id);

        if (election.isStarted()) {
            throw new IllegalStateException("Não é possível eliminar uma eleição que já foi iniciada.");
        }

        if (election.getVotes() != null && !election.getVotes().isEmpty()) {
            throw new IllegalStateException("Não é possível eliminar uma eleição que já possui votos.");
        }

        try {
            if (election.getType() == ElectionType.LEGISLATIVE) {
                deleteLegislativeCircles(election);
            }

            if (election.getOrganisations() != null) {
                election.getOrganisations().clear();
            }

            if (election.getVotersVoted() != null) {
                election.getVotersVoted().clear();
            }

            electionRepository.delete(election);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao eliminar a eleição: " + e.getMessage(), e);
        }
    }

    private void updateLegislativeCircles(Election election, LocalDateTime startDate, LocalDateTime endDate) {
        if (election instanceof Presidential) {
            return;
        }

        List<ElectoralCircle> circles = electoralCircleRepository.findAll().stream()
                .filter(circle -> circle.getStartDate() != null &&
                        circle.getEndDate() != null &&
                        circle.getStartDate().equals(election.getStartDate()) &&
                        circle.getEndDate().equals(election.getEndDate()))
                .toList();

        for (ElectoralCircle circle : circles) {
            circle.setStartDate(startDate);
            circle.setEndDate(endDate);
            circle.setDescription(election.getDescription());
            electoralCircleRepository.save(circle);
        }
    }


    private void deleteLegislativeCircles(Election election) {
        if (election.getType() != ElectionType.LEGISLATIVE) {
            return;
        }

        try {
            List<Legislative> legislatives = legislativeRepository.findAll();

            for (Legislative legislative : legislatives) {
                if (legislative.getElectoralCircles() != null) {
                    List<ElectoralCircle> circlesToDelete = legislative.getElectoralCircles().stream()
                            .filter(circle -> circle.getStartDate() != null &&
                                    circle.getEndDate() != null &&
                                    circle.getStartDate().equals(election.getStartDate()) &&
                                    circle.getEndDate().equals(election.getEndDate()))
                            .toList();

                    for (ElectoralCircle circle : circlesToDelete) {
                        if (circle.getVotes() != null) {
                            voteRepository.deleteAll(circle.getVotes());
                            circle.setVotes(null);
                        }

                        if (circle.getParties() != null) {
                            circle.getParties().clear();
                        }

                        if (circle.getParishes() != null) {
                            circle.getParishes().clear();
                        }

                        if (circle.getMunicipalities() != null) {
                            circle.getMunicipalities().clear();
                        }

                        circle.setLegislative(null);
                        legislative.getElectoralCircles().remove(circle);

                        legislativeRepository.save(legislative);
                        electoralCircleRepository.save(circle);

                        electoralCircleRepository.delete(circle);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao eliminar círculos legislativos: " + e.getMessage(), e);
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
