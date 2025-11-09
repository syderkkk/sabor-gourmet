package com.restaurant.saborgourmet.repository;

import com.restaurant.saborgourmet.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoIdPedido(Long idPedido);

    void deleteByPedidoIdPedido(Long idPedido);
}
