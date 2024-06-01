package com.elizabetinka.lab4.ownermicroservice2.listeners;

import com.elizabetinka.lab4.dto.CatDto;
import com.elizabetinka.lab4.dto.ForAddUserDto;
import com.elizabetinka.lab4.dto.OwnerDto;
import com.elizabetinka.lab4.dto.UserDto;
import com.elizabetinka.lab4.ownermicroservice2.OwnerService;
import com.elizabetinka.lab4.ownermicroservice2.UserService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@EnableRabbit //нужно для активации обработки аннотаций @RabbitListener
@Component
public class ControllerListener {

    @Autowired
    OwnerService ownerService;
    @Autowired
    UserService userService;
    @Autowired
    ConnectionFactory connectionFactory;
    private static final String EXCHANGE_NAME = "OwnerAnswerExchange";




    @RabbitListener(queues = "getUserByUsername", containerFactory = "customListenerContainerFactory")
    public UserDto GetUserByUsername(String message) {
        System.out.println("Received from queue getUserByUsername: " + message);
        UserDto userDto = userService.GetUserByUsername(message);
        System.out.println("send: " + userDto);
        return userDto;
        /*

        try (Connection connection = connectionFactory.createConnection();
             Channel channel = connection.createChannel(false)) {
            String routing_key = "getUserByUsernameAnswer";
            channel.basicQos(100);
            //channel.queueDeclarePassive(routing_key);
            //channel.queueBind(routing_key, EXCHANGE_NAME, routing_key);
            byte[] data = SerializationUtils.serialize(userDto);
            System.out.println(Arrays.toString(data));
            channel.basicPublish(EXCHANGE_NAME, routing_key , null,data);
            System.out.println("send to " + routing_key);
            channel.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

         */
    }

    @RabbitListener(queues = "updateOwner", containerFactory = "customListenerContainerFactory")
    public void updateOwner(OwnerDto ownerDto) throws Exception {
        //Object object =    SerializationUtils.deserialize(body);
        //System.out.println("Received from queue 1: " + object);
        System.out.println("Received from queue updateOwner: " + ownerDto);
        //OwnerDto ownerDto = (OwnerDto) object;
        ownerService.updateOwner(ownerDto);
    }

    @RabbitListener(queues = "deleteOwnerById", containerFactory = "customListenerContainerFactory")
    public boolean deleteOwnerById(Long id) {
        System.out.println("Received from queue deleteOwnerById: " + id);
        //Long id = (Long) object;
        boolean ans = ownerService.deleteById(id);
        System.out.println("send: " + ans);
        return ans;
    }

    @RabbitListener(queues = "getOwnerById", containerFactory = "customListenerContainerFactory")
    public OwnerDto getOwnerById(Long id) {
        //Object object =    SerializationUtils.deserialize(body);
        System.out.println("Received from queue getOwnerById: " + id);
        //Long id = (Long) object;
        OwnerDto ans = ownerService.getOwner(id);
        System.out.println("send: " + ans);
        return ans;
    }

    @RabbitListener(queues = "getOwner", containerFactory = "customListenerContainerFactory")
    public List<OwnerDto> getOwners(String message) {
        System.out.println("Received from queue getOwner: " + message);
        List<OwnerDto> ans = ownerService.getAllOwners();
        System.out.println("send: " + ans);
        return ans;
    }

    @RabbitListener(queues = "addOwner", containerFactory = "customListenerContainerFactory")
    public OwnerDto addOwner(OwnerDto ownerDto) {
        System.out.println("Received from queue addOwner: " + ownerDto);
        OwnerDto ans = ownerService.addOwner(ownerDto);
        System.out.println("send: " + ans);
        return ans;
    }

    @RabbitListener(queues = "deleteOwner", containerFactory = "customListenerContainerFactory")
    public boolean deleteOwner(String message) {
        System.out.println("Received from queue deleteOwner: " + message);
        boolean ans = ownerService.deleteAll();
        System.out.println("send: " + ans);
        return ans;
    }
    @RabbitListener(queues = "getOwnerByName", containerFactory = "customListenerContainerFactory")
    public List<OwnerDto> getOwnerByName(String message) {
        System.out.println("Received from queue getOwnerByName: " + message);
        List<OwnerDto> ans = ownerService.getAllByName(message);
        System.out.println("send: " + ans);
        return ans;
    }

    @RabbitListener(queues = "getOwnerByBirthday", containerFactory = "customListenerContainerFactory")
    public List<OwnerDto> getOwnerByBirthday(LocalDate localDate) {
        System.out.println("Received from queue getOwnerByBirthday: " + localDate);
        List<OwnerDto> ans = ownerService.getAllByBirthday(localDate);
        System.out.println("send: " + ans);
        return ans;
    }

    @RabbitListener(queues = "addUser", containerFactory = "customListenerContainerFactory")
    public OwnerDto addUser( ForAddUserDto forAddUserDto) {
        System.out.println("I GEEET");
        System.out.println("ans: "+ forAddUserDto);
        OwnerDto ans = ownerService.addUser(forAddUserDto.getOwnerDto(), forAddUserDto.getLogin(), forAddUserDto.getPassword(), forAddUserDto.getRole());
        System.out.println("ans: "+ ans);
        System.out.println("I SEND");
        return ans;
    }

    }

