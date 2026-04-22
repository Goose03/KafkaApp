package com.lta.backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lta.backend.service.MensajeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/consumer")
public class ConsumerResource {

    @Autowired
    private MensajeService mensajeService;

    // Devuelve TODOS los mensajes recibidos
    @GetMapping("/mensajes")
    public ResponseEntity<List<Map<String, String>>> getMensajes() {
        return ResponseEntity.ok(mensajeService.getMensajes());
    }

    // Devuelve mensajes filtrados por tópico
    // Ejemplo: GET /consumer/mensajes/gestion-pacientes
    @GetMapping("/mensajes/{topico}")
    public ResponseEntity<List<Map<String, String>>> getMensajesPorTopico(@PathVariable String topico) {
        return ResponseEntity.ok(mensajeService.getMensajesPorTopico(topico));
    }
}