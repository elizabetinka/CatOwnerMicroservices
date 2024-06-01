package com.elizabetinka.lab4.catmicroservice2;

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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.format.DateTimeFormatter;

@EntityScan("com.elizabetinka.lab4.jpa")
@EnableJpaRepositories("com.elizabetinka.lab4.jpa")
@SpringBootApplication(scanBasePackages= {
        "com.elizabetinka.lab4.catmicroservice2"})
@Import(RabbitConfiguration.class)
public class CatMicroservice2Application {

    public static void main(String[] args) {
        SpringApplication.run(CatMicroservice2Application.class, args);
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
