package com.iscte_meta_systems.evoting_server.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResultsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPresidentialResults() throws Exception {
        MvcResult result = mockMvc.perform(get("/elections/1/results/presidential"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetPresidentialResults: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetLegislativeResults() throws Exception {
        MvcResult result = mockMvc.perform(get("/electoralcircles/1/results"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetLegislativeResults: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetAllLegislativeResults() throws Exception {
        MvcResult result = mockMvc.perform(get("/Elections/1/results/legislative"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetAllLegislativeResults: " + result.getResponse().getContentAsString());
    }
}

