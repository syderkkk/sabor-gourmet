# Sistema de Gestión de Restaurante "Sabor Gourmet"

Sistema de gestión integral para restaurantes desarrollado con Spring Boot 3+, implementando AOP y Spring Security.

## 📋 Descripción

Sistema empresarial que permite gestionar las operaciones de un restaurante, incluyendo:
- Control de pedidos
- Gestión de mesas
- Administración de clientes
- Catálogo de platos
- Auditoría con AOP
- Seguridad basada en roles

## 🛠️ Tecnologías Utilizadas

- **Backend:**
    - Spring Boot 3.2.0
    - Spring Data JPA
    - Spring Security
    - Spring AOP
    - MySQL 8.0

- **Frontend:**
    - Thymeleaf
    - Bootstrap 5.3.2
    - JavaScript ES6+

- **Herramientas:**
    - Maven
    - Lombok
    - BCrypt (cifrado de contraseñas)

## 📦 Estructura del Proyecto

```
sabor-gourmet/
├── src/main/java/com/restaurant/saborgourmet/
│ ├── config/ # Configuraciones (Security, AOP)
│ ├── model/ # Entidades JPA
│ ├── repository/ # Repositorios Spring Data
│ ├── service/ # Lógica de negocio
│ ├── controller/ # Controladores MVC
│ ├── aspect/ # Aspectos AOP
│ ├── dto/ # Data Transfer Objects
│ └── exception/ # Manejo de excepciones
├── src/main/resources/
│ ├── templates/ # Vistas Thymeleaf
│ ├── static/ # CSS, JS, imágenes
│ └── application.properties
└── pom.xml
```

## ⚙️ Instalación y Configuración

### Prerrequisitos

- JDK 17 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior

### Pasos de Instalación

1. **Clonar el repositorio:**
   git clone <url-repositorio>
   cd sabor-gourmet

text

2. **Configurar la base de datos:**

Crear la base de datos en MySQL:
CREATE DATABASE sabor_gourmet CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

text

3. **Configurar application.properties:**

Editar `src/main/resources/application.properties`:
spring.datasource.url=jdbc:mysql://localhost:3306/sabor_gourmet
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

text

4. **Compilar el proyecto:**
   mvn clean install

text

5. **Ejecutar la aplicación:**
   mvn spring-boot:run

text

6. **Acceder a la aplicación:**

Abrir el navegador en: `http://localhost:8080`

## 👥 Usuarios de Prueba

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin | admin123 | ADMIN |
| mozo | mozo123 | MOZO |
| cocinero | cocinero123 | COCINERO |
| cajero | cajero123 | CAJERO |

## 🔐 Seguridad (Spring Security)

### Roles y Permisos

- **ADMIN**: Acceso total al sistema
- **MOZO**: Gestión de pedidos y mesas
- **COCINERO**: Vista de cocina y cambio de estado de pedidos
- **CAJERO**: Gestión de ventas y facturación

### Rutas Protegidas

- `/admin/**` → Solo ADMIN
- `/pedidos/**` → MOZO, COCINERO, ADMIN
- `/ventas/**` → CAJERO, ADMIN
- `/inventario/**` → ADMIN

## 🎯 Aspectos AOP Implementados

### 1. Aspecto de Auditoría
- Registra todas las operaciones CRUD en la tabla `bitacora`
- Captura: usuario, acción, entidad, IP, fecha/hora
- Métodos auditados: crear, actualizar, eliminar

### 2. Aspecto de Logging de Excepciones
- Registra errores y excepciones en el sistema
- Facilita debugging y monitoreo

## 📊 Módulo Implementado: Gestión de Pedidos

### Funcionalidades

1. **Crear Pedido:**
    - Seleccionar mesa
    - Agregar cliente (opcional)
    - Añadir platos con cantidades
    - Observaciones especiales

2. **Listar Pedidos:**
    - Ver todos los pedidos
    - Filtrar por estado
    - Información resumida

3. **Detalle de Pedido:**
    - Información completa
    - Lista de platos
    - Total calculado
    - Acciones rápidas

4. **Editar Pedido:**
    - Modificar observaciones
    - Agregar/eliminar platos

5. **Cambiar Estado:**
    - Pendiente → En Preparación
    - En Preparación → Servido
    - Servido → Cerrado

6. **Vista de Cocina:**
    - Pedidos pendientes
    - Pedidos en preparación
    - Actualización automática

## 🗄️ Modelo de Datos

### Entidades Principales

- **Pedido**: Registro de pedidos
- **DetallePedido**: Platos por pedido
- **Cliente**: Información de clientes
- **Mesa**: Control de mesas
- **Plato**: Catálogo del menú
- **Usuario**: Usuarios del sistema
- **Bitacora**: Auditoría de acciones

## 🚀 Despliegue

### Compilar JAR

mvn clean package

text

### Ejecutar JAR

java -jar target/sabor-gourmet-1.0.0.jar

text

### Variables de Entorno (Producción)

export DB_URL=jdbc:mysql://host:3306/sabor_gourmet
export DB_USER=usuario
export DB_PASSWORD=contraseña
export SERVER_PORT=8080

text

## 📝 Requerimientos Cumplidos

### Técnicos
✅ Spring Boot 3+  
✅ Patrón MVC  
✅ Thymeleaf + Bootstrap 5  
✅ Spring Data JPA + MySQL  
✅ Spring Security con roles  
✅ AOP (Auditoría)

### Funcionales
✅ CRUD completo del módulo  
✅ Gestión de pedidos  
✅ Control de estados  
✅ Interfaz responsive  
✅ Auditoría de acciones

### No Funcionales
✅ Contraseñas cifradas (BCrypt)  
✅ Autenticación requerida  
✅ Bitácora de acciones  
✅ Interfaz intuitiva  
✅ Idioma español  
✅ Arquitectura modular

## 📄 Licencia

Este proyecto es de uso académico.

## 👨‍💻 Autor

**Italo Andre Mendoza Yampi**  
Ciclo IV - Desarrollo de Aplicaciones Web

---

**Fecha:** Noviembre 2025