package com.restaurant.saborgourmet;

import com.restaurant.saborgourmet.model.*;
import com.restaurant.saborgourmet.model.enums.EstadoMesa;
import com.restaurant.saborgourmet.model.enums.RolUsuario;
import com.restaurant.saborgourmet.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class SaborgourmetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaborgourmetApplication.class, args);
        log.info("===========================================");
        log.info("Sistema Sabor Gourmet iniciado exitosamente");
        log.info("===========================================");
    }

    /**
     * Inicializa datos de prueba en la base de datos
     */
    @Bean
    CommandLineRunner initData(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            MesaRepository mesaRepository,
            PlatoRepository platoRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            log.info("Iniciando carga de datos de prueba...");

            // Crear usuarios si no existen
            if (usuarioRepository.count() == 0) {
                log.info("Creando usuarios de prueba...");

                Usuario admin = new Usuario();
                admin.setNombreUsuario("admin");
                admin.setContrasena(passwordEncoder.encode("admin123"));
                admin.setRol(RolUsuario.ADMIN);
                admin.setNombreCompleto("Administrador del Sistema");
                admin.setEstado(true);
                usuarioRepository.save(admin);

                Usuario mozo = new Usuario();
                mozo.setNombreUsuario("mozo");
                mozo.setContrasena(passwordEncoder.encode("mozo123"));
                mozo.setRol(RolUsuario.MOZO);
                mozo.setNombreCompleto("Juan Pérez - Mozo");
                mozo.setEstado(true);
                usuarioRepository.save(mozo);

                Usuario cocinero = new Usuario();
                cocinero.setNombreUsuario("cocinero");
                cocinero.setContrasena(passwordEncoder.encode("cocinero123"));
                cocinero.setRol(RolUsuario.COCINERO);
                cocinero.setNombreCompleto("María García - Cocinera");
                cocinero.setEstado(true);
                usuarioRepository.save(cocinero);

                Usuario cajero = new Usuario();
                cajero.setNombreUsuario("cajero");
                cajero.setContrasena(passwordEncoder.encode("cajero123"));
                cajero.setRol(RolUsuario.CAJERO);
                cajero.setNombreCompleto("Carlos López - Cajero");
                cajero.setEstado(true);
                usuarioRepository.save(cajero);

                log.info("Usuarios creados: admin, mozo, cocinero, cajero");
            }

            // Crear clientes de prueba
            if (clienteRepository.count() == 0) {
                log.info("Creando clientes de prueba...");

                Cliente cliente1 = new Cliente();
                cliente1.setDni("12345678");
                cliente1.setNombres("Pedro");
                cliente1.setApellidos("Rodríguez Silva");
                cliente1.setTelefono("987654321");
                cliente1.setCorreo("pedro.rodriguez@email.com");
                cliente1.setEstado(true);
                clienteRepository.save(cliente1);

                Cliente cliente2 = new Cliente();
                cliente2.setDni("87654321");
                cliente2.setNombres("Ana");
                cliente2.setApellidos("Martínez López");
                cliente2.setTelefono("912345678");
                cliente2.setCorreo("ana.martinez@email.com");
                cliente2.setEstado(true);
                clienteRepository.save(cliente2);

                Cliente cliente3 = new Cliente();
                cliente3.setDni("11223344");
                cliente3.setNombres("Luis");
                cliente3.setApellidos("Fernández Torres");
                cliente3.setTelefono("923456789");
                cliente3.setCorreo("luis.fernandez@email.com");
                cliente3.setEstado(true);
                clienteRepository.save(cliente3);

                log.info("Clientes creados exitosamente");
            }

            // Crear mesas
            if (mesaRepository.count() == 0) {
                log.info("Creando mesas...");

                for (int i = 1; i <= 10; i++) {
                    Mesa mesa = new Mesa();
                    mesa.setNumero(i);
                    mesa.setCapacidad(i <= 5 ? 4 : 6);
                    mesa.setEstado(EstadoMesa.DISPONIBLE);
                    mesaRepository.save(mesa);
                }

                log.info("10 mesas creadas exitosamente");
            }

            // Crear platos del menú
            if (platoRepository.count() == 0) {
                log.info("Creando menú de platos...");

                // Entradas
                crearPlato(platoRepository, "Causa Limeña", "entrada",
                        new BigDecimal("15.00"), "Causa rellena de pollo con palta");
                crearPlato(platoRepository, "Ceviche de Pescado", "entrada",
                        new BigDecimal("25.00"), "Ceviche clásico con camote y choclo");
                crearPlato(platoRepository, "Tequeños", "entrada",
                        new BigDecimal("12.00"), "Tequeños de queso con salsa");

                // Platos de Fondo
                crearPlato(platoRepository, "Lomo Saltado", "fondo",
                        new BigDecimal("32.00"), "Lomo saltado con papas y arroz");
                crearPlato(platoRepository, "Ají de Gallina", "fondo",
                        new BigDecimal("28.00"), "Ají de gallina con papa y arroz");
                crearPlato(platoRepository, "Arroz con Mariscos", "fondo",
                        new BigDecimal("35.00"), "Arroz con mariscos frescos");
                crearPlato(platoRepository, "Tallarines a la Huancaína", "fondo",
                        new BigDecimal("26.00"), "Tallarines con salsa huancaína y bistec");
                crearPlato(platoRepository, "Pollo a la Brasa", "fondo",
                        new BigDecimal("30.00"), "1/4 de pollo con papas y ensalada");

                // Postres
                crearPlato(platoRepository, "Suspiro Limeño", "postre",
                        new BigDecimal("10.00"), "Tradicional suspiro a la limeña");
                crearPlato(platoRepository, "Mazamorra Morada", "postre",
                        new BigDecimal("8.00"), "Mazamorra morada con arroz con leche");
                crearPlato(platoRepository, "Picarones", "postre",
                        new BigDecimal("9.00"), "Picarones con miel de chancaca");

                // Bebidas
                crearPlato(platoRepository, "Chicha Morada", "bebida",
                        new BigDecimal("6.00"), "Chicha morada natural");
                crearPlato(platoRepository, "Inca Kola", "bebida",
                        new BigDecimal("5.00"), "Inca Kola 500ml");
                crearPlato(platoRepository, "Jugo de Papaya", "bebida",
                        new BigDecimal("7.00"), "Jugo natural de papaya");
                crearPlato(platoRepository, "Pisco Sour", "bebida",
                        new BigDecimal("18.00"), "Pisco sour clásico");

                log.info("Menú de platos creado exitosamente");
            }

            log.info("Inicialización de datos completada");
            log.info("===========================================");
            log.info("Usuarios disponibles:");
            log.info("  - admin/admin123 (Administrador)");
            log.info("  - mozo/mozo123 (Mozo)");
            log.info("  - cocinero/cocinero123 (Cocinero)");
            log.info("  - cajero/cajero123 (Cajero)");
            log.info("===========================================");
        };
    }

    private void crearPlato(PlatoRepository repository, String nombre, String tipo,
                            BigDecimal precio, String descripcion) {
        Plato plato = new Plato();
        plato.setNombre(nombre);
        plato.setTipo(tipo);
        plato.setPrecio(precio);
        plato.setDescripcion(descripcion);
        plato.setEstado(true);
        repository.save(plato);
    }
}
