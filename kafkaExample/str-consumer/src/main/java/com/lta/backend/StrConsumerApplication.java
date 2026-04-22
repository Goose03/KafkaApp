package com.lta.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

// Se excluye KafkaAdminAutoConfiguration para evitar que el Consumer
// cree tópicos automáticamente. La creación de tópicos es responsabilidad
// exclusiva del Producer (str-producer/KafkaAdminConfig.java)
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.class
})
@EnableKafka
public class StrConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrConsumerApplication.class, args);
    }
}