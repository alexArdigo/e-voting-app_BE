package com.iscte_meta_systems.evoting_server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.model.CandidateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCandidatesByElection() throws Exception {
        MvcResult result = mockMvc.perform(get("/elections/1/candidates"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetCandidatesByElection: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetCandidateById() throws Exception {
        MvcResult result = mockMvc.perform(get("/candidate/1"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetCandidateById: " + result.getResponse().getContentAsString());
    }

    @Test
    void testAddCandidate() throws Exception {
        CandidateDTO candidate = new CandidateDTO();
        candidate.setName("Candidato Teste");
//        candidate.setPartyName("Partido Teste");
//        candidate.setElectionId(1L); // Ajuste conforme necessário
        String json = objectMapper.writeValueAsString(candidate);
        MvcResult result = mockMvc.perform(post("/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testAddCandidate: " + result.getResponse().getContentAsString());
    }

    @Test
    void testAddCandidateAndGetByElection() throws Exception {

        CandidateDTO candidate = new CandidateDTO();
        candidate.setName("Candidato Teste");
        String json = objectMapper.writeValueAsString(candidate);
        MvcResult addResult = mockMvc.perform(post("/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testAddCandidate: " + addResult.getResponse().getContentAsString());

        // Verifica se o candidato aparece na lista de candidatos da eleição 1
        MvcResult getResult = mockMvc.perform(get("/elections/1/candidates"))
                .andExpect(status().isOk())
                .andReturn();
        String response = getResult.getResponse().getContentAsString();
        System.out.println("testGetCandidatesByElection: " + response);
        assert response.contains("Candidato Teste");
    }
}
