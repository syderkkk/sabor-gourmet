package com.restaurant.saborgourmet.repository;

import com.restaurant.saborgourmet.model.Pedido;
import com.restaurant.saborgourmet.model.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByEstadoOrderByFechaHoraDesc(EstadoPedido estado);

    List<Pedido> findByMesaIdMesaAndEstadoIn(Long idMesa, List<EstadoPedido> estados);

    @Query("SELECT p FROM Pedido p WHERE p.fechaHora BETWEEN :inicio AND :fin ORDER BY p.fechaHora DESC")
    List<Pedido> findByFechaHoraBetween(@Param("inicio") LocalDateTime inicio,
                                        @Param("fin") LocalDateTime fin);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detalles WHERE p.idPedido = :id")
    Pedido findByIdWithDetalles(@Param("id") Long id);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado")
    Long countByEstado(@Param("estado") EstadoPedido estado);
}
