package com.elizabetinka.lab4.labwork5microservice.presentation;

import com.elizabetinka.lab4.labwork5microservice.presentation.controllers.CatController;
import com.elizabetinka.lab4.labwork5microservice.services.RabbitConfiguration;
import com.elizabetinka.lab4.labwork5microservice.services.services.OwnerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.Base64;


//@ContextConfiguration(classes = CatController.class)
@Import({ RabbitConfiguration.class})
@SpringBootTest
public class CatMicroserviceTests {


    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Test
    void UserGetInformation() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/v1/cat/get"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
    }


    @Test
    void UserGetInformation2() throws Exception {
        try {
            URL url = new URL ("http://localhost:8080/api/v1/cat/get");
            String encoding = Base64.getEncoder().encodeToString(("admin:admin").getBytes("UTF-8"));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty  ("Authorization", "Basic " + encoding);
            InputStream content = (InputStream)connection.getInputStream();
            BufferedReader in   =
                    new BufferedReader (new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
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
