package com.restaurant.saborgourmet.model.enums;

public enum EstadoPedido {
    PENDIENTE("Pendiente"),
    EN_PREPARACION("En Preparación"),
    SERVIDO("Servido"),
    CERRADO("Cerrado"),
    CANCELADO("Cancelado");

    private final String displayName;

    EstadoPedido(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
