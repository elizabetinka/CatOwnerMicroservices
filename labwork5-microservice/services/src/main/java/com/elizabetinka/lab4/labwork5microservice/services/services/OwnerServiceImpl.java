package com.elizabetinka.lab4.labwork5microservice.services.services;


import com.elizabetinka.lab4.dto.ForAddUserDto;
import com.elizabetinka.lab4.dto.UserDto;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import lombok.NonNull;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elizabetinka.lab4.dto.OwnerDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OwnerServiceImpl  implements OwnerService {

    private final PasswordEncoder passwordEncoder;
    private AmqpTemplate amqpTemplate;
    private static final String EXCHANGE_NAME = "OwnerExchange";
    ConnectionFactory connectionFactory;

    private volatile boolean waited = false;
    private volatile Object object = null;

    @Override
    public void notify(Object object) {
        System.out.println("notify");
        waited = true;
        this.object = object;
    }

    @Autowired
    public OwnerServiceImpl(PasswordEncoder passwordEncoder, ConnectionFactory connectionFactory, AmqpTemplate template) {
        this.connectionFactory = connectionFactory;
        this.amqpTemplate = template;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void updateOwner(OwnerDto owner) throws Exception {
        amqpTemplate.convertAndSend(EXCHANGE_NAME,"deleteOwnerById",owner);
        // sendfunc(owner, "updateOwner");
    }

    @Override
    public boolean deleteById(@NonNull Long id) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"deleteOwnerById",id);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return false;
        }
        else{
            System.out.println("get ");
            System.out.println((boolean) ob);
            return (boolean)ob;
        }
    }

    @Override
    public OwnerDto getOwner(@NonNull Long id) {
        System.out.println("getOwner ");
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getOwnerById",id);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((OwnerDto) ob);
            return (OwnerDto)ob;
        }
    }


    @Override
    public List<OwnerDto> getAllOwners() {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getOwner","getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((List<OwnerDto>) ob);
            return (List<OwnerDto>)ob;
        }
    }

    @Override
    public OwnerDto addOwner(OwnerDto ownerDto) {
        System.out.println("addOwner ");
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"addOwner","addOwner");
        System.out.println("send ");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((OwnerDto) ob);
            return (OwnerDto)ob;
        }
    }

    @Override
    public boolean deleteAll() {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"deleteOwner","deleteOwner");
        System.out.println("send ");
        if (ob == null) {
            System.out.println("wait ");
            return false;
        }
        else{
            System.out.println("get ");
            System.out.println((boolean) ob);
            return (boolean)ob;
        }
    }

    @Override
    public List<OwnerDto> getAllByName(String name) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getOwnerByName",name);
        System.out.println("send ");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((List<OwnerDto>) ob);
            return (List<OwnerDto>)ob;
        }
    }

    @Override
    public List<OwnerDto> getAllByBirthday(LocalDate birthday) {
        System.out.println("GetUserByUsername ");
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getOwnerByBirthday",birthday);
        System.out.println("send ");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((List<OwnerDto>) ob);
            return (List<OwnerDto>)ob;
        }
    }

    @Override
    public OwnerDto addUser(OwnerDto ownerDto, String login, String password, String role) throws InterruptedException, ExecutionException {
        //passwordEncoder.encode(password)
        ForAddUserDto forAddUserDto = new ForAddUserDto(ownerDto, login, passwordEncoder.encode(password), role);
        System.out.println("send: "+ forAddUserDto);
       Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"addUser",forAddUserDto);
        System.out.println("send ");
       if (ob == null) {
           System.out.println("wait ");
           return null;
       }
       else{
           System.out.println("get ");
           System.out.println((OwnerDto) ob);
           return (OwnerDto)ob;
       }






