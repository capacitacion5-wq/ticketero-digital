package com.example.ticketero.model.enums;

public enum QueueType {
    CAJA("Caja", 5, 1),
    PERSONAL_BANKER("Personal Banker", 15, 2),
    EMPRESAS("Empresas", 20, 3),
    GERENCIA("Gerencia", 30, 4);

    private final String displayName;
    private final int avgTimeMinutes;
    private final int priority;

    QueueType(String displayName, int avgTimeMinutes, int priority) {
        this.displayName = displayName;
        this.avgTimeMinutes = avgTimeMinutes;
        this.priority = priority;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getAvgTimeMinutes() {
        return avgTimeMinutes;
    }

    public int getPriority() {
        return priority;
    }
}
