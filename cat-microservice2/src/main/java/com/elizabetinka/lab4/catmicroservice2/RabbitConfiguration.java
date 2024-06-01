package com.elizabetinka.lab4.catmicroservice2;


import com.elizabetinka.lab4.dto.JacksonMessageConverter;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class RabbitConfiguration {


    //настраиваем соединение с RabbitMQ
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

    @Bean
    public MessageConverter jackson2MessageConverter() {
        JacksonMessageConverter jsonMessageConverter = new JacksonMessageConverter();
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory customListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(jackson2MessageConverter());
        return factory;
    }
    private void declareQueue(ConnectionFactory connectionFactory) {
        try (Connection connection = connectionFactory.createConnection();
             Channel channel = connection.createChannel(false)) {
            List<String> keys = List.of("getAllCats","updateCat","deleteCatById", "getCat","deleteAllCat","addCat","getAllCatsByName","getAllCatsByBreed","getAllCatsByOwnerId","getAllCatsByBirthday","getAllCatsByColor");

            String OWNER_EXCHANGE_NAME = "CatExchange";

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
    public DirectExchange CatExchange() {
        return new DirectExchange("CatExchange");
    }

    @Bean
    public Queue Queue2() {
        return new Queue("getAllCats",true,false,false,null);
    }
    @Bean
    public Queue Queue3() {
        return new Queue("updateCat",true,false,false,null);
    }
    @Bean
    public Queue Queue4() {
        return new Queue("deleteCatById",true,false,false,null);
    }
    @Bean
    public Queue Queue5() {
        return new Queue("getCat",true,false,false,null);
    }
    @Bean
    public Queue Queue6() {
        return new Queue("deleteAllCat",true,false,false,null);
    }
    @Bean
    public Queue Queue7() {
        return new Queue("addCat",true,false,false,null);
    }
    @Bean
    public Queue Queue8() {
        return new Queue("getAllCatsByName",true,false,false,null);
    }
    @Bean
    public Queue getOwner() {
        return new Queue("getAllCatsByBreed",true,false,false,null);
    }
    @Bean
    public Queue Queue10() {
        return new Queue("getAllCatsByOwnerId",true,false,false,null);
    }
    @Bean
    public Queue Queue11() {
        return new Queue("getAllCatsByBirthday",true,false,false,null);
    }
    @Bean
    public Queue Queue12() {
        return new Queue("getAllCatsByColor",true,false,false,null);
    }

    @Bean
    public Binding CatExchangeBilding2() {
        return BindingBuilder.bind(Queue2()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding3() {
        return BindingBuilder.bind(Queue3()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding4() {
        return BindingBuilder.bind(Queue4()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding5() {
        return BindingBuilder.bind(Queue5()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding6() {
        return BindingBuilder.bind(Queue6()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding7() {
        return BindingBuilder.bind(Queue7()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding8() {
        return BindingBuilder.bind(Queue8()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding10() {
        return BindingBuilder.bind(Queue10()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding11() {
        return BindingBuilder.bind(Queue11()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding12() {
        return BindingBuilder.bind(Queue12()).to(CatExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBildingGet() {
        return BindingBuilder.bind(getOwner()).to(CatExchange()).withQueueName();
    }


/*
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }


    @Bean
    public DirectExchange CatAnswerExchange() {
        return new DirectExchange("CatAnswerExchange");
    }

    @Bean
    public DirectExchange FromCatToOwnerExchange() {
        return new DirectExchange("FromCatToOwnerExchange");
    }
    //объявляем очередь с именем queue1

    //объявляем очередь с именем queue1

    //объявляем очередь с именем queue1
    //@Bean
    //public Binding CatExchangeBilding() {
    //    return BindingBuilder.bind(CatExchange());
    //}



    /*

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