/*
        sendfunc(forAddUserDto, "addUser");
        OwnerDto ans = null;
        CountDownLatch latch = new CountDownLatch(1);

        CompletableFuture<Void> asyncOp = CompletableFuture.runAsync(
                () -> {while (!waited) {

            //Thread.onSpinWait();
            System.out.println("wait ");}
        }
        );
        asyncOp.get();

        /*
        Thread thread = new Thread(() -> {
            // Выполняется некая задача
            while (!waited) {

                //Thread.onSpinWait();
                System.out.println("wait ");
            }
            latch.countDown();
        });
        thread.start();
        latch.await();


        if (object != null) {
            ans = (OwnerDto) object;
        }
        return ans;
        */
    }
}

  /*
        CompletableFuture.runAsync(() -> {
            while (!waited) {
                Thread.onSpinWait();
                //System.out.println("wait ");
            }
        });
        if (object!=null){
            ans = (OwnerDto) object;
        }
    ;
        return object;


        //return getfunc("addUserAnswer");
    }

}


<T2> T2 getfunc(String queueName) {

        try (Connection connection = connectionFactory.createConnection();
             Channel channel = connection.createChannel(false)) {
            channel.basicQos(100);
            String routing_answer_key = queueName;
            //channel.queueDeclarePassive(routing_answer_key);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(routing_answer_key, true, consumer);
            T2 ans = null;
            try {
                while (true) {
                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                    if (delivery == null) {
                        Thread.sleep(100); // сообщений нет - ждем
                    } else {
                        try {
                            System.out.println("I GEEEET ANSWER1 " + routing_answer_key);
                            if (delivery.getBody().length != 0) {

                                Object object = SerializationUtils.deserialize(delivery.getBody());
                                //System.out.println(" [x] Received '" + object + "'");
                                ans = (T2) object;

                            }// бизнес лоигка
                            //channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); // подтверждение приема
                            break;
                        } catch (Exception e) {
                            System.out.println("Exception  ");
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
            /*
            QueueingConsumer.Delivery delivery = consumer.nextDelivery(1000);
            T2 ans=null;
            while (delivery != null) {
                try {
                    if (delivery.getBody().length!=0){

                        Object object =    SerializationUtils.deserialize(delivery.getBody());
                        //System.out.println(" [x] Received '" + object + "'");
                        System.out.println("I GEEEET ANSWER1");
                        ans= (T2) object;
                    }
                    String message = new String(delivery.getBody());
                    String routingKey = delivery.getEnvelope().getRoutingKey();

                    //Processing logic goes here
                } catch(Exception e) {
                    e.printStackTrace();
                }
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
                delivery = consumer.nextDelivery();
            }
            channel.close();
            connection.close();
            return ans;

             */
            /*
            while (true) {
                // Use a timeout of 1000 milliseconds
                QueueingConsumer.Delivery delivery = consumer.nextDelivery(1000);

                // Test if delivery is null, meaning the timeout was reached.
                if (delivery != null) {
                    if (delivery.getBody().length!=0){

                        Object object =    SerializationUtils.deserialize(delivery.getBody());
                        //System.out.println(" [x] Received '" + object + "'");
                        System.out.println("I GEEEET ANSWER1");
                        return (T2)object;
                    }
                    System.out.println("I GEEEET ANSWER2");
                    break;
                }
                else {
                    break;
                }
            }
            return null;

             */
/*
            AtomicBoolean flag= new AtomicBoolean(false);

            AtomicReference<T2> obj= new AtomicReference<>();
            flag2=false;


            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                if (delivery.getBody().length!=0){

                    Object object =    SerializationUtils.deserialize(delivery.getBody());
                    //System.out.println(" [x] Received '" + object + "'");
                    obj.set((T2) object);
                    flag2=true;
                    System.out.println("I GEEEET ANSWER1");

                }
                flag.set(true);
                System.out.println("I GEEEET ANSWER2");
            };
            channel.basicConsume(routing_answer_key, true, deliverCallback, consumerTag -> { });

            //System.out.println("consume");
            int k=0;
            while (!flag2) {
                k+=1;
                if (k==1){
                    System.out.println("consume");
                }
            }

            //channel.close();
            //System.out.println(" [x] Received '" + obj.get() + "'");

            return obj.get();

        } catch (Exception e) {
        System.out.println(e.getMessage());
        throw new RuntimeException(e);
        }
                }

<T1> void sendfunc(T1 ob, String queueName) {
    try (Connection connection = connectionFactory.createConnection();
         Channel channel = connection.createChannel(false)) {
        String routing_key = queueName;
        channel.basicQos(100);
        //channel.queueDeclare(routing_key, false, false, false, null);
        //channel.queueBind(routing_key, EXCHANGE_NAME, routing_key);
        byte[] data = SerializationUtils.serialize(ob);
        System.out.println(Arrays.toString(data));
        channel.basicPublish(EXCHANGE_NAME, routing_key, null, data);
        System.out.println("I SEND");
        channel.close();
        connection.close();
    } catch (Exception e) {
        System.out.println(e.getMessage());
        throw new RuntimeException(e);
    }
}

void sendfunc(String ob, String queueName) {
    try (Connection connection = connectionFactory.createConnection();
         Channel channel = connection.createChannel(false)) {
        String routing_key = queueName;
        channel.basicQos(100);
        //channel.queueDeclare(routing_key, false, false, false, null);
        //channel.queueBind(routing_key, EXCHANGE_NAME, routing_key);
        channel.basicPublish(EXCHANGE_NAME, routing_key, null, ob.getBytes(StandardCharsets.UTF_8));
        channel.close();
        connection.close();
    } catch (Exception e) {
        System.out.println(e.getMessage());
        throw new RuntimeException(e);
    }
}
*/