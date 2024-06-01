package com.elizabetinka.lab4.catmicroservice2.listeners;

import com.elizabetinka.lab4.catmicroservice2.CatService;
import com.elizabetinka.lab4.dto.CatDto;
import com.elizabetinka.lab4.dto.Color;
import com.elizabetinka.lab4.dto.OwnerDto;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@EnableRabbit //нужно для активации обработки аннотаций @RabbitListener
@Component
public class ControllerListener {


    CatService catService;

    ConnectionFactory connectionFactory;


    @Autowired
    public ControllerListener(CatService catService, ConnectionFactory connectionFactory) {
        this.catService = catService;
        this.connectionFactory = connectionFactory;
    }


    @RabbitListener(queues = "getAllCats", containerFactory = "customListenerContainerFactory")
    public List<CatDto> getAllCats(String message) {
        System.out.println("Received from queue : " + message);
        List<CatDto> catDtos = catService.getAllCats();
        return catDtos;
    }


    @RabbitListener(queues = "updateCat", containerFactory = "customListenerContainerFactory")
    public void updateCat(CatDto id) throws Exception {
        catService.updateCat(id);
    }

    @RabbitListener(queues = "deleteCatById", containerFactory = "customListenerContainerFactory")
    public boolean deleteCatById(Long id) throws Exception {
        System.out.println("Received from queue deleteCatById: " + id);
        boolean catDtos = catService.deleteById(id);
        return catDtos;

    }

    @RabbitListener(queues = "deleteAllCat", containerFactory = "customListenerContainerFactory")
    public boolean deleteAllCat(String message) {
        System.out.println("Received from queue 1: " + message);
        boolean catDtos = catService.deleteAll();
        return catDtos;

    }


    @RabbitListener(queues = "addCat", containerFactory = "customListenerContainerFactory")
    public CatDto addCat(CatDto id) throws Exception {
        System.out.println("Received from addCat: " + id);
        CatDto catDtos = catService.addCat(id);
        return catDtos;
    }


    @RabbitListener(queues = "getCat", containerFactory = "customListenerContainerFactory")
    public CatDto getCat(Long id) throws Exception {
        System.out.println("Received from queue getCat: " + id);
        CatDto catDtos = catService.getCat(id);
        return catDtos;
    }

    @RabbitListener(queues = "getAllCatsByName", containerFactory = "customListenerContainerFactory")
    public List<CatDto> getAllCatsByName(String message) {
        System.out.println("Received from queue 1: " + message);
        List<CatDto> ans = catService.getAllByName(message);
        return ans;
    }


    @RabbitListener(queues = "getAllCatsByBreed", containerFactory = "customListenerContainerFactory")
    public List<CatDto> getAllCatsByBreed(String message) {
        System.out.println("Received from queue 1: " + message);
        List<CatDto> ans = catService.getAllByBreed(message);
        return ans;
    }

    @RabbitListener(queues = "getAllCatsByOwnerId", containerFactory = "customListenerContainerFactory")
    public List<CatDto> getAllCatsByOwnerId(Long id) {
        List<CatDto> ans = catService.getAllByOwnerId(id);
        return ans;
    }


    @RabbitListener(queues = "getAllCatsByBirthday", containerFactory = "customListenerContainerFactory")
    public List<CatDto> getAllCatsByBirthday(LocalDate id) {
        List<CatDto> ans = catService.getAllByBirthday(id);
        return ans;
    }

    @RabbitListener(queues = "getAllCatsByColor", containerFactory = "customListenerContainerFactory")
    public List<CatDto> getAllCatsByColor(Color id) {
        List<CatDto> ans = catService.getAllByColor(com.elizabetinka.lab4.jpa.Color.valueOf(id.toString()));
        return ans;
    }

    }

