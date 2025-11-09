package com.restaurant.saborgourmet.repository;

import com.restaurant.saborgourmet.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDni(String dni);

    List<Cliente> findByEstado(Boolean estado);

    List<Cliente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombres, String apellidos);

    boolean existsByDni(String dni);
}
