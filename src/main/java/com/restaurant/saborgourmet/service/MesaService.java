package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.model.Mesa;
import com.restaurant.saborgourmet.model.enums.EstadoMesa;
import java.util.List;

public interface MesaService {
    List<Mesa> findAll();
    Mesa findById(Long id);
    Mesa save(Mesa mesa);
    void deleteById(Long id);
    List<Mesa> findByEstado(EstadoMesa estado);
    Mesa cambiarEstado(Long id, EstadoMesa nuevoEstado);
}