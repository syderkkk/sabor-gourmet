package com.restaurant.saborgourmet.model.enums;

public enum RolUsuario {
    ADMIN("Administrador"),
    MOZO("Mozo"),
    COCINERO("Cocinero"),
    CAJERO("Cajero");

    private final String displayName;

    RolUsuario(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
