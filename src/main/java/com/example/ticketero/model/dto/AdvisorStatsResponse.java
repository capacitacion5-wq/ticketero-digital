package com.example.ticketero.model.dto;

import com.example.ticketero.model.enums.AdvisorStatus;

public record AdvisorStatsResponse(
    Long id,
    String name,
    Integer moduleNumber,
    AdvisorStatus status,
    Integer assignedTicketsCount
) {}
