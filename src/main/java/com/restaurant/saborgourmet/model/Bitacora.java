package com.restaurant.saborgourmet.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Long idBitacora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "accion", nullable = false, length = 500)
    private String accion;

    @Column(name = "entidad", length = 100)
    private String entidad;

    @Column(name = "id_entidad")
    private Long idEntidad;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;

    @CreationTimestamp
    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;
}
