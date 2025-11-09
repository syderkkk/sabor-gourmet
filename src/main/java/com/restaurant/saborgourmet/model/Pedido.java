package com.restaurant.saborgourmet.model;

import com.restaurant.saborgourmet.model.enums.EstadoPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @ManyToOne(fetch = FetchType.EAGER)  // ✅ EAGER
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @NotNull(message = "La mesa es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)  // ✅ EAGER
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa mesa;

    @CreationTimestamp
    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    // ✅ CAMBIO AQUÍ: EAGER
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<DetallePedido> detalles = new HashSet<>();

    @Version
    @Column(name = "version")
    private Long version;

    public void calcularTotal() {
        this.total = detalles.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
        calcularTotal();
    }

    public void removeDetalle(DetallePedido detalle) {
        detalles.remove(detalle);
        detalle.setPedido(null);
        calcularTotal();
    }
}
