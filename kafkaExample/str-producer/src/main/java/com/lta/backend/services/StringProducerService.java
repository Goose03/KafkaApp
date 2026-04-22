package com.lta.backend.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class StringProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, int partition, String message) {
        kafkaTemplate.send(topic, partition, null, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error al enviar mensaje a '{}' partición {}: {}", topic, partition, ex.getMessage());
                    } else {
                        log.info("✅ Enviado a tópico '{}' partición {} offset {}",
                                topic,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}