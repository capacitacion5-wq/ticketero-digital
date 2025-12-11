package com.example.ticketero.model.enums;

public enum MessageTemplate {
    TOTEM_TICKET_CREADO("Tu ticket ha sido creado: {numero}. Posición: {posicion}"),
    TOTEM_PROXIMO_TURNO("¡Pronto será tu turno! Posición: {posicion}"),
    TOTEM_ES_TU_TURNO("¡Es tu turno! Dirígete al módulo {modulo}");

    private final String template;

    MessageTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
