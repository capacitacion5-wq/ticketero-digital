package com.example.ticketero.controller;

import com.example.ticketero.model.dto.*;
import com.example.ticketero.model.enums.QueueType;
import com.example.ticketero.service.AdvisorService;
import com.example.ticketero.service.QueueManagementService;
import com.example.ticketero.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final TicketService ticketService;
    private final AdvisorService advisorService;
    private final QueueManagementService queueManagementService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {
        log.info("GET /api/admin/dashboard");

        SummaryResponse summary = new SummaryResponse(
            100, 25, 5, 70,
            advisorService.countAvailableAdvisors(),
            advisorService.countBusyAdvisors()
        );

        List<QueueStatusResponse> queues = new ArrayList<>();
        for (QueueType type : QueueType.values()) {
            queues.add(new QueueStatusResponse(
                type,
                queueManagementService.obtenerTamañoCola(type),
                type.getAvgTimeMinutes(),
                1
            ));
        }

        List<AdvisorStatsResponse> advisors = advisorService.getAllAdvisors().stream()
            .map(a -> new AdvisorStatsResponse(a.getId(), a.getName(), a.getModuleNumber(), a.getStatus(), a.getAssignedTicketsCount()))
            .toList();

        DashboardResponse dashboard = new DashboardResponse(summary, queues, advisors);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary() {
        log.info("GET /api/admin/summary");
        SummaryResponse summary = new SummaryResponse(
            100, 25, 5, 70,
            advisorService.countAvailableAdvisors(),
            advisorService.countBusyAdvisors()
        );
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/advisors")
    public ResponseEntity<List<AdvisorStatsResponse>> getAdvisors() {
        log.info("GET /api/admin/advisors");
        List<AdvisorStatsResponse> advisors = advisorService.getAllAdvisors().stream()
            .map(a -> new AdvisorStatsResponse(a.getId(), a.getName(), a.getModuleNumber(), a.getStatus(), a.getAssignedTicketsCount()))
            .toList();
        return ResponseEntity.ok(advisors);
    }
}
