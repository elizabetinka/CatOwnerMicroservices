package com.elizabetinka.lab4.labwork5microservice.services.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.NonNull;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elizabetinka.lab4.dto.*;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CatServiceImpl implements CatService{

    private static final String EXCHANGE_NAME = "CatExchange";
    private AmqpTemplate amqpTemplate;
    ConnectionFactory connectionFactory;

    private boolean waited = false;
    private Object object = null;
    @Override
    public void notify(Object object) {
        waited=true;
        this.object=object;
    }

    @Autowired
    public CatServiceImpl(ConnectionFactory connectionFactory,AmqpTemplate template) {
        this.connectionFactory = connectionFactory;
        this.amqpTemplate = template;
    }


    @Override
    public CatDto addCat(CatDto catDto) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"addCat",catDto);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((CatDto) ob);
            return (CatDto) ob;
        }
    }

    @Override
    public CatDto getCat(@NonNull Long id) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getCat",id);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((CatDto) ob);
            return (CatDto) ob;
        }

    }

    @Override
    public List<CatDto> getAllCats() {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getAllCats","getAllCats");
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((List<CatDto>) ob);
            return (List<CatDto>) ob;
        }

    }

    @Override
    public boolean deleteById(@NonNull Long id) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"deleteCatById",id);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return false;
        }
        else{
            System.out.println("get ");
            System.out.println((boolean) ob);
            return (boolean) ob;
        }

    }

    @Override
    public boolean deleteAll() {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"deleteAllCat","deleteAllCat");
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return false;
        }
        else{
            System.out.println("get ");
            System.out.println((boolean) ob);
            return (boolean) ob;
        }

    }

    @Override
    public void updateCat(CatDto cat) throws Exception {
        amqpTemplate.convertAndSend(EXCHANGE_NAME,"updateCat",cat);
    }

    @Override
    public List<CatDto> getAllByName(String name) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getAllCatsByName",name);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((List<CatDto>) ob);
            return (List<CatDto>) ob;
        }
    }

    @Override
    public List<CatDto> getAllByBirthday(LocalDate birthday) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getAllCatsByBirthday",birthday);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((List<CatDto>) ob);
            return (List<CatDto>) ob;
        }
    }

    @Override
    public List<CatDto> getAllByBreed(String breed) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getAllCatsByBreed",breed);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((List<CatDto>) ob);
            return (List<CatDto>) ob;
        }
    }

    @Override
    public List<CatDto> getAllByColor(Color color) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getAllCatsByColor",color);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((List<CatDto>) ob);
            return (List<CatDto>) ob;
        }

    }

    @Override
    public List<CatDto> getAllByOwnerId(Long id) {
        Object ob = amqpTemplate.convertSendAndReceive(EXCHANGE_NAME,"getAllCatsByOwnerId",id);
        //sendfunc("getOwner", "getOwner");
        if (ob == null) {
            System.out.println("wait ");
            return null;
        }
        else{
            System.out.println("get ");
            System.out.println((List<CatDto>) ob);
            return (List<CatDto>) ob;
        }
    }

}


/*
     <T1,T2>  T2 Sendfunc(T1 ob,String queueName){
        try (Connection connection = connectionFactory.createConnection();
             Channel channel = connection.createChannel(false)) {
            String routing_key = queueName;
            channel.queueDeclare(routing_key, false, false, false, null);
            channel.queueBind(routing_key, EXCHANGE_NAME, routing_key);
            byte[] data = SerializationUtils.serialize(ob);
            System.out.println(Arrays.toString(data));
            channel.basicPublish(EXCHANGE_NAME, routing_key , null,data);

            String routing_answer_key = String.join(queueName,"Answer");
            channel.queueDeclare(routing_answer_key, false, false, false, null);

            AtomicBoolean flag= new AtomicBoolean(false);
            AtomicReference<T2> obj= new AtomicReference<>();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Object object =    SerializationUtils.deserialize(delivery.getBody());
                //System.out.println(" [x] Received '" + object + "'");
                obj.set((T2) object);
                flag.set(true);
            };
            channel.basicConsume(routing_answer_key, true, deliverCallback, consumerTag -> { });

            //System.out.println("consume");
            while (!flag.get()) {
            }
            channel.close();
            //System.out.println(" [x] Received '" + obj.get() + "'");
            return obj.get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
<T2>  T2 getfunc(String queueName){

    try (Connection connection = connectionFactory.createConnection();
         Channel channel = connection.createChannel(false)) {
        channel.basicQos(100);
        String routing_answer_key = queueName;
        //channel.queueDeclarePassive(routing_answer_key);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(routing_answer_key, true, consumer);
        T2 ans=null;
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
                            ans= (T2) object;

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

    <T1>  void sendfunc(T1 ob,String queueName){
        try (Connection connection = connectionFactory.createConnection();
             Channel channel = connection.createChannel(false)) {
            channel.basicQos(100);
            String routing_key = queueName;
            //channel.queueDeclare(routing_key, false, false, false, null);
            //channel.queueBind(routing_key, EXCHANGE_NAME, routing_key);
            byte[] data = SerializationUtils.serialize(ob);
            System.out.println(Arrays.toString(data));
            channel.basicPublish(EXCHANGE_NAME, routing_key , null,data);
            System.out.println("Send  "+routing_key);
            channel.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    void sendfunc(String ob,String queueName){
        try (Connection connection = connectionFactory.createConnection();
             Channel channel = connection.createChannel(false)) {
            String routing_key = queueName;
            channel.basicQos(100);
            //channel.queueDeclare(routing_key,false,false,false,null);
            //channel.queueBind(routing_key, EXCHANGE_NAME, routing_key);
            channel.basicPublish(EXCHANGE_NAME, routing_key , null,ob.getBytes(StandardCharsets.UTF_8));
            channel.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    */