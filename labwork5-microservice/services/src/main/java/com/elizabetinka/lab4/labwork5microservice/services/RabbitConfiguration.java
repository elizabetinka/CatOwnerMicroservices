package com.elizabetinka.lab4.labwork5microservice.services;



import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class RabbitConfiguration {


    //настраиваем соединение с RabbitMQ
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("password");
        //declareQueue(connectionFactory);
        return connectionFactory;
    }

    private void declareQueue(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.createConnection();
             Channel channel = connection.createChannel(false)) {
            List<String> keys = List.of("addCatAnswer","getCatAnswer","getAllCatsAnswer", "deleteCatByIdAnswer","deleteAllCatAnswer","getAllCatsByNameAnswer","getAllCatsByBirthdayAnswer","getAllCatsByBreedAnswer","getAllCatsByColorAnswer","getAllCatsByOwnerIdAnswer");

            String OWNER_EXCHANGE_NAME = "CatAnswerExchange";

            for (String key : keys) {
                channel.queueDeclare(key,false,false,false,null);
                channel.queueBind(key, OWNER_EXCHANGE_NAME, key);
            }
            keys = List.of("deleteOwnerByIdAnswer","getOwnerByIdAnswer","getOwnerAnswer", "addOwnerAnswer","deleteOwnerAnswer","getOwnerByNameAnswer","getOwnerByBirthdayAnswer","addUserAnswer");

            OWNER_EXCHANGE_NAME = "OwnerAnswerExchange";

            for (String key : keys) {
                channel.queueDeclare(key,false,false,false,null);
                channel.queueBind(key, OWNER_EXCHANGE_NAME, key);
            }
            channel.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }


    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public RabbitTemplate amqpOwnerTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReplyTimeout(60000);
        rabbitTemplate.setUseDirectReplyToContainer(false);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }
/*
 @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }


    @Bean
    public RabbitTemplate amqpOwnerTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReplyAddress(replyOwnerQueue().getName());
        rabbitTemplate.setReplyTimeout(60000);
        rabbitTemplate.setUseDirectReplyToContainer(false);
        return rabbitTemplate;
    }
    @Bean
    public RabbitTemplate amqpCatTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReplyAddress(replyCatQueue().getName());
        rabbitTemplate.setReplyTimeout(60000);
        rabbitTemplate.setUseDirectReplyToContainer(false);
        return rabbitTemplate;
    }



    @Bean
    public SimpleMessageListenerContainer replyListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueues(replyOwnerQueue(),replyCatQueue());
        container.setMessageListener(amqpOwnerTemplate());
        //container.setMessageListener(amqpCatTemplate());
        return container;
    }

    @Bean
    public Queue replyOwnerQueue() {
        return new Queue("owner.reply.queue");
    }
    @Bean
    public Queue replyCatQueue() {
        return new Queue("cat.reply.queue");
    }

    @Bean
    public DirectExchange OwnerAnswerExchange() {
        return new DirectExchange("CatAnswerExchange");
    }

    @Bean
    public DirectExchange CatAnswerExchange() {
        return new DirectExchange("CatAnswerExchange");
    }
*/
    @Bean
    public DirectExchange CatExchange() {
        return new DirectExchange("CatExchange");
    }
    //объявляем очередь с именем queue1
    @Bean
    public DirectExchange OwnerExchange() {
        return new DirectExchange("OwnerExchange");
    }





    /*

    @Bean
    public Binding CatExchangeBildingA() {
        return BindingBuilder.bind(Queue1A()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding2A() {
        return BindingBuilder.bind(Queue2A()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBildingaddUserAnswer() {
        return BindingBuilder.bind(addUserAnswer()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding4A() {
        return BindingBuilder.bind(Queue4A()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding5A() {
        return BindingBuilder.bind(Queue5A()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding6A() {
        return BindingBuilder.bind(Queue6A()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding7A() {
        return BindingBuilder.bind(Queue7A()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding8A() {
        return BindingBuilder.bind(Queue8A()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding9A() {
        return BindingBuilder.bind(Queue9A()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding10A() {
        return BindingBuilder.bind(Queue10A()).to(OwnerAnswerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding11A() {
        return BindingBuilder.bind(Queue11A()).to(OwnerAnswerExchange()).withQueueName();
    }

/*


/*
    //объявляем очередь с именем queue1
    @Bean
    public Binding CatExchangeBilding() {
        return BindingBuilder.bind("getAllCats").to(CatExchange());
    }



    /*

    //объявляем очередь с именем queue1
    @Bean
    public Queue CatQueue() {
        return new Queue("CatQueue");
    }
    //объявляем очередь с именем queue1
    @Bean
    public Queue OwnerQueue() {
        return new Queue("OwnerQueue");
    }

    //объявляем очередь с именем queue1
    @Bean
    public DirectExchange CatExchange() {
        return new DirectExchange("CatExchange");
    }
    //объявляем очередь с именем queue1
    @Bean
    public DirectExchange OwnerExchange() {
        return new DirectExchange("OwnerExchange");
    }
    //объявляем контейнер, который будет содержать листенер для сообщений
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer1() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames("queue1");

        return container;
    }
    */


}
