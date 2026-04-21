package com.lta.backend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StringProducerService {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void sendMessage(String message){
        String topic;
        if(message.contains("test")){
            topic = "test";
        }
        else if(message.contains("pacientes")){
            topic = "Gestion-pacientes";
        }
        else if(message.contains("citas")){
            topic = "Gestion-citas";
        }
        else if(message.contains("visualización")){
            topic = "Visualización";
        }
        else{
            topic = "str-topic";
        }


        kafkaTemplate.send(topic, 1, null, message).whenComplete((result,ex) -> {
           if(ex != null){
               log.error("Error, al enviar el mensaje: {}",ex.getMessage());
           }
           log.info("Mensaje enviado con éxito: {}",result.getProducerRecord().value());
           log.info("Particion {}, Offset {}", result.getRecordMetadata().partition(),result.getRecordMetadata().offset());
        });
    }
}
