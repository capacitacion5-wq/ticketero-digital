package com.example.ticketero.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public boolean enviarMensaje(String telefono, String mensaje) {
        if (botToken == null || botToken.isBlank()) {
            log.warn("Telegram bot token no configurado");
            return false;
        }

        try {
            log.info("Enviando mensaje a {}: {}", telefono, mensaje);
            // Implementación real en PROMPT 4
            return true;
        } catch (Exception e) {
            log.error("Error enviando mensaje a {}: {}", telefono, e.getMessage());
            return false;
        }
    }
}
