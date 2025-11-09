package com.restaurant.saborgourmet.repository;

import com.restaurant.saborgourmet.model.Mesa;
import com.restaurant.saborgourmet.model.enums.EstadoMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    List<Mesa> findByEstado(EstadoMesa estado);

    Optional<Mesa> findByNumero(Integer numero);

    boolean existsByNumero(Integer numero);

    List<Mesa> findByEstadoOrderByNumeroAsc(EstadoMesa estado);
}
