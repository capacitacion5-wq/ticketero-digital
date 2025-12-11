package com.example.ticketero.service;

import com.example.ticketero.exception.TicketActivoExistenteException;
import com.example.ticketero.exception.TicketNotFoundException;
import com.example.ticketero.model.dto.TicketCreateRequest;
import com.example.ticketero.model.dto.TicketResponse;
import com.example.ticketero.model.entity.Ticket;
import com.example.ticketero.model.enums.TicketStatus;
import com.example.ticketero.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AtomicInteger ticketCounter = new AtomicInteger(0);

    @Transactional
    public TicketResponse crearTicket(TicketCreateRequest request) {
        log.info("Creando ticket para nationalId: {}", request.nationalId());

        validarTicketActivoExistente(request.nationalId());

        String numero = generarNumeroTicket(request.queueType().name().charAt(0));
        int posicion = calcularPosicionEnCola(request.queueType());
        int tiempoEstimado = calcularTiempoEstimado(posicion, request.queueType());

        Ticket ticket = Ticket.builder()
            .nationalId(request.nationalId())
            .telefono(request.telefono())
            .branchOffice(request.branchOffice())
            .queueType(request.queueType())
            .status(TicketStatus.EN_ESPERA)
            .numero(numero)
            .positionInQueue(posicion)
            .estimatedWaitMinutes(tiempoEstimado)
            .build();

        ticket = ticketRepository.save(ticket);
        log.info("Ticket creado: {}", ticket.getNumero());

        return toResponse(ticket);
    }

    public TicketResponse obtenerTicketPorCodigo(UUID codigoReferencia) {
        Ticket ticket = ticketRepository.findByCodigoReferencia(codigoReferencia)
            .orElseThrow(() -> new TicketNotFoundException("Ticket no encontrado"));
        return toResponse(ticket);
    }

    private void validarTicketActivoExistente(String nationalId) {
        List<TicketStatus> estadosActivos = List.of(
            TicketStatus.EN_ESPERA, 
            TicketStatus.PROXIMO, 
            TicketStatus.ATENDIENDO
        );
        
        ticketRepository.findByNationalIdAndStatusIn(nationalId, estadosActivos)
            .ifPresent(t -> {
                throw new TicketActivoExistenteException(
                    "Ya tienes un ticket activo: " + t.getNumero()
                );
            });
    }

    private String generarNumeroTicket(char prefijo) {
        int numero = ticketCounter.incrementAndGet();
        return prefijo + String.format("%02d", numero % 100);
    }

    private int calcularPosicionEnCola(Object queueType) {
        return ticketCounter.get();
    }

    private int calcularTiempoEstimado(int posicion, Object queueType) {
        return posicion * 5;
    }

    private TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
            ticket.getCodigoReferencia(),
            ticket.getNumero(),
            ticket.getNationalId(),
            ticket.getBranchOffice(),
            ticket.getQueueType(),
            ticket.getStatus(),
            ticket.getPositionInQueue(),
            ticket.getEstimatedWaitMinutes(),
            ticket.getAssignedModuleNumber(),
            ticket.getCreatedAt()
        );
    }
}
