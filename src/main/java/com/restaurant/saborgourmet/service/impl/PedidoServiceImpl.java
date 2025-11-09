package com.restaurant.saborgourmet.service.impl;

import com.restaurant.saborgourmet.dto.DetallePedidoDTO;
import com.restaurant.saborgourmet.dto.PedidoDTO;
import com.restaurant.saborgourmet.exception.ResourceNotFoundException;
import com.restaurant.saborgourmet.model.*;
import com.restaurant.saborgourmet.model.enums.EstadoMesa;
import com.restaurant.saborgourmet.model.enums.EstadoPedido;
import com.restaurant.saborgourmet.repository.*;
import com.restaurant.saborgourmet.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ClienteRepository clienteRepository;
    private final MesaRepository mesaRepository;
    private final PlatoRepository platoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido crear(PedidoDTO pedidoDTO) {
        log.info("Creando nuevo pedido para mesa: {}", pedidoDTO.getIdMesa());

        // Validar y obtener mesa
        Mesa mesa = mesaRepository.findById(pedidoDTO.getIdMesa())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));

        if (mesa.getEstado() == EstadoMesa.OCUPADA) {
            throw new IllegalStateException("La mesa ya está ocupada");
        }

        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setObservaciones(pedidoDTO.getObservaciones());

        // Agregar cliente si existe
        if (pedidoDTO.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(pedidoDTO.getIdCliente())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
            pedido.setCliente(cliente);
        }

        // Agregar detalles
        if (pedidoDTO.getDetalles() != null && !pedidoDTO.getDetalles().isEmpty()) {
            for (DetallePedidoDTO detalleDTO : pedidoDTO.getDetalles()) {
                Plato plato = platoRepository.findById(detalleDTO.getIdPlato())
                        .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado"));

                DetallePedido detalle = new DetallePedido();
                detalle.setPlato(plato);
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.calcularSubtotal();

                pedido.addDetalle(detalle);
            }
        }

        // Cambiar estado de mesa
        mesa.setEstado(EstadoMesa.OCUPADA);
        mesaRepository.save(mesa);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        log.info("Pedido creado exitosamente con ID: {}", pedidoGuardado.getIdPedido());

        return pedidoGuardado;
    }

    @Override
    public Pedido actualizar(Long id, PedidoDTO pedidoDTO) {
        log.info("Actualizando pedido ID: {}", id);

        Pedido pedido = findById(id);

        if (pedido.getEstado() == EstadoPedido.CERRADO) {
            throw new IllegalStateException("No se puede actualizar un pedido cerrado");
        }

        // Actualizar observaciones
        pedido.setObservaciones(pedidoDTO.getObservaciones());

        // Actualizar detalles si se proporcionan
        if (pedidoDTO.getDetalles() != null) {
            pedido.getDetalles().clear();

            for (DetallePedidoDTO detalleDTO : pedidoDTO.getDetalles()) {
                Plato plato = platoRepository.findById(detalleDTO.getIdPlato())
                        .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado"));

                DetallePedido detalle = new DetallePedido();
                detalle.setPlato(plato);
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.calcularSubtotal();

                pedido.addDetalle(detalle);
            }
        }

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        log.info("Cambiando estado del pedido {} a {}", id, nuevoEstado);

        Pedido pedido = findById(id);
        pedido.setEstado(nuevoEstado);

        // Si el pedido se cierra, liberar la mesa
        if (nuevoEstado == EstadoPedido.CERRADO || nuevoEstado == EstadoPedido.CANCELADO) {
            Mesa mesa = pedido.getMesa();
            mesa.setEstado(EstadoMesa.DISPONIBLE);
            mesaRepository.save(mesa);
        }

        return pedidoRepository.save(pedido);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Eliminando pedido ID: {}", id);

        Pedido pedido = findById(id);

        // Liberar mesa si está asociada
        if (pedido.getMesa() != null) {
            Mesa mesa = pedido.getMesa();
            mesa.setEstado(EstadoMesa.DISPONIBLE);
            mesaRepository.save(mesa);
        }

        pedidoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstadoOrderByFechaHoraDesc(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.findByFechaHoraBetween(inicio, fin);
    }

    @Override
    public Pedido addDetalle(Long idPedido, DetallePedidoDTO detalleDTO) {
        Pedido pedido = findById(idPedido);

        Plato plato = platoRepository.findById(detalleDTO.getIdPlato())
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado"));

        DetallePedido detalle = new DetallePedido();
        detalle.setPlato(plato);
        detalle.setCantidad(detalleDTO.getCantidad());
        detalle.calcularSubtotal();

        pedido.addDetalle(detalle);

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido removeDetalle(Long idPedido, Long idDetalle) {
        Pedido pedido = findById(idPedido);

        DetallePedido detalle = detallePedidoRepository.findById(idDetalle)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle no encontrado"));

        pedido.removeDetalle(detalle);
        detallePedidoRepository.delete(detalle);

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByEstado(EstadoPedido estado) {
        return pedidoRepository.countByEstado(estado);
    }
}
