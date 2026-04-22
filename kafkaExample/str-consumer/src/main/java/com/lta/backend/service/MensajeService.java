package com.lta.backend.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
@Service
public class MensajeService {

    // Lista thread-safe para almacenar los mensajes recibidos de Kafka
    private final CopyOnWriteArrayList<Map<String, String>> mensajes = new CopyOnWriteArrayList<>();

    public void guardarMensaje(String topico, int particion, String tipo, String contenido) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        var entrada = Map.of(
                "topico", topico,
                "particion", String.valueOf(particion),
                "tipo", tipo,
                "contenido", contenido,
                "hora", timestamp
        );
        mensajes.add(entrada);
        log.info("Guardado en memoria → tópico={} partición={} tipo={}", topico, particion, tipo);
    }

    public List<Map<String, String>> getMensajes() {
        return new ArrayList<>(mensajes);
    }

    public List<Map<String, String>> getMensajesPorTopico(String topico) {
        return mensajes.stream()
                .filter(m -> m.get("topico").equals(topico))
                .toList();
    }
}
