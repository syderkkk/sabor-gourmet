package com.restaurant.saborgourmet.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long idCliente;

    @NotNull(message = "La mesa es obligatoria")
    private Long idMesa;

    private String observaciones;

    private List<DetallePedidoDTO> detalles;
}
