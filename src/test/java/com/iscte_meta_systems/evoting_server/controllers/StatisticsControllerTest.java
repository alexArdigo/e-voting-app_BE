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
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetDistrictStatistics() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/statistics/districts/Lisboa/statistics"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetDistrictStatistics: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetTotalVotesByElection() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/statistics/total-votes-by-election?electionId=1"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetTotalVotesByElection: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetTotalVotesByParty() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/statistics/total-votes-by-party?electionId=1&partyName=IniciativaLiberal"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetTotalVotesByParty: " + result.getResponse().getContentAsString());
    }
}

