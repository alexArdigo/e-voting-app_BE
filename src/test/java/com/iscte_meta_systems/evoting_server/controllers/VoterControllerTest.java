package com.iscte_meta_systems.evoting_server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.model.VoterDTO;
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
public class VoterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveVoterAuthenticated() throws Exception {
        VoterDTO voterDTO = new VoterDTO();
        // Preencha outros campos obrigatórios se necessário
        String json = objectMapper.writeValueAsString(voterDTO);
        MvcResult result = mockMvc.perform(post("/voters/authenticated")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testSaveVoterAuthenticated: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetInfo() throws Exception {
        MvcResult result = mockMvc.perform(get("/voters/info"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetInfo: " + result.getResponse().getContentAsString());
    }

    @Test
    void testHasAlreadyVoted() throws Exception {
        String voter = "votante@test.com";
        Long electionId = 1L;
        MvcResult result = mockMvc.perform(get("/voters/has-voted")
                        .param("voter", voter)
                        .param("electionId", electionId.toString()))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testHasAlreadyVoted: " + result.getResponse().getContentAsString());
    }
}
