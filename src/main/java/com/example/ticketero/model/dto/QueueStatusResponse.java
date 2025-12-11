package com.example.ticketero.model.dto;

import com.example.ticketero.model.enums.QueueType;

public record QueueStatusResponse(
    QueueType queueType,
    Integer totalInQueue,
    Integer avgWaitTime,
    Integer nextTicketPosition
) {}
