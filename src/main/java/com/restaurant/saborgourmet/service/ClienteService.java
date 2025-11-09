package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.model.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> findAll();
    Cliente findById(Long id);
    Cliente save(Cliente cliente);
    void deleteById(Long id);
    List<Cliente> findByEstado(Boolean estado);
}

