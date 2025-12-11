package com.example.ticketero.service;

import com.example.ticketero.model.entity.Mensaje;
import com.example.ticketero.model.entity.Ticket;
import com.example.ticketero.model.enums.MessageTemplate;
import com.example.ticketero.repository.MensajeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final MensajeRepository mensajeRepository;
    private final TelegramService telegramService;

    public void programarMensajesParaTicket(Ticket ticket) {
        if (ticket.getTelefono() == null || ticket.getTelefono().isBlank()) {
            log.debug("Ticket {} sin teléfono, no se programan mensajes", ticket.getNumero());
            return;
        }

        // Mensaje 1: Inmediato
        crearMensaje(ticket, MessageTemplate.TOTEM_TICKET_CREADO, LocalDateTime.now());

        // Mensaje 2: En 5 minutos
        crearMensaje(ticket, MessageTemplate.TOTEM_PROXIMO_TURNO, LocalDateTime.now().plusMinutes(5));

        // Mensaje 3: En 10 minutos
        crearMensaje(ticket, MessageTemplate.TOTEM_ES_TU_TURNO, LocalDateTime.now().plusMinutes(10));

        log.info("Mensajes programados para ticket {}", ticket.getNumero());
    }

    private void crearMensaje(Ticket ticket, MessageTemplate plantilla, LocalDateTime fechaProgramada) {
        Mensaje mensaje = Mensaje.builder()
            .ticket(ticket)
            .plantilla(plantilla)
            .estadoEnvio("PENDIENTE")
            .fechaProgramada(fechaProgramada)
            .intentos(0)
            .build();

        mensajeRepository.save(mensaje);
    }

    public void enviarMensajeProgramado(Mensaje mensaje) {
        String contenido = mensaje.getPlantilla().getTemplate();
        boolean enviado = telegramService.enviarMensaje(mensaje.getTicket().getTelefono(), contenido);

        if (enviado) {
            mensaje.setEstadoEnvio("ENVIADO");
            mensaje.setFechaEnvio(LocalDateTime.now());
        } else {
            mensaje.setIntentos(mensaje.getIntentos() + 1);
            if (mensaje.getIntentos() >= 3) {
                mensaje.setEstadoEnvio("FALLIDO");
            }
        }

        mensajeRepository.save(mensaje);
    }
}
