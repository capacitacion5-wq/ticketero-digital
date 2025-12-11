package com.example.ticketero.model.dto;

import com.example.ticketero.model.enums.QueueType;
import com.example.ticketero.model.enums.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
    UUID codigoReferencia,
    String numero,
    String nationalId,
    String branchOffice,
    QueueType queueType,
    TicketStatus status,
    Integer positionInQueue,
    Integer estimatedWaitMinutes,
    Integer assignedModuleNumber,
    LocalDateTime createdAt
) {}
