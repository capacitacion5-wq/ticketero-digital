package com.example.ticketero.model.entity;

import com.example.ticketero.model.enums.MessageTemplate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(name = "plantilla", nullable = false, length = 50)
    private MessageTemplate plantilla;

    @Column(name = "estado_envio", nullable = false, length = 20)
    private String estadoEnvio;

    @Column(name = "fecha_programada", nullable = false)
    private LocalDateTime fechaProgramada;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "telegram_message_id", length = 50)
    private String telegramMessageId;

    @Column(name = "intentos", nullable = false)
    private Integer intentos;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (intentos == null) {
            intentos = 0;
        }
        if (estadoEnvio == null) {
            estadoEnvio = "PENDIENTE";
        }
    }
}
