package com.example.ticketero.scheduler;

import com.example.ticketero.model.entity.Mensaje;
import com.example.ticketero.repository.MensajeRepository;
import com.example.ticketero.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MensajeScheduler {

    private final MensajeRepository mensajeRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void procesarMensajesPendientes() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Mensaje> mensajesPendientes = mensajeRepository.findPendingMessages("PENDIENTE", ahora);

        if (mensajesPendientes.isEmpty()) {
            log.debug("No hay mensajes pendientes");
            return;
        }

        log.info("Procesando {} mensajes pendientes", mensajesPendientes.size());

        for (Mensaje mensaje : mensajesPendientes) {
            try {
                notificationService.enviarMensajeProgramado(mensaje);
            } catch (Exception e) {
                log.error("Error procesando mensaje {}: {}", mensaje.getId(), e.getMessage());
            }
        }
    }
}
