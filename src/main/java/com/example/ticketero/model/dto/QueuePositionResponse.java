package com.example.ticketero.model.dto;

import com.example.ticketero.model.enums.TicketStatus;

public record QueuePositionResponse(
    String numero,
    Integer positionInQueue,
    Integer estimatedWaitMinutes,
    TicketStatus status,
    Integer assignedModuleNumber
) {}
