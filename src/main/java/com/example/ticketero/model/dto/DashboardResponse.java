package com.example.ticketero.model.dto;

import java.util.List;

public record DashboardResponse(
    SummaryResponse summary,
    List<QueueStatusResponse> queues,
    List<AdvisorStatsResponse> advisors
) {}
