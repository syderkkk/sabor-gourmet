package com.restaurant.saborgourmet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ventas")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CAJERO', 'ADMIN')")
public class VentasController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("titulo", "Ventas");
        return "ventas/index";
    }
}
