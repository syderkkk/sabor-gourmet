package com.restaurant.saborgourmet.service.impl;

import com.restaurant.saborgourmet.exception.ResourceNotFoundException;
import com.restaurant.saborgourmet.model.Plato;
import com.restaurant.saborgourmet.repository.PlatoRepository;
import com.restaurant.saborgourmet.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlatoServiceImpl implements PlatoService {

    private final PlatoRepository platoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Plato> findAll() {
        return platoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Plato findById(Long id) {
        return platoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con ID: " + id));
    }

    @Override
    public Plato save(Plato plato) {
        return platoRepository.save(plato);
    }

    @Override
    public void deleteById(Long id) {
        platoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plato> findByEstado(Boolean estado) {
        return platoRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plato> findByTipo(String tipo) {
        return platoRepository.findByTipoAndEstado(tipo, true);
    }
}
