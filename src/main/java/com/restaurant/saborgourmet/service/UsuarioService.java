package com.restaurant.saborgourmet.service;

import com.restaurant.saborgourmet.model.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UsuarioService extends UserDetailsService {
    List<Usuario> findAll();
    Usuario findById(Long id);
    Usuario save(Usuario usuario);
    void deleteById(Long id);
    Usuario findByNombreUsuario(String nombreUsuario);
}