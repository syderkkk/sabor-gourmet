package com.restaurant.saborgourmet.model.enums;

public enum EstadoMesa {
    DISPONIBLE("Disponible"),
    OCUPADA("Ocupada"),
    RESERVADA("Reservada"),
    MANTENIMIENTO("Mantenimiento");

    private final String displayName;

    EstadoMesa(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
