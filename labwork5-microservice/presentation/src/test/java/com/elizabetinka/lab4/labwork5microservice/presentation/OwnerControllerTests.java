package com.elizabetinka.lab4.labwork5microservice.presentation;

import com.elizabetinka.lab4.dto.*;
import com.elizabetinka.lab4.labwork5microservice.presentation.controllers.OwnerController;
import com.elizabetinka.lab4.labwork5microservice.services.services.OwnerService;
import com.elizabetinka.lab4.labwork5microservice.services.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = OwnerController.class)
@ContextConfiguration(classes = OwnerController.class)
@Import({SecurityConfig.class})
class OwnerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @MockBean
    private UserService userService;



    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");



    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/owner/add/1/1",
    })
    @WithAnonymousUser
    void NoAnthUserCantAddOwnerInformation(String str) throws Exception {
        OwnerDto owner =new OwnerDto(null,null,null,null);
        mockMvc.perform(postJson(str, owner).with(csrf())).andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/owner/add/1/1",
    })
    @WithMockUser
    void NoAdminCantAddOwnerInformation(String str) throws Exception {
        OwnerDto owner =new OwnerDto(null,null,null,null);
        mockMvc.perform(postJson(str, owner).with(csrf())).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/owner/add/1/1",
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCantAddOwnerInformation(String str) throws Exception {
        OwnerDto owner =new OwnerDto(null,null,null,null);
        mockMvc.perform(postJson(str, owner).with(csrf())).andExpect(status().isOk());
    }



    public static MockHttpServletRequestBuilder postJson(String uri, Object body) {
        try {
            String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(body);
            return post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static MockHttpServletRequestBuilder getJson(String uri, Object body) {
        try {
            String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(body);
            return get(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static MockHttpServletRequestBuilder updateJson(String uri, Object body) {
        try {
            String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(body);
            return put(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
