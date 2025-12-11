package com.example.ticketero.service;

import com.example.ticketero.model.entity.Advisor;
import com.example.ticketero.model.entity.Ticket;
import com.example.ticketero.model.enums.QueueType;
import com.example.ticketero.model.enums.TicketStatus;
import com.example.ticketero.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QueueManagementService {

    private final TicketRepository ticketRepository;
    private final AdvisorService advisorService;

    public void asignarTicketAProximoAsesor(QueueType queueType) {
        List<Ticket> ticketsEnEspera = ticketRepository
            .findByStatusAndQueueTypeOrderByCreatedAtAsc(TicketStatus.EN_ESPERA, queueType);

        if (ticketsEnEspera.isEmpty()) {
            return;
        }

        Ticket proximoTicket = ticketsEnEspera.get(0);
        Optional<Advisor> advisorDisponible = advisorService.findAvailableAdvisor();

        if (advisorDisponible.isPresent()) {
            Advisor advisor = advisorDisponible.get();
            proximoTicket.setAssignedAdvisor(advisor);
            proximoTicket.setAssignedModuleNumber(advisor.getModuleNumber());
            proximoTicket.setStatus(TicketStatus.PROXIMO);
            
            ticketRepository.save(proximoTicket);
            advisorService.assignTicket(advisor);
            
            log.info("Ticket {} asignado a asesor {}", proximoTicket.getNumero(), advisor.getName());
        }
    }

    public void actualizarPosicionesEnCola(QueueType queueType) {
        List<Ticket> ticketsEnCola = ticketRepository
            .findByStatusAndQueueTypeOrderByCreatedAtAsc(TicketStatus.EN_ESPERA, queueType);

        for (int i = 0; i < ticketsEnCola.size(); i++) {
            Ticket ticket = ticketsEnCola.get(i);
            ticket.setPositionInQueue(i + 1);
            ticket.setEstimatedWaitMinutes((i + 1) * 5);
            ticketRepository.save(ticket);
        }
    }

    public Integer obtenerTamañoCola(QueueType queueType) {
        return ticketRepository.countByStatusAndQueueType(TicketStatus.EN_ESPERA, queueType);
    }
}
