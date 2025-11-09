package com.restaurant.saborgourmet.repository;

import com.restaurant.saborgourmet.model.Usuario;
import com.restaurant.saborgourmet.model.enums.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    List<Usuario> findByEstado(Boolean estado);

    List<Usuario> findByRol(RolUsuario rol);

    boolean existsByNombreUsuario(String nombreUsuario);
}
