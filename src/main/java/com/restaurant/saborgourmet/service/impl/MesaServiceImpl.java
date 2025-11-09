package com.restaurant.saborgourmet.service.impl;

import com.restaurant.saborgourmet.exception.ResourceNotFoundException;
import com.restaurant.saborgourmet.model.Mesa;
import com.restaurant.saborgourmet.model.enums.EstadoMesa;
import com.restaurant.saborgourmet.repository.MesaRepository;
import com.restaurant.saborgourmet.service.MesaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MesaServiceImpl implements MesaService {

    private final MesaRepository mesaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Mesa> findAll() {
        return mesaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Mesa findById(Long id) {
        return mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + id));
    }

    @Override
    public Mesa save(Mesa mesa) {
        if (mesa.getIdMesa() == null && mesaRepository.existsByNumero(mesa.getNumero())) {
            throw new IllegalStateException("Ya existe una mesa con el número: " + mesa.getNumero());
        }
        return mesaRepository.save(mesa);
    }

    @Override
    public void deleteById(Long id) {
        Mesa mesa = findById(id);
        if (mesa.getEstado() == EstadoMesa.OCUPADA) {
            throw new IllegalStateException("No se puede eliminar una mesa ocupada");
        }
        mesaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mesa> findByEstado(EstadoMesa estado) {
        return mesaRepository.findByEstadoOrderByNumeroAsc(estado);
    }

    @Override
    public Mesa cambiarEstado(Long id, EstadoMesa nuevoEstado) {
        Mesa mesa = findById(id);
        mesa.setEstado(nuevoEstado);
        return mesaRepository.save(mesa);
    }
}
