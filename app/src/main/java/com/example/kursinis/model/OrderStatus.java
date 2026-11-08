package com.example.kursinis.model;

public enum OrderStatus {
    PENDING("Laukia patvirtinimo"),
    SEEN_BY_STAFF("Peržiūrėtas restorano"),
    IN_PROGRESS("Ruošiamas"),
    IN_DELIVERY("Pristatomas"),
    COMPLETE("Pristatytas");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}