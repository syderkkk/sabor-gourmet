package com.restaurant.saborgourmet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "platos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plato")
    private Long idPlato;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @JsonIgnore
    @OneToMany(mappedBy = "plato", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DetallePedido> detallesPedido = new HashSet<>();
}
