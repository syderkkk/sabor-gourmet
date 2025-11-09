package com.restaurant.saborgourmet.aspect;

import com.restaurant.saborgourmet.model.Bitacora;
import com.restaurant.saborgourmet.model.Usuario;
import com.restaurant.saborgourmet.repository.BitacoraRepository;
import com.restaurant.saborgourmet.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Aspecto de Auditoría
 * Registra en base de datos cada acción realizada (crear, actualizar, eliminar)
 * de las tablas del módulo de Pedidos
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditoriaAspect {

    private final BitacoraRepository bitacoraRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Pointcut para métodos de creación en el servicio de Pedidos
     */
    @Pointcut("execution(* com.restaurant.saborgourmet.service.impl.PedidoServiceImpl.crear(..))")
    public void crearPedido() {}

    /**
     * Pointcut para métodos de actualización en el servicio de Pedidos
     */
    @Pointcut("execution(* com.restaurant.saborgourmet.service.impl.PedidoServiceImpl.actualizar(..)) || " +
            "execution(* com.restaurant.saborgourmet.service.impl.PedidoServiceImpl.cambiarEstado(..))")
    public void actualizarPedido() {}

    /**
     * Pointcut para métodos de eliminación en el servicio de Pedidos
     */
    @Pointcut("execution(* com.restaurant.saborgourmet.service.impl.PedidoServiceImpl.deleteById(..))")
    public void eliminarPedido() {}

    /**
     * After Returning para operaciones de creación
     */
    @AfterReturning(pointcut = "crearPedido()", returning = "result")
    public void auditarCreacion(JoinPoint joinPoint, Object result) {
        try {
            String nombreMetodo = joinPoint.getSignature().getName();
            String entidad = "Pedido";
            Long idEntidad = extractId(result);
            String detalle = String.format("Se creó un nuevo pedido con ID: %d", idEntidad);

            registrarAuditoria("CREAR", entidad, idEntidad, detalle, joinPoint);

            log.info("Auditoría registrada - CREAR {}: {}", entidad, idEntidad);
        } catch (Exception e) {
            log.error("Error al registrar auditoría de creación", e);
        }
    }

    /**
     * After Returning para operaciones de actualización
     */
    @AfterReturning(pointcut = "actualizarPedido()", returning = "result")
    public void auditarActualizacion(JoinPoint joinPoint, Object result) {
        try {
            String nombreMetodo = joinPoint.getSignature().getName();
            String entidad = "Pedido";
            Long idEntidad = extractId(result);

            Object[] args = joinPoint.getArgs();
            String detalle = String.format("Se actualizó el pedido con ID: %d. Método: %s",
                    idEntidad, nombreMetodo);

            registrarAuditoria("ACTUALIZAR", entidad, idEntidad, detalle, joinPoint);

            log.info("Auditoría registrada - ACTUALIZAR {}: {}", entidad, idEntidad);
        } catch (Exception e) {
            log.error("Error al registrar auditoría de actualización", e);
        }
    }

    /**
     * Before para operaciones de eliminación (necesitamos capturar antes de eliminar)
     */
    @Before("eliminarPedido()")
    public void auditarEliminacion(JoinPoint joinPoint) {
        try {
            String entidad = "Pedido";
            Object[] args = joinPoint.getArgs();
            Long idEntidad = (Long) args[0];

            String detalle = String.format("Se eliminó el pedido con ID: %d", idEntidad);

            registrarAuditoria("ELIMINAR", entidad, idEntidad, detalle, joinPoint);

            log.info("Auditoría registrada - ELIMINAR {}: {}", entidad, idEntidad);
        } catch (Exception e) {
            log.error("Error al registrar auditoría de eliminación", e);
        }
    }

    /**
     * Método auxiliar para registrar en la bitácora
     */
    private void registrarAuditoria(String accion, String entidad, Long idEntidad,
                                    String detalle, JoinPoint joinPoint) {
        try {
            Bitacora bitacora = new Bitacora();

            // Obtener usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String nombreUsuario = authentication.getName();
                Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                        .orElse(null);
                bitacora.setUsuario(usuario);
            }

            // Obtener IP del usuario
            String ipAddress = obtenerIpAddress();

            // Construir acción completa
            String accionCompleta = String.format("%s %s", accion, entidad);

            bitacora.setAccion(accionCompleta);
            bitacora.setEntidad(entidad);
            bitacora.setIdEntidad(idEntidad);
            bitacora.setDetalle(detalle);
            bitacora.setIpAddress(ipAddress);
            bitacora.setFechaHora(LocalDateTime.now());

            bitacoraRepository.save(bitacora);

        } catch (Exception e) {
            log.error("Error al guardar en bitácora", e);
        }
    }

    /**
     * Extrae el ID de la entidad del objeto resultado
     */
    private Long extractId(Object result) {
        if (result == null) return null;

        try {
            // Buscar método getId o getIdPedido
            Method[] methods = result.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals("getIdPedido") || method.getName().equals("getId")) {
                    return (Long) method.invoke(result);
                }
            }
        } catch (Exception e) {
            log.error("Error al extraer ID", e);
        }

        return null;
    }

    /**
     * Obtiene la dirección IP del cliente
     */
    private String obtenerIpAddress() {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ip = request.getHeader("X-Forwarded-For");

                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }

                return ip;
            }
        } catch (Exception e) {
            log.error("Error al obtener IP", e);
        }

        return "unknown";
    }

    /**
     * Aspecto para logging de excepciones
     */
    @AfterThrowing(pointcut = "execution(* com.restaurant.saborgourmet.service.impl.PedidoServiceImpl.*(..))",
            throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        String nombreMetodo = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.error("Excepción en {}.{}: {}", className, nombreMetodo, ex.getMessage());

        // Registrar en bitácora
        try {
            Bitacora bitacora = new Bitacora();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String nombreUsuario = authentication.getName();
                Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario).orElse(null);
                bitacora.setUsuario(usuario);
            }

            bitacora.setAccion("ERROR");
            bitacora.setEntidad("Sistema");
            bitacora.setDetalle(String.format("Error en %s.%s: %s", className, nombreMetodo, ex.getMessage()));
            bitacora.setIpAddress(obtenerIpAddress());

            bitacoraRepository.save(bitacora);
        } catch (Exception e) {
            log.error("Error al registrar excepción en bitácora", e);
        }
    }
}
