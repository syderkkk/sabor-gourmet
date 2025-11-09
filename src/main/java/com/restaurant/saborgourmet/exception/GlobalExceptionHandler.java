package com.restaurant.saborgourmet.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex,
                                                  RedirectAttributes redirectAttributes) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/pedidos";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException ex,
                                              RedirectAttributes redirectAttributes) {
        log.error("Estado ilegal: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/pedidos";
    }

    // Ignorar el error del favicon.ico
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(NoResourceFoundException ex) {
        // Solo registrar si NO es favicon
        if (!ex.getMessage().contains("favicon.ico")) {
            log.warn("Recurso estático no encontrado: {}", ex.getMessage());
        }
        // Retornar null para que Spring maneje silenciosamente el error
        return null;
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        log.error("Error no controlado: ", ex);
        model.addAttribute("error", "Ha ocurrido un error inesperado. Por favor, contacte al administrador.");
        return "error";
    }
}
