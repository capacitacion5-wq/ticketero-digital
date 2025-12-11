package com.example.ticketero.model.enums;

public enum TicketStatus {
    EN_ESPERA("En Espera"),
    PROXIMO("Próximo"),
    ATENDIENDO("Atendiendo"),
    COMPLETADO("Completado"),
    CANCELADO("Cancelado");

    private final String displayName;

    TicketStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
