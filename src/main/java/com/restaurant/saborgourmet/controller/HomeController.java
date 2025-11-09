package com.restaurant.saborgourmet.controller;

import com.restaurant.saborgourmet.model.enums.EstadoPedido;
import com.restaurant.saborgourmet.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PedidoService pedidoService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("usuario", authentication.getName());
            model.addAttribute("rol", authentication.getAuthorities());

            // Estadísticas rápidas
            Long pedidosPendientes = pedidoService.countByEstado(EstadoPedido.PENDIENTE);
            Long pedidosEnPreparacion = pedidoService.countByEstado(EstadoPedido.EN_PREPARACION);
            Long pedidosServidos = pedidoService.countByEstado(EstadoPedido.SERVIDO);

            model.addAttribute("pedidosPendientes", pedidosPendientes);
            model.addAttribute("pedidosEnPreparacion", pedidosEnPreparacion);
            model.addAttribute("pedidosServidos", pedidosServidos);
        }

        return "index";
    }
}
