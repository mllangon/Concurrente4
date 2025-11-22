# Sistema de Notificaciones en Tiempo Real con Spring WebFlux y MongoDB

## Miembros del Grupo
- [Nombre del estudiante 1]
- [Nombre del estudiante 2]
- [Nombre del estudiante 3] (si aplica)

## Descripción de la Solución

Este proyecto implementa un sistema de notificaciones en tiempo real utilizando **Spring WebFlux** para programación reactiva, **MongoDB Reactive** para el almacenamiento de datos, y **Server-Sent Events (SSE)** para la comunicación en tiempo real entre el servidor y el cliente. La interfaz de usuario está construida con **Thymeleaf** y JavaScript para mostrar las notificaciones de forma dinámica sin necesidad de recargar la página.

### Lógica de la Solución

La solución utiliza un patrón reactivo donde:

1. **Flujo de Datos Reactivo**: El servicio utiliza `Flux` y `Mono` de Project Reactor para manejar flujos de datos asíncronos y no bloqueantes.

2. **Server-Sent Events (SSE)**: El endpoint `/notificaciones/sse/{usuario}` mantiene una conexión persistente con el cliente, enviando notificaciones en tiempo real a medida que se crean.

3. **Sink Multicast**: Se utiliza un `Sinks.Many` para emitir nuevas notificaciones a todos los clientes conectados mediante SSE, permitiendo la distribución en tiempo real.

4. **MongoDB Reactive**: Todas las operaciones de base de datos son no bloqueantes, utilizando los drivers reactivos de MongoDB.

5. **Interfaz Dinámica**: La vista Thymeleaf se conecta al stream SSE mediante JavaScript, actualizando automáticamente la tabla de notificaciones cuando llegan nuevas.

## Archivos Relevantes

- **pom.xml**: Configuración de Maven con dependencias de Spring WebFlux, MongoDB Reactive y Thymeleaf.

- **NotificacionesApplication.java**: Clase principal de Spring Boot que inicia la aplicación.

- **Notificacion.java**: Entidad/modelo que representa una notificación con atributos: id, usuario, mensaje, tipo, fecha y leido.

- **NotificacionRepository.java**: Interfaz ReactiveMongoRepository que proporciona métodos reactivos para consultar notificaciones por usuario y tipo.

- **NotificacionService.java**: Servicio reactivo que implementa la lógica de negocio: flujo SSE en tiempo real, creación, marcado como leído, filtrado y eliminación de notificaciones.

- **NotificacionController.java**: Controlador WebFlux que expone endpoints REST y SSE, además de la vista Thymeleaf para la interfaz de usuario.

- **notificaciones.html**: Vista Thymeleaf con JavaScript que se conecta al stream SSE, muestra notificaciones en tiempo real, permite filtrar por tipo, marcar como leído y eliminar notificaciones.

- **application.properties**: Configuración de la aplicación: puerto del servidor, conexión a MongoDB (localhost:27017, base de datos "notificaciones") y configuración de Thymeleaf.

## Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- MongoDB 4.4+ ejecutándose en localhost:27017

## Instrucciones de Ejecución

1. **Iniciar MongoDB**:
   ```bash
   mongod
   ```

2. **Compilar el proyecto**:
   ```bash
   mvn clean install
   ```

3. **Ejecutar la aplicación**:
   ```bash
   mvn spring-boot:run
   ```

4. **Acceder a la aplicación**:
   - Abrir el navegador en: `http://localhost:8080/notificaciones/usuario1`
   - Hacer clic en "Generar Notificaciones de Ejemplo" para crear datos de prueba
   - Las notificaciones aparecerán automáticamente en tiempo real gracias a SSE

## Endpoints Disponibles

- `GET /notificaciones/{usuario}` - Vista principal de notificaciones
- `GET /notificaciones/sse/{usuario}` - Stream SSE para notificaciones en tiempo real
- `GET /notificaciones/api/{usuario}` - Obtener todas las notificaciones de un usuario (JSON)
- `POST /notificaciones/api` - Crear nueva notificación (JSON)
- `POST /notificaciones/crear` - Crear notificación desde formulario
- `PUT /notificaciones/api/{id}/leer` - Marcar notificación como leída (JSON)
- `POST /notificaciones/{id}/leer` - Marcar como leída desde vista
- `GET /notificaciones/api/{usuario}/filtrar?tipo={tipo}` - Filtrar notificaciones por tipo
- `DELETE /notificaciones/api/{id}` - Eliminar notificación (JSON)
- `POST /notificaciones/{id}/eliminar` - Eliminar desde vista
- `POST /notificaciones/generar-ejemplo/{usuario}` - Generar notificaciones de ejemplo

## Características Implementadas

✅ Registro de nuevas notificaciones en MongoDB  
✅ Visualización de notificaciones en tiempo real usando SSE  
✅ Marcado de notificaciones como leídas  
✅ Filtrado de notificaciones por tipo (INFO, ALERTA, URGENTE)  
✅ Eliminación de notificaciones  
✅ Interfaz de usuario moderna y responsiva  
✅ Resaltado visual de notificaciones no leídas  
✅ Actualización automática sin recargar la página  
