package com.example.ticketero.model.dto;

public record SummaryResponse(
    Integer totalTicketsToday,
    Integer ticketsInQueue,
    Integer ticketsBeingServed,
    Integer completedTickets,
    Integer availableAdvisors,
    Integer busyAdvisors
) {}
