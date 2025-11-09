package com.restaurant.saborgourmet.repository;

import com.restaurant.saborgourmet.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {

    List<Plato> findByEstado(Boolean estado);

    List<Plato> findByTipoAndEstado(String tipo, Boolean estado);

    List<Plato> findByNombreContainingIgnoreCaseAndEstado(String nombre, Boolean estado);
}
