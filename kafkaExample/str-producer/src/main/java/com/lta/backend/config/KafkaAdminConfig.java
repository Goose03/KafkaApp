package com.lta.backend.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@Configuration
public class KafkaAdminConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    // Tópico 1: Gestión de pacientes — 3 particiones
    // Partición 0: Registro, Partición 1: Actualización, Partición 2: Eliminación
    // Tópico 2: Gestión de citas — 3 particiones
    // Partición 0: Creación, Partición 1: Cancelación, Partición 2: Reprogramación
    // Tópico 3: Visualización/estado — 2 particiones
    // Partición 0: Consulta de estado, Partición 1: Historial médico
    @Bean
    public KafkaAdmin.NewTopics topics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("gestion-pacientes").partitions(3).replicas(1).build(),
                TopicBuilder.name("gestion-citas").partitions(3).replicas(1).build(),
                TopicBuilder.name("visualizacion-estado").partitions(2).replicas(1).build()
        );
    }
}