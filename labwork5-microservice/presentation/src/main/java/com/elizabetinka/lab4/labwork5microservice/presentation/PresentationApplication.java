package com.elizabetinka.lab4.labwork5microservice.presentation;

import com.elizabetinka.lab4.labwork5microservice.services.RabbitConfiguration;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.format.DateTimeFormatter;


//@EntityScan("com.elizabetinka.lab4.dao.model")
//@EnableJpaRepositories("com.elizabetinka.lab4.dao.repositories")
@SpringBootApplication(scanBasePackages= {
        "com.elizabetinka.lab4.labwork5microservice"})
@Import({SecurityConfig.class, RabbitConfiguration.class})
public class PresentationApplication {
    public static void main(String[] args) {
        SpringApplication.run(PresentationApplication.class, args);
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return builder -> {

            // formatter
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // deserializers
            builder.deserializers(new LocalDateDeserializer(dateFormatter));
            builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));

            // serializers
            builder.serializers(new LocalDateSerializer(dateFormatter));
            builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
        };
    }
}


