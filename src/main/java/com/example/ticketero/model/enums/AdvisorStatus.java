package com.example.ticketero.model.enums;

public enum AdvisorStatus {
    AVAILABLE("Disponible"),
    BUSY("Ocupado"),
    OFFLINE("Desconectado");

    private final String displayName;

    AdvisorStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
