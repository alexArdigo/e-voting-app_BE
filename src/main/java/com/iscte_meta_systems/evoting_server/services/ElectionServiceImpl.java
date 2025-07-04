package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import com.iscte_meta_systems.evoting_server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public List<ElectionDTO> getElections(String electionType, Integer electionYear) {
        List<Election> elections = electionRepository.findAll();

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

        String startDateStr = dto.getStartDate().contains("T") ? dto.getStartDate() : dto.getStartDate() + "T00:00:00";
        String endDateStr = dto.getEndDate().contains("T") ? dto.getEndDate() : dto.getEndDate() + "T00:00:00";
        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr);

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        Election election;
        switch (dto.getElectionType()) {
            case PRESIDENTIAL:
                election = new Presidential();
                election.setType(ElectionType.PRESIDENTIAL);
                break;
            case LEGISLATIVE:
                List<String> distritos = List.of(
                        "Viana do Castelo", "Braga", "Vila Real", "Bragança", "Porto", "Aveiro", "Viseu", "Guarda",
                        "Coimbra", "Leiria", "Castelo Branco", "Santarém", "Lisboa", "Portalegre", "Évora", "Setúbal",
                        "Beja", "Faro", "Madeira", "Açores", "Europa", "Fora da Europa"
                );
                int[] seatsDistritos = {6,19,5,3,40,16,8,3,9,10,4,9,48,2,3,18,3,9,6,5,2,2};
                List<ElectoralCircle> circles = new ArrayList<>();
                for (int i = 0; i < distritos.size(); i++) {
                    ElectoralCircle circle = new ElectoralCircle();
                    circle.setName(distritos.get(i));
                    circle.setSeats(seatsDistritos[i]);
                    circle.setElectoralCircleType(ElectoralCircleType.NATIONAL);
                    District district = districtRepository.findByDistrictName(distritos.get(i));
                    if (district != null) {
                        circle.setDistricts(district);
                    }
                    circle.setName(dto.getName() + " - " + distritos.get(i));
                    circle.setStartDate(startDate);
                    circle.setEndDate(endDate);
                    circle.setDescription(dto.getDescription());
                    electionRepository.save(circle);
                    circles.add(circle);
                }
                Legislative legislative = new Legislative();
                legislative.setElectoralCircles(circles);

                ElectionDTO resultDto = new ElectionDTO();
                return resultDto;
            default:
                throw new IllegalArgumentException("Unknown election type: " + dto.getElectionType());
        }

        election.setName(dto.getName());
        election.setDescription(dto.getDescription());
        election.setStartDate(startDate);
        election.setEndDate(endDate);

        electionRepository.save(election);
        return dto;
    }

    @Override
    public List<Organisation> getBallotByElectionId(Long id) {
        Election election = getElectionById(id);
        return election.getOrganisations();
    }

    @Override
    public Vote castVote(Long electionId, Vote voteRequest) {
        Election election = getElectionById(electionId);
        Voter voter = voterService.getLoggedVoter();

        String hash = voterService.getHashIdentification(voter.getNif());

        VoterHash voterHash = voterHashRepository.getVoterHashByHashIdentification(hash);

        if(election.getVotersVoted() != null && election.getVotersVoted().contains(voterHash)) {
            throw new IllegalStateException("Voter has already voted in this election.");
        }

        if (!election.isStarted()) {
            throw new IllegalStateException("Election has not started.");
        }

        Parish parish = voter.getParish();
        Organisation organisation = organisationRepository.getReferenceById(voteRequest.getOrganisation().getId());
        Municipality municipality = voterHash.getMunicipality();

        Vote vote = new Vote();
        vote.setOrganisation(organisation);
        vote.setMunicipality(municipality);
        vote.setParish(parish);

        election.addVote(vote);
        election.addVoted(voterHash.getHashIdentification());
        electionRepository.save(election);
        return voteRepository.save(vote);
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
}
