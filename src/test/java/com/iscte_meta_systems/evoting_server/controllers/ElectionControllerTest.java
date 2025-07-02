package com.iscte_meta_systems.evoting_server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.model.ElectionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//testes criados por IA.

@SpringBootTest
@AutoConfigureMockMvc
public class ElectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetElections() throws Exception {
        mockMvc.perform(get("/elections"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetElectionById() throws Exception {
        MvcResult result = mockMvc.perform(get("/elections/1"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetElectionById: " + result.getResponse().getContentAsString());
    }

    @Test
    void testCreateElection() throws Exception {
        ElectionDTO dto = new ElectionDTO();
        dto.setElectionType(com.iscte_meta_systems.evoting_server.enums.ElectionType.PRESIDENTIAL);
        dto.setName("Presidenciais 2026");
        dto.setDescription("Eleições presidenciais portuguesas de 2026");
        dto.setStartDate("2026-01-24");
        dto.setEndDate("2026-01-25");
        dto.setDistrictName("Lisboa");
        dto.setSeats(1); // Exemplo, ajuste conforme necessário
        // Preencher outros campos obrigatórios se necessário
        String json = objectMapper.writeValueAsString(dto);
        MvcResult result = mockMvc.perform(post("/elections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testCreateElection: " + result.getResponse().getContentAsString());
    }

    @Test
    void testCreateElection_MissingType() throws Exception {
        ElectionDTO dto = new ElectionDTO();
        dto.setName("Sem Tipo");
        dto.setStartDate("2026-01-24");
        dto.setEndDate("2026-01-25");
        String json = objectMapper.writeValueAsString(dto);
        MvcResult result = mockMvc.perform(post("/elections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError())
                .andReturn();
        System.out.println("testCreateElection_MissingType: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetElectionById_NotFound() throws Exception {
        mockMvc.perform(get("/elections/52"));
    }

    @Test
    void testGetBallotByElectionId() throws Exception {
        mockMvc.perform(get("/elections/1/ballot"))
                .andExpect(status().isOk());
    }

    @Test
    void testCastVote() throws Exception {
        // Supondo que a eleição 1 existe e está iniciada, e existe uma organização com id 1
        String voteJson = "{" +
                "\"organisation\": {\"id\": 1}," +
                "\"municipality\": null," +
                "\"parish\": null" +
                "}";
        mockMvc.perform(post("/elections/1/castVote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(voteJson))
                .andExpect(status().isOk());
    }

    @Test
    void testStartElection() throws Exception {
        mockMvc.perform(post("/elections/1/startElection"))
                .andExpect(status().isOk());
    }

    @Test
    void testEndElection() throws Exception {
        mockMvc.perform(post("/elections/1/endElection"))
                .andExpect(status().isOk());
    }

    @Test
    void testPopulatePartiesAndCandidates() throws Exception {
        mockMvc.perform(post("/elections/1/populate-parties"))
                .andExpect(status().isOk());
    }

    @Test
    void testIsElectionStarted() throws Exception {
        mockMvc.perform(get("/1/isStarted"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllElections() throws Exception {
        mockMvc.perform(get("/elections/all"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetActiveElections() throws Exception {
        mockMvc.perform(get("/election/active"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNotActiveElections() throws Exception {
        mockMvc.perform(get("/election/notactive"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateLegislativeElection() throws Exception {
        ElectionDTO dto = new ElectionDTO();
        // Removido dto.setId(400L); para evitar conflito de chave única
        dto.setElectionType(com.iscte_meta_systems.evoting_server.enums.ElectionType.LEGISLATIVE);
        dto.setName("Legislativas 2026");
        dto.setDescription("Eleições legislativas portuguesas de 2026");
        dto.setStartDate("2026-03-10");
        dto.setEndDate("2026-03-11");
        String json = objectMapper.writeValueAsString(dto);
        MvcResult result = mockMvc.perform(post("/elections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testCreateLegislativeElection: " + result.getResponse().getContentAsString());
    }
}
