package com.elizabetinka.lab4.labwork5microservice.presentation;

import com.elizabetinka.lab4.labwork5microservice.services.RabbitConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SpringRabbitTest
class PresentationApplicationTests {

    @Test
    void contextLoads() throws  IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/owner/get"))
                .GET()
                .build();
        final HttpClient client = HttpClient.newHttpClient();

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals("Hello!", response.body());
    }
}
