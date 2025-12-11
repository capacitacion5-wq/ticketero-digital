package com.example.ticketero.service;

import com.example.ticketero.model.entity.Advisor;
import com.example.ticketero.model.enums.AdvisorStatus;
import com.example.ticketero.repository.AdvisorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdvisorService {

    private final AdvisorRepository advisorRepository;

    public Optional<Advisor> findAvailableAdvisor() {
        List<Advisor> available = advisorRepository
            .findByStatusOrderByAssignedTicketsCountAsc(AdvisorStatus.AVAILABLE);
        
        return available.stream().findFirst();
    }

    @Transactional
    public void assignTicket(Advisor advisor) {
        advisor.setAssignedTicketsCount(advisor.getAssignedTicketsCount() + 1);
        if (advisor.getAssignedTicketsCount() >= 3) {
            advisor.setStatus(AdvisorStatus.BUSY);
        }
        advisorRepository.save(advisor);
        log.info("Ticket asignado a asesor: {}", advisor.getName());
    }

    @Transactional
    public void releaseTicket(Advisor advisor) {
        advisor.setAssignedTicketsCount(Math.max(0, advisor.getAssignedTicketsCount() - 1));
        if (advisor.getAssignedTicketsCount() < 3) {
            advisor.setStatus(AdvisorStatus.AVAILABLE);
        }
        advisorRepository.save(advisor);
        log.info("Ticket liberado de asesor: {}", advisor.getName());
    }

    public Integer countAvailableAdvisors() {
        return advisorRepository.countByStatus(AdvisorStatus.AVAILABLE);
    }

    public Integer countBusyAdvisors() {
        return advisorRepository.countByStatus(AdvisorStatus.BUSY);
    }

    public List<Advisor> getAllAdvisors() {
        return advisorRepository.findAll();
    }
}
