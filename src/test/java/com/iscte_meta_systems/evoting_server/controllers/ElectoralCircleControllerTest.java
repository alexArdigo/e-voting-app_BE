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
public class ElectoralCircleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetTerritoryData() throws Exception {
        MvcResult result = mockMvc.perform(get("/territory"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetTerritoryData: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetAllDistricts() throws Exception {
        MvcResult result = mockMvc.perform(get("/districts"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetAllDistricts: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetDistrictByName() throws Exception {
        MvcResult result = mockMvc.perform(get("/districts/Lisboa"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetDistrictByName: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetAllMunicipalitiesByDistrict() throws Exception {
        MvcResult result = mockMvc.perform(get("/districts/Lisboa/municipalities"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetAllMunicipalitiesByDistrict: " + result.getResponse().getContentAsString());
    }

    @Test
    void testGetParishesByMunicipality() throws Exception {
        MvcResult result = mockMvc.perform(get("/municipalities/Lisboa/parishes"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testGetParishesByMunicipality: " + result.getResponse().getContentAsString());
    }

    @Test
    void testSearchParishes() throws Exception {
        MvcResult result = mockMvc.perform(get("/parishes/search?name=Santa"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testSearchParishes: " + result.getResponse().getContentAsString());
    }
}

