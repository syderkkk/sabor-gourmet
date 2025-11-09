package com.restaurant.saborgourmet.controller;

import com.restaurant.saborgourmet.dto.DetallePedidoDTO;
import com.restaurant.saborgourmet.dto.PedidoDTO;
import com.restaurant.saborgourmet.model.Pedido;
import com.restaurant.saborgourmet.model.enums.EstadoPedido;
import com.restaurant.saborgourmet.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('MOZO', 'COCINERO', 'ADMIN')")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final MesaService mesaService;
    private final PlatoService platoService;

    /**
     * Lista todos los pedidos
     */
    @GetMapping
    public String listar(@RequestParam(value = "estado", required = false) String estado,
                         Model model) {
        List<Pedido> pedidos;

        if (estado != null && !estado.isEmpty()) {
            try {
                EstadoPedido estadoPedido = EstadoPedido.valueOf(estado);
                pedidos = pedidoService.findByEstado(estadoPedido);
                model.addAttribute("estadoFiltro", estado);
            } catch (IllegalArgumentException e) {
                pedidos = pedidoService.findAll();
            }
        } else {
            pedidos = pedidoService.findAll();
        }

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("estados", EstadoPedido.values());
        model.addAttribute("titulo", "Gestión de Pedidos");

        return "pedidos/lista";
    }

    /**
     * Muestra el formulario para crear un nuevo pedido
     */
    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('MOZO', 'ADMIN')")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("pedidoDTO", new PedidoDTO());
        model.addAttribute("clientes", clienteService.findByEstado(true));
        model.addAttribute("mesas", mesaService.findAll());
        model.addAttribute("platos", platoService.findByEstado(true));
        model.addAttribute("titulo", "Nuevo Pedido");

        return "pedidos/crear";
    }

    /**
     * Procesa la creación de un nuevo pedido
     */
    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('MOZO', 'ADMIN')")
    public String guardar(@Valid @ModelAttribute PedidoDTO pedidoDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findByEstado(true));
            model.addAttribute("mesas", mesaService.findAll());
            model.addAttribute("platos", platoService.findByEstado(true));
            model.addAttribute("titulo", "Nuevo Pedido");
            return "pedidos/crear";
        }

        try {
            Pedido pedido = pedidoService.crear(pedidoDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Pedido #" + pedido.getIdPedido() + " creado exitosamente");
            return "redirect:/pedidos/" + pedido.getIdPedido();
        } catch (Exception e) {
            log.error("Error al crear pedido", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al crear el pedido: " + e.getMessage());
            return "redirect:/pedidos/nuevo";
        }
    }

    /**
     * Muestra los detalles de un pedido
     */
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        try {
            Pedido pedido = pedidoService.findById(id);
            model.addAttribute("pedido", pedido);
            model.addAttribute("estados", EstadoPedido.values());
            model.addAttribute("titulo", "Detalle del Pedido #" + id);
            return "pedidos/detalle";
        } catch (Exception e) {
            log.error("Error al obtener pedido", e);
            return "redirect:/pedidos";
        }
    }

    /**
     * Muestra el formulario para editar un pedido
     */
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('MOZO', 'ADMIN')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Pedido pedido = pedidoService.findById(id);

            if (pedido.getEstado() == EstadoPedido.CERRADO) {
                model.addAttribute("error", "No se puede editar un pedido cerrado");
                return "redirect:/pedidos/" + id;
            }

            PedidoDTO pedidoDTO = new PedidoDTO();
            pedidoDTO.setIdMesa(pedido.getMesa().getIdMesa());
            if (pedido.getCliente() != null) {
                pedidoDTO.setIdCliente(pedido.getCliente().getIdCliente());
            }
            pedidoDTO.setObservaciones(pedido.getObservaciones());

            model.addAttribute("pedido", pedido);
            model.addAttribute("pedidoDTO", pedidoDTO);
            model.addAttribute("clientes", clienteService.findByEstado(true));
            model.addAttribute("mesas", mesaService.findAll());
            model.addAttribute("platos", platoService.findByEstado(true));
            model.addAttribute("titulo", "Editar Pedido #" + id);

            return "pedidos/editar";
        } catch (Exception e) {
            log.error("Error al cargar formulario de edición", e);
            return "redirect:/pedidos";
        }
    }

    /**
     * Procesa la actualización de un pedido
     */
    @PostMapping("/{id}/actualizar")
    @PreAuthorize("hasAnyRole('MOZO', 'ADMIN')")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute PedidoDTO pedidoDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (result.hasErrors()) {
            Pedido pedido = pedidoService.findById(id);
            model.addAttribute("pedido", pedido);
            model.addAttribute("clientes", clienteService.findByEstado(true));
            model.addAttribute("mesas", mesaService.findAll());
            model.addAttribute("platos", platoService.findByEstado(true));
            model.addAttribute("titulo", "Editar Pedido #" + id);
            return "pedidos/editar";
        }

        try {
            Pedido pedido = pedidoService.actualizar(id, pedidoDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Pedido #" + pedido.getIdPedido() + " actualizado exitosamente");
            return "redirect:/pedidos/" + id;
        } catch (Exception e) {
            log.error("Error al actualizar pedido", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al actualizar el pedido: " + e.getMessage());
            return "redirect:/pedidos/" + id + "/editar";
        }
    }

    /**
     * Cambia el estado de un pedido
     */
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam EstadoPedido nuevoEstado,
                                RedirectAttributes redirectAttributes) {
        try {
            Pedido pedido = pedidoService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("success",
                    "Estado del pedido #" + id + " cambiado a " + nuevoEstado.getDisplayName());
            return "redirect:/pedidos/" + id;
        } catch (Exception e) {
            log.error("Error al cambiar estado del pedido", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al cambiar el estado: " + e.getMessage());
            return "redirect:/pedidos/" + id;
        }
    }

    /**
     * Agrega un detalle (plato) a un pedido existente
     */
    @PostMapping("/{id}/agregar-detalle")
    @PreAuthorize("hasAnyRole('MOZO', 'ADMIN')")
    public String agregarDetalle(@PathVariable Long id,
                                 @Valid @ModelAttribute DetallePedidoDTO detalleDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            pedidoService.addDetalle(id, detalleDTO);
            redirectAttributes.addFlashAttribute("success", "Plato agregado al pedido");
            return "redirect:/pedidos/" + id;
        } catch (Exception e) {
            log.error("Error al agregar detalle", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al agregar el plato: " + e.getMessage());
            return "redirect:/pedidos/" + id;
        }
    }

    /**
     * Elimina un detalle de un pedido
     */
    @PostMapping("/{id}/eliminar-detalle/{idDetalle}")
    @PreAuthorize("hasAnyRole('MOZO', 'ADMIN')")
    public String eliminarDetalle(@PathVariable Long id,
                                  @PathVariable Long idDetalle,
                                  RedirectAttributes redirectAttributes) {
        try {
            pedidoService.removeDetalle(id, idDetalle);
            redirectAttributes.addFlashAttribute("success", "Plato eliminado del pedido");
            return "redirect:/pedidos/" + id;
        } catch (Exception e) {
            log.error("Error al eliminar detalle", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al eliminar el plato: " + e.getMessage());
            return "redirect:/pedidos/" + id;
        }
    }

    /**
     * Elimina un pedido completo
     */
    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.deleteById(id);
            redirectAttributes.addFlashAttribute("success",
                    "Pedido #" + id + " eliminado exitosamente");
            return "redirect:/pedidos";
        } catch (Exception e) {
            log.error("Error al eliminar pedido", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al eliminar el pedido: " + e.getMessage());
            return "redirect:/pedidos/" + id;
        }
    }

    /**
     * Vista de cocina - Solo pedidos pendientes y en preparación
     */
    @GetMapping("/cocina")
    @PreAuthorize("hasAnyRole('COCINERO', 'ADMIN')")
    public String vistaCocina(Model model) {
        List<Pedido> pedidosPendientes = pedidoService.findByEstado(EstadoPedido.PENDIENTE);
        List<Pedido> pedidosEnPreparacion = pedidoService.findByEstado(EstadoPedido.EN_PREPARACION);

        model.addAttribute("pedidosPendientes", pedidosPendientes);
        model.addAttribute("pedidosEnPreparacion", pedidosEnPreparacion);
        model.addAttribute("titulo", "Vista de Cocina");

        return "pedidos/cocina";
    }
}
