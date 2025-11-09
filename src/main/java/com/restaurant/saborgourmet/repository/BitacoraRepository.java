package com.restaurant.saborgourmet.repository;

import com.restaurant.saborgourmet.model.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {

    List<Bitacora> findByUsuarioIdUsuario(Long idUsuario);

    List<Bitacora> findByEntidadAndIdEntidad(String entidad, Long idEntidad);

    @Query("SELECT b FROM Bitacora b WHERE b.fechaHora BETWEEN :inicio AND :fin ORDER BY b.fechaHora DESC")
    List<Bitacora> findByFechaHoraBetween(@Param("inicio") LocalDateTime inicio,
                                          @Param("fin") LocalDateTime fin);

    List<Bitacora> findTop100ByOrderByFechaHoraDesc();
}
