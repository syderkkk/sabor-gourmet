package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.dto.DetallePedidoDTO;
import com.restaurant.saborgourmet.dto.PedidoDTO;
import com.restaurant.saborgourmet.model.Pedido;
import com.restaurant.saborgourmet.model.enums.EstadoPedido;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoService {

    List<Pedido> findAll();

    Pedido findById(Long id);

    Pedido save(Pedido pedido);

    Pedido crear(PedidoDTO pedidoDTO);

    Pedido actualizar(Long id, PedidoDTO pedidoDTO);

    Pedido cambiarEstado(Long id, EstadoPedido nuevoEstado);

    void deleteById(Long id);

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    Pedido addDetalle(Long idPedido, DetallePedidoDTO detalleDTO);

    Pedido removeDetalle(Long idPedido, Long idDetalle);

    Long countByEstado(EstadoPedido estado);
}
