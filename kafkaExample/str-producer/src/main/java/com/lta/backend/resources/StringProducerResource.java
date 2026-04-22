package com.lta.backend.resources;

import com.lta.backend.services.StringProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producer")
public class StringProducerResource {

    @Autowired
    private StringProducerService stringProducerService;

    // ─── TÓPICO 1: gestion-pacientes ───────────────────────────────────────────

    // Partición 0: Registro de paciente
    @PostMapping("/pacientes/registro")
    public ResponseEntity<?> registrarPaciente(@RequestBody String mensaje) {
        stringProducerService.sendMessage("gestion-pacientes", 0, mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body("Paciente registrado enviado a Kafka");
    }

    // Partición 1: Actualización de paciente
    @PostMapping("/pacientes/actualizacion")
    public ResponseEntity<?> actualizarPaciente(@RequestBody String mensaje) {
        stringProducerService.sendMessage("gestion-pacientes", 1, mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body("Actualización de paciente enviada a Kafka");
    }

    // Partición 2: Eliminación de paciente
    @PostMapping("/pacientes/eliminacion")
    public ResponseEntity<?> eliminarPaciente(@RequestBody String mensaje) {
        stringProducerService.sendMessage("gestion-pacientes", 2, mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body("Eliminación de paciente enviada a Kafka");
    }

    // ─── TÓPICO 2: gestion-citas ───────────────────────────────────────────────

    // Partición 0: Creación de cita
    @PostMapping("/citas/creacion")
    public ResponseEntity<?> crearCita(@RequestBody String mensaje) {
        stringProducerService.sendMessage("gestion-citas", 0, mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body("Creación de cita enviada a Kafka");
    }

    // Partición 1: Cancelación de cita
    @PostMapping("/citas/cancelacion")
    public ResponseEntity<?> cancelarCita(@RequestBody String mensaje) {
        stringProducerService.sendMessage("gestion-citas", 1, mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cancelación de cita enviada a Kafka");
    }

    // Partición 2: Reprogramación de cita
    @PostMapping("/citas/reprogramacion")
    public ResponseEntity<?> reprogramarCita(@RequestBody String mensaje) {
        stringProducerService.sendMessage("gestion-citas", 2, mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body("Reprogramación de cita enviada a Kafka");
    }

    // ─── TÓPICO 3: visualizacion-estado ───────────────────────────────────────

    // Partición 0: Consulta de estado del paciente
    @PostMapping("/visualizacion/estado")
    public ResponseEntity<?> consultarEstado(@RequestBody String mensaje) {
        stringProducerService.sendMessage("visualizacion-estado", 0, mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body("Consulta de estado enviada a Kafka");
    }

    // Partición 1: Historial médico
    @PostMapping("/visualizacion/historial")
    public ResponseEntity<?> consultarHistorial(@RequestBody String mensaje) {
        stringProducerService.sendMessage("visualizacion-estado", 1, mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body("Historial médico enviado a Kafka");
    }
}