package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.*;
import com.iscte_meta_systems.evoting_server.enums.ElectionType;
import com.iscte_meta_systems.evoting_server.enums.ElectoralCircleType;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import com.iscte_meta_systems.evoting_server.model.VoteRequestModel;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
import com.iscte_meta_systems.evoting_server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ParishRepository parishRepository;

    @Autowired
    private MunicipalityRepository municipalityRepository;

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

                    if (e.getOrganisations() != null) {
                        List<OrganisationDTO> orgDtos = e.getOrganisations().stream()
                                .map(org -> {
                                    OrganisationDTO orgDto = new OrganisationDTO();
                                    orgDto.setName(org.getOrganisationName());
                                    orgDto.setType(orgDto.getType());
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
        if (dto.getElectionType() == null) {
            throw new IllegalArgumentException("Election type is required.");
        }
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
                break;
            case LEGISLATIVE:
                List<String> districts = List.of(
                    "Viana do Castelo", "Braga", "Vila Real", "Bragança", "Porto", "Aveiro", "Viseu", "Guarda",
                    "Coimbra", "Leiria", "Castelo Branco", "Santarém", "Lisboa", "Portalegre", "Évora", "Setúbal",
                    "Beja", "Faro", "Madeira", "Açores", "Europa", "Fora da Europa"
                );
                int[] seatsDistricts = {6,19,5,3,40,16,8,3,9,10,4,9,48,2,3,18,3,9,6,5,2,2};
                List<ElectoralCircle> circles = new ArrayList<>();
                for (int i = 0; i < districts.size(); i++) {
                    ElectoralCircle circle = new ElectoralCircle();
                    circle.setName(districts.get(i));
                    circle.setSeats(seatsDistricts[i]);
                    circle.setElectoralCircleType(ElectoralCircleType.NATIONAL);
                    District district = districtRepository.findByDistrictName(districts.get(i));
                    if (district != null) {
                        circle.setDistricts(district);
                    }
                    circle.setName(districts.get(i));
                    circle.setStartDate(startDate);
                    circle.setEndDate(endDate);
                    circle.setName(dto.getName() + " - " + districts.get(i));
                    circle.setDescription(dto.getDescription());
                    electionRepository.save(circle);
                    circles.add(circle);
                }

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
    public Vote castVote(Long electionId, VoteRequestModel voteRequest) {
        Election election = getElectionById(electionId);
        VoterDTO voterDTO = voterService.getInfo();
        String voterId = voterDTO.getNif().toString();

        if (election.getVotersVoted() != null) {
            boolean alreadyVoted = election.getVotersVoted().stream()
                    .anyMatch(hash -> passwordEncoder.matches(voterId, String.valueOf(hash)));
            if (alreadyVoted) {
                throw new IllegalStateException("Voter has already voted in this election.");
            }
        }

        Parish parish = parishRepository.findByParishName(voterDTO.getParish());
        Organisation organisation = organisationRepository.findOrganisationById(voteRequest.getOrganisationId());
        Municipality municipality = voterDTO.getMunicipality() != null ?
                municipalityRepository.findByMunicipalityName(voterDTO.getMunicipality()) : null;

        Vote vote = new Vote();
        vote.setOrganisation(organisation);
        vote.setMunicipality(municipality);
        vote.setParish(parish);

        election.addVote(vote);
        election.addVoted(passwordEncoder.encode(voterId));
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
        for(Election election : elections) {
            if (!election.isStarted()){
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
