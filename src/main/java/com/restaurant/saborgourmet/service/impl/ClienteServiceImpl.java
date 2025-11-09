package com.restaurant.saborgourmet.service.impl;

import com.restaurant.saborgourmet.exception.ResourceNotFoundException;
import com.restaurant.saborgourmet.model.Cliente;
import com.restaurant.saborgourmet.repository.ClienteRepository;
import com.restaurant.saborgourmet.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findByEstado(Boolean estado) {
        return clienteRepository.findByEstado(estado);
    }
}
