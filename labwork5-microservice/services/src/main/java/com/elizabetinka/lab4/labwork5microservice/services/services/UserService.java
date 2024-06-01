package com.elizabetinka.lab4.labwork5microservice.services.services;

import com.elizabetinka.lab4.dto.OwnerDto;
import com.elizabetinka.lab4.dto.UserDto;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserService {

    private static final String EXCHANGE_NAME = "OwnerExchange";
    private AmqpTemplate amqpTemplate;
    ConnectionFactory connectionFactory;

    private volatile boolean waited = false;
    private volatile Object object = null;


    public void notify(Object object) {
        waited=true;
        this.object=object;

    }

    @Autowired
    public UserService(ConnectionFactory connectionFactory, AmqpTemplate template) {
        this.connectionFactory = connectionFactory;
        this.amqpTemplate=template;
    }

    public Long GetOwnerIdByUsername(String username)  {
        System.out.println("GetOwnerIdByUsername ");
        try {
            return GetUserByUsername(username).getOwner_id();
        }
        catch (Exception e) {
            throw  new UsernameNotFoundException(username + " not found");
        }
    }

    public UserDto GetUserByUsername(String username){
        System.out.println("GetUserByUsername ");
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getUserByUsername",username);
        System.out.println("send ");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((UserDto)  ob);
            return (UserDto) ob;
        }
        /*
        try (Connection connection = connectionFactory.createConnection();
                 Channel channel = connection.createChannel(false)) {
            String routing_key = "getUserByUsername";
            channel.basicQos(10);
            //channel.queueDeclare(routing_key, false, false, false, null);
            //channel.queueBind(routing_key, EXCHANGE_NAME, routing_key);
            channel.basicPublish(EXCHANGE_NAME, routing_key , null,username.getBytes(StandardCharsets.UTF_8));
            System.out.println("I Send to "+routing_key);
            UserDto ans=null;
            while (!waited) {
                Thread.onSpinWait();
                System.out.println("wait ");
            }
            if (object!=null){
                ans=(UserDto)object;
            }

         */

/*
                String routing_answer_key = "getUserByUsernameAnswer";
                //channel.queueDeclarePassive(routing_answer_key);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(routing_answer_key, true, consumer);

            try {
                while (true) {
                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                    if (delivery == null) {
                        Thread.sleep(100); // сообщений нет - ждем
                    } else {
                        try {
                            System.out.println("I GEEEET ANSWER1 "+routing_answer_key);
                            if (delivery.getBody().length!=0){

                                Object object =    SerializationUtils.deserialize(delivery.getBody());
                                //System.out.println(" [x] Received '" + object + "'");
                                ans= (UserDto) object;

                            }// бизнес лоигка
                            //channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); // подтверждение приема
                            break;
                        } catch (Exception e) {
                            channel.basicReject(delivery.getEnvelope().getDeliveryTag(), true); // возвращение в очередь - на усмотрение
                        } // обработка
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace(); // цикл прервется если соединение разорвется - это на усмотрение уже
            }


            channel.close();
            connection.close();
            return ans;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
*/

        }




}
