package com.example.ticketero.scheduler;

import com.example.ticketero.model.enums.QueueType;
import com.example.ticketero.service.QueueManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueProcessorScheduler {

    private final QueueManagementService queueManagementService;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void procesarColas() {
        log.debug("Procesando colas...");

        for (QueueType queueType : QueueType.values()) {
            try {
                queueManagementService.actualizarPosicionesEnCola(queueType);
                queueManagementService.asignarTicketAProximoAsesor(queueType);
            } catch (Exception e) {
                log.error("Error procesando cola {}: {}", queueType, e.getMessage());
            }
        }
    }
}
