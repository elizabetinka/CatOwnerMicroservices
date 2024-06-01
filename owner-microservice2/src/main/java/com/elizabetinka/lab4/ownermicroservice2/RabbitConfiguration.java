package com.elizabetinka.lab4.ownermicroservice2;


import com.elizabetinka.lab4.dto.JacksonMessageConverter;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
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
            List<String> keys = List.of("addUser","getOwnerByBirthday","getOwnerByName", "deleteOwner","addOwner","getOwner","getOwnerById","deleteOwnerById","updateOwner","getUserByUsername");
            channel.basicQos(100);
            String OWNER_EXCHANGE_NAME = "OwnerExchange";

            for (String key : keys) {
                channel.queueDeclare(key, false, false, false, null);
                channel.queueBind(key, OWNER_EXCHANGE_NAME, key);
            }

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
    public DirectExchange OwnerExchange() {
        return new DirectExchange("OwnerExchange");
    }

    @Bean
    public Queue Queue2() {
        return new Queue("addUser",true,false,false,null);
    }
    @Bean
    public Queue Queue3() {
        return new Queue("addOwner",true,false,false,null);
    }
    @Bean
    public Queue Queue4() {
        return new Queue("getUserByUsername",true,false,false,null);
    }
    @Bean
    public Queue Queue5() {
        return new Queue("getOwnerByName",true,false,false,null);
    }
    @Bean
    public Queue Queue6() {
        return new Queue("getOwnerById",true,false,false,null);
    }
    @Bean
    public Queue Queue7() {
        return new Queue("getOwnerByBirthday",true,false,false,null);
    }
    @Bean
    public Queue Queue8() {
        return new Queue("deleteOwner",true,false,false,null);
    }
    @Bean
    public Queue getOwner() {
        return new Queue("getOwner",true,false,false,null);
    }
    @Bean
    public Queue Queue10() {
        return new Queue("deleteOwnerById",true,false,false,null);
    }
    @Bean
    public Queue Queue11() {
        return new Queue("updateOwner",true,false,false,null);
    }

    @Bean
    public Binding CatExchangeBilding2() {
        return BindingBuilder.bind(Queue2()).to(OwnerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding3() {
        return BindingBuilder.bind(Queue3()).to(OwnerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding4() {
        return BindingBuilder.bind(Queue4()).to(OwnerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding5() {
        return BindingBuilder.bind(Queue5()).to(OwnerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding6() {
        return BindingBuilder.bind(Queue6()).to(OwnerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding7() {
        return BindingBuilder.bind(Queue7()).to(OwnerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding8() {
        return BindingBuilder.bind(Queue8()).to(OwnerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding10() {
        return BindingBuilder.bind(Queue10()).to(OwnerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBilding11() {
        return BindingBuilder.bind(Queue11()).to(OwnerExchange()).withQueueName();
    }
    @Bean
    public Binding CatExchangeBildingGet() {
        return BindingBuilder.bind(getOwner()).to(OwnerExchange()).withQueueName();
    }


/*



    @Bean
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(1);
        factory.setConsecutiveActiveTrigger(1);
        factory.setConnectionFactory(connectionFactory());
        registrar.setContainerFactory(factory);
        registrar.setEndpointRegistry(rabbitListenerEndpointRegistry());


    }

 */
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
