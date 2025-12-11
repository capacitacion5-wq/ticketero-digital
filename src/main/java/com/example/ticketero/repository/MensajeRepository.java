package com.example.ticketero.repository;

import com.example.ticketero.model.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("SELECT m FROM Mensaje m WHERE m.estadoEnvio = :estado AND m.fechaProgramada <= :ahora ORDER BY m.fechaProgramada ASC")
    List<Mensaje> findPendingMessages(
        @Param("estado") String estado,
        @Param("ahora") LocalDateTime ahora
    );

    @Query("SELECT m FROM Mensaje m WHERE m.ticket.id = :ticketId ORDER BY m.createdAt ASC")
    List<Mensaje> findByTicketId(@Param("ticketId") Long ticketId);

    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.estadoEnvio = 'PENDIENTE'")
    Integer countPendingMessages();
}
