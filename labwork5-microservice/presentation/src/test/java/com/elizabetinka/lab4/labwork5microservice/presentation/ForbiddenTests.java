package com.elizabetinka.lab4.labwork5microservice.presentation;


import com.elizabetinka.lab4.dto.*;
import com.elizabetinka.lab4.labwork5microservice.presentation.controllers.CatController;
import com.elizabetinka.lab4.labwork5microservice.presentation.controllers.OwnerController;
import com.elizabetinka.lab4.labwork5microservice.services.services.CatService;
import com.elizabetinka.lab4.labwork5microservice.services.services.OwnerService;
import com.elizabetinka.lab4.labwork5microservice.services.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
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
@WebMvcTest(controllers = {CatController.class, OwnerController.class})
@ContextConfiguration(classes =  {CatController.class, OwnerController.class})
@Import({SecurityConfig.class})
class ForbiddenTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatService catService;


    @MockBean
    private OwnerService ownerService;


    @MockBean
    private UserService userService;



    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCanGetInformationAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(1L);
        CatDto CatDto =new CatDto(id,null,null,null,null,1L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);

        String str  = "/api/v1/cat/get/"+id;
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCantGetInformationNotAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(1L);
        CatDto CatDto =new CatDto(id,null,null,null,null,2L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);

        String str  = "/api/v1/cat/get/"+id;
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanGetInformationNotAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(0L);
        CatDto CatDto =new CatDto(id,null,null,null,null,2L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);

        String str  = "/api/v1/cat/get/"+id;
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanGetInformationAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(0L);
        CatDto CatDto =new CatDto(id,null,null,null,null,0L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);

        String str  = "/api/v1/cat/get/"+id;
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isOk());
    }


    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCanDeleteInformationAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(1L);
        CatDto CatDto =new CatDto(id,null,null,null,null,1L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);
        Mockito.when(catService.deleteById(id)).thenReturn(true);

        String str  = "/api/v1/cat/delete/"+id;
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCantDeleteInformationNotAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(1L);
        CatDto CatDto =new CatDto(id,null,null,null,null,2L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);
        Mockito.when(catService.deleteById(id)).thenReturn(true);

        String str  = "/api/v1/cat/delete/"+id;
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanDeleteInformationNotAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(0L);
        CatDto CatDto =new CatDto(id,null,null,null,null,2L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);
        Mockito.when(catService.deleteById(id)).thenReturn(true);

        String str  = "/api/v1/cat/delete/"+id;
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanDeleteInformationAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(0L);
        CatDto CatDto =new CatDto(id,null,null,null,null,0L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);
        Mockito.when(catService.deleteById(id)).thenReturn(true);

        String str  = "/api/v1/cat/delete/"+id;
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isOk());
    }



    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCanUpdateInformationAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(1L);
        CatDto CatDto =new CatDto(id,null,null,null,null,1L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);


        String str  = "/api/v1/cat/update";
        mockMvc.perform(updateJson(str,CatDto).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCantUpdateInformationNotAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(1L);
        CatDto CatDto =new CatDto(id,null,null,null,null,2L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);

        String str  = "/api/v1/cat/update";
        mockMvc.perform(updateJson(str,CatDto).with(csrf())).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanUpdateInformationNotAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(0L);
        CatDto CatDto =new CatDto(id,null,null,null,null,2L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);

        String str  = "/api/v1/cat/update";
        mockMvc.perform(updateJson(str,CatDto).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanUpdateInformationAboutHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(0L);
        CatDto CatDto =new CatDto(id,null,null,null,null,0L,null);
        Mockito.when(catService.getCat(id)).thenReturn(CatDto);

        String str  = "/api/v1/cat/update";
        mockMvc.perform(updateJson(str,CatDto).with(csrf())).andExpect(status().isOk());
    }




    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCanAddHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(1L);
        CatDto CatDto =new CatDto(id,null,null,null,null,1L,null);

        String str  = "/api/v1/cat/add";
        mockMvc.perform(postJson(str,CatDto).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCantAddNotHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(1L);
        CatDto CatDto =new CatDto(id,null,null,null,null,2L,null);

        String str  = "/api/v1/cat/add";
        mockMvc.perform(postJson(str,CatDto).with(csrf())).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanAddNotHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(0L);
        CatDto CatDto =new CatDto(id,null,null,null,null,2L,null);

        String str  = "/api/v1/cat/add";
        mockMvc.perform(postJson(str,CatDto).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanAddHisCat(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(0L);
        CatDto CatDto =new CatDto(id,null,null,null,null,0L,null);

        String str  = "/api/v1/cat/add";
        mockMvc.perform(postJson(str,CatDto).with(csrf())).andExpect(status().isOk());
    }





    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCanGetInfoAboutHe(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(id);

        String str  = "/api/v1/owner/get/"+id;
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCantGetInfoAboutOther(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(id+1);

        String str  = "/api/v1/owner/get/"+id;
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanGetInfoAboutHe(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(id);

        String str  = "/api/v1/owner/get/"+id;
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanGetInfoAboutAther(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(id+1);

        String str  = "/api/v1/owner/get/"+id;
        mockMvc.perform(get(str).with(csrf())).andExpect(status().isOk());
    }




    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCanDeleteHe(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(id);
        Mockito.when(ownerService.deleteById(id)).thenReturn(true);

        String str  = "/api/v1/owner/delete/"+id;
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser
    void OwnerCantDeleteOther(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(id+1);
        Mockito.when(ownerService.deleteById(id)).thenReturn(true);

        String str  = "/api/v1/owner/delete/"+id;
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanDeleteHe(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(id);
        Mockito.when(ownerService.deleteById(id)).thenReturn(true);

        String str  = "/api/v1/owner/delete/"+id;
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value= {
            "0",
            "2",
            "4",
            "10",
            "11"
    })
    @WithMockUser(username="admin",roles={"ADMIN"})
    void AdminCanDeleteOther(Long id) throws Exception {
        Mockito.when(userService.GetOwnerIdByUsername(Mockito.any())).thenReturn(id+1);
        Mockito.when(ownerService.deleteById(id)).thenReturn(true);

        String str  = "/api/v1/owner/delete/"+id;
        mockMvc.perform(delete(str).with(csrf())).andExpect(status().isOk());
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
