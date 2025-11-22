package com.concurrente.notificaciones.controller;

import com.concurrente.notificaciones.model.Notificacion;
import com.concurrente.notificaciones.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService service;

    @Autowired
    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    /**
     * Vista principal de notificaciones para un usuario
     */
    @GetMapping("/{usuario}")
    public String verNotificaciones(@PathVariable String usuario, Model model) {
        model.addAttribute("usuario", usuario);
        model.addAttribute("tipos", new String[]{"INFO", "ALERTA", "URGENTE"});
        return "notificaciones";
    }

    /**
     * Endpoint SSE para recibir notificaciones en tiempo real
     */
    @GetMapping(value = "/sse/{usuario}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<Notificacion> streamNotificaciones(@PathVariable String usuario) {
        return service.getNotificacionesEnTiempoReal(usuario)
                .delayElements(Duration.ofMillis(500)) // Pequeño delay para evitar sobrecarga
                .doOnError(error -> System.err.println("Error en SSE: " + error.getMessage()));
    }

    /**
     * Obtener todas las notificaciones de un usuario (JSON)
     */
    @GetMapping("/api/{usuario}")
    @ResponseBody
    public Flux<Notificacion> getNotificaciones(@PathVariable String usuario) {
        return service.getNotificacionesPorUsuario(usuario);
    }

    /**
     * Crear una nueva notificación
     */
    @PostMapping("/api")
    @ResponseBody
    public Mono<Notificacion> crearNotificacion(@RequestBody Notificacion notificacion) {
        return service.addNotificacion(notificacion);
    }

    /**
     * Endpoint para crear notificación desde formulario
     */
    @PostMapping("/crear")
    public Mono<String> crearNotificacionForm(
            @RequestParam String usuario,
            @RequestParam String mensaje,
            @RequestParam String tipo) {
        Notificacion notificacion = new Notificacion(usuario, mensaje, tipo);
        return service.addNotificacion(notificacion)
                .then(Mono.just("redirect:/notificaciones/" + usuario));
    }

    /**
     * Marcar notificación como leída
     */
    @PutMapping("/api/{id}/leer")
    @ResponseBody
    public Mono<Notificacion> marcarLeido(@PathVariable String id) {
        return service.marcarLeido(id);
    }

    /**
     * Endpoint para marcar como leída desde la vista
     */
    @PostMapping("/{id}/leer")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, String>>> marcarLeidoForm(@PathVariable String id, @RequestParam String usuario) {
        return service.marcarLeido(id)
                .map(notificacion -> ResponseEntity.ok().body(Map.of("status", "success", "leido", String.valueOf(notificacion.getLeido()))))
                .onErrorResume(error -> {
                    System.err.println("Error al marcar como leído " + id + ": " + error.getMessage());
                    error.printStackTrace();
                    return Mono.just(ResponseEntity.status(500).body(Map.of("status", "error", "message", error.getMessage())));
                });
    }

    /**
     * Filtrar notificaciones por tipo
     */
    @GetMapping("/api/{usuario}/filtrar")
    @ResponseBody
    public Flux<Notificacion> filtrarPorTipo(
            @PathVariable String usuario,
            @RequestParam String tipo) {
        return service.filtrarPorTipo(usuario, tipo);
    }

    /**
     * Eliminar notificación
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public Mono<Void> eliminarNotificacion(@PathVariable String id) {
        return service.eliminarNotificacion(id);
    }

    /**
     * Endpoint para eliminar desde la vista
     */
    @PostMapping("/{id}/eliminar")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, String>>> eliminarNotificacionForm(@PathVariable String id, @RequestParam String usuario) {
        System.out.println("Intentando eliminar notificación con ID: " + id);
        return service.eliminarNotificacion(id)
                .then(Mono.just(ResponseEntity.ok().body(Map.of("status", "success"))))
                .onErrorResume(error -> {
                    System.err.println("Error al eliminar notificación " + id + ": " + error.getMessage());
                    error.printStackTrace();
                    return Mono.just(ResponseEntity.status(500).body(Map.of("status", "error", "message", error.getMessage())));
                });
    }

    /**
     * Endpoint de prueba para generar notificaciones de ejemplo
     */
    @GetMapping("/generar-ejemplo/{usuario}")
    public Mono<String> generarEjemplo(@PathVariable String usuario) {
        Notificacion n1 = new Notificacion(usuario, "Pedido enviado", "INFO");
        Notificacion n2 = new Notificacion(usuario, "Pago rechazado", "ALERTA");
        Notificacion n3 = new Notificacion(usuario, "Servidor caído", "URGENTE");
        
        return service.addNotificacion(n1)
                .then(service.addNotificacion(n2))
                .then(service.addNotificacion(n3))
                .then(Mono.just("redirect:/notificaciones/" + usuario));
    }
}

