package com.iscte_meta_systems.evoting_server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscte_meta_systems.evoting_server.model.UserRegisterDTO;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterAdmin() throws Exception {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("adminTest");
        dto.setPassword("admin123");
        String json = objectMapper.writeValueAsString(dto);
        MvcResult result = mockMvc.perform(post("/registerAdmin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testRegisterAdmin: " + result.getResponse().getContentAsString());
    }

    @Test
    void testRegisterViewer() throws Exception {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("viewerTest");
        dto.setPassword("viewer123");
        String json = objectMapper.writeValueAsString(dto);
        MvcResult result = mockMvc.perform(post("/registerViewer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testRegisterViewer: " + result.getResponse().getContentAsString());
    }

    @Test
    void testPendingAuthorization() throws Exception {
        MvcResult result = mockMvc.perform(get("/pendingAuthorization"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testPendingAuthorization: " + result.getResponse().getContentAsString());
    }

    @Test
    void testApproveViewer() throws Exception {
        // Supondo que existe um viewer com ID 1
        MvcResult result = mockMvc.perform(post("/approveViewer/1"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testApproveViewer: " + result.getResponse().getContentAsString());
    }

    @Test
    void testFindUserByUsername() throws Exception {
        MvcResult result = mockMvc.perform(get("/findUserByUsername?username=adminTest"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("testFindUserByUsername: " + result.getResponse().getContentAsString());
    }
}

