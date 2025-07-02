package com.iscte_meta_systems.evoting_server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.model.OrganisationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Criado através de IA

@SpringBootTest
@AutoConfigureMockMvc
public class OrganisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetOrganisations() throws Exception {
        MvcResult result = mockMvc.perform(get("/organisations"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetOrganisations: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetOrganisationById() throws Exception {
        MvcResult result = mockMvc.perform(get("/organisations/1"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetOrganisationById: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetAllParties() throws Exception {
        MvcResult result = mockMvc.perform(get("/parties"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetAllParties: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetAllUniParties() throws Exception {
        MvcResult result = mockMvc.perform(get("/uniparties"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetAllUniParties: " + result.getResponse().getContentAsString());
    }

    @Test
    void testCreateOrganisation() throws Exception {
        OrganisationDTO dto = new OrganisationDTO();
        dto.setName("Organização Teste");
        // Preencha outros campos obrigatórios se necessário
        String json = objectMapper.writeValueAsString(dto);
        MvcResult result = mockMvc.perform(post("/organisations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testCreateOrganisation: " + result.getResponse().getContentAsString());
    }
}

