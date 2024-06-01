package com.elizabetinka.lab4.labwork5microservice.presentation;


import com.elizabetinka.lab4.dto.CatDto;
import com.elizabetinka.lab4.dto.Color;
import com.elizabetinka.lab4.labwork5microservice.presentation.controllers.CatController;
import com.elizabetinka.lab4.labwork5microservice.presentation.controllers.ForChooseCat;
import com.elizabetinka.lab4.labwork5microservice.services.services.CatService;
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

/*

@WebMvcTest(controllers = CatController.class)
@ContextConfiguration(classes = CatController.class)
@Import(SecurityConfig.class)

 */
@WebMvcTest(controllers = CatController.class)
@ContextConfiguration(classes = CatController.class)
@Import({SecurityConfig.class})
class CatControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatService catService;


    @MockBean
    private UserService userService;



    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    @WithMockUser
    void AnthUserCanGetFilterInformation() throws Exception {
        ForChooseCat chooseCat =new ForChooseCat(null,null,null,"pink",null);

        String str  = "/api/v1/cat/getby";
        Object ob = mockMvc.perform(getJson(str, chooseCat).with(csrf())).andExpect(status().isOk());
    }
    @Test
    @WithAnonymousUser
    void NoAnthUserCantGetFilterInformation() throws Exception {

        ForChooseCat chooseCat =new ForChooseCat(null,null,null,"pink",null);

        String str  = "/api/v1/cat/getby";
        mockMvc.perform(getJson(str, chooseCat).with(csrf())).andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/cat/get",
            "/api/v1/cat/get/0",
            "/api/v1/cat/get/1",
            "/api/v1/cat/get/100",
    })
    @WithAnonymousUser
    void NoAnthUserCantGetAnyInformation(String str) throws Exception {
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/cat/delete",
            "/api/v1/cat/delete/0",
            "/api/v1/cat/delete/1",
            "/api/v1/cat/delete/100",
    })
    @WithAnonymousUser
    void NoAnthUserCantDeleteAnyInformation(String str) throws Exception {
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/cat/add",
    })
    @WithAnonymousUser
    void NoAnthUserCantAddCatInformation(String str) throws Exception {
        CatDto chooseCat =new CatDto(null,null,null,null, Color.pink,1L,null);
        mockMvc.perform(postJson(str, chooseCat).with(csrf())).andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/cat/update",
    })
    @WithAnonymousUser
    void NoAnthUserCantUpdateAnyInformation(String str) throws Exception {
        CatDto chooseCat =new CatDto(null,null,null,null, Color.pink,1L,null);
        mockMvc.perform(updateJson(str,chooseCat).with(csrf())).andExpect(status().isUnauthorized());
    }




    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/cat/delete",
    })
    @WithMockUser
    void AnthUserCanDeleteAnyInformation(String str) throws Exception {
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/cat/add",
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AnthUserCanAddCatInformation(String str) throws Exception {
        CatDto chooseCat =new CatDto(null,null,null,null, Color.pink,1L,null);
        mockMvc.perform(postJson(str, chooseCat).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/cat/update",
    })
    @WithMockUser
    void AnthUserCanUpdateAnyInformation(String str) throws Exception {
        CatDto chooseCat =new CatDto(1L,null,null,null, Color.pink,1L,null);
        mockMvc.perform(updateJson(str,chooseCat).with(csrf())).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "/api/v1/cat/get",
    })
    @WithMockUser
    void AnthUserCanGetAnyInformation(String str) throws Exception {
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isOk());
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
