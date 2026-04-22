package com.lta.backend.listeners;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;
import com.lta.backend.service.MensajeService;

@Log4j2
@Component
public class StrConsumerListener {

    @Autowired
    private MensajeService mensajeService;

    // ─── TÓPICO 1: gestion-pacientes ──────────────────────────────────────────

    @KafkaListener(
            groupId = "grupo-pacientes",
            topicPartitions = @TopicPartition(topic = "gestion-pacientes", partitions = {"0"}),
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenRegistroPaciente(String message) {
        log.info("📋 [REGISTRO PACIENTE] Partición 0 → {}", message);
        mensajeService.guardarMensaje("gestion-pacientes", 0, "Registro", message);
    }

    @KafkaListener(
            groupId = "grupo-pacientes",
            topicPartitions = @TopicPartition(topic = "gestion-pacientes", partitions = {"1"}),
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenActualizacionPaciente(String message) {
        log.info("✏️ [ACTUALIZACIÓN PACIENTE] Partición 1 → {}", message);
        mensajeService.guardarMensaje("gestion-pacientes", 1, "Actualización", message);
    }

    @KafkaListener(
            groupId = "grupo-pacientes",
            topicPartitions = @TopicPartition(topic = "gestion-pacientes", partitions = {"2"}),
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenEliminacionPaciente(String message) {
        log.info("🗑️ [ELIMINACIÓN PACIENTE] Partición 2 → {}", message);
        mensajeService.guardarMensaje("gestion-pacientes", 2, "Eliminación", message);
    }

    // ─── TÓPICO 2: gestion-citas ───────────────────────────────────────────────

    @KafkaListener(
            groupId = "grupo-citas",
            topicPartitions = @TopicPartition(topic = "gestion-citas", partitions = {"0"}),
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenCreacionCita(String message) {
        log.info("📅 [CREACIÓN CITA] Partición 0 → {}", message);
        mensajeService.guardarMensaje("gestion-citas", 0, "Creación", message);
    }

    @KafkaListener(
            groupId = "grupo-citas",
            topicPartitions = @TopicPartition(topic = "gestion-citas", partitions = {"1"}),
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenCancelacionCita(String message) {
        log.info("❌ [CANCELACIÓN CITA] Partición 1 → {}", message);
        mensajeService.guardarMensaje("gestion-citas", 1, "Cancelación", message);
    }

    @KafkaListener(
            groupId = "grupo-citas",
            topicPartitions = @TopicPartition(topic = "gestion-citas", partitions = {"2"}),
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenReprogramacionCita(String message) {
        log.info("🔄 [REPROGRAMACIÓN CITA] Partición 2 → {}", message);
        mensajeService.guardarMensaje("gestion-citas", 2, "Reprogramación", message);
    }

    // ─── TÓPICO 3: visualizacion-estado ───────────────────────────────────────

    @KafkaListener(
            groupId = "grupo-visualizacion",
            topicPartitions = @TopicPartition(topic = "visualizacion-estado", partitions = {"0"}),
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenEstadoPaciente(String message) {
        log.info("👁️ [CONSULTA ESTADO] Partición 0 → {}", message);
        mensajeService.guardarMensaje("visualizacion-estado", 0, "Consulta Estado", message);
    }

    @KafkaListener(
            groupId = "grupo-visualizacion",
            topicPartitions = @TopicPartition(topic = "visualizacion-estado", partitions = {"1"}),
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenHistorialMedico(String message) {
        log.info("📂 [HISTORIAL MÉDICO] Partición 1 → {}", message);
        mensajeService.guardarMensaje("visualizacion-estado", 1, "Historial Médico", message);
    }
}