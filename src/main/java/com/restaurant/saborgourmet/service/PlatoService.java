package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.model.Plato;
import java.util.List;

public interface PlatoService {
    List<Plato> findAll();
    Plato findById(Long id);
    Plato save(Plato plato);
    void deleteById(Long id);
    List<Plato> findByEstado(Boolean estado);
    List<Plato> findByTipo(String tipo);
}