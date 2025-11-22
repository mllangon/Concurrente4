package com.concurrente.notificaciones.service;

import com.concurrente.notificaciones.model.Notificacion;
import com.concurrente.notificaciones.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Date;

@Service
public class NotificacionService {

    private final NotificacionRepository repository;
    private final Sinks.Many<Notificacion> notificationSink;

    @Autowired
    public NotificacionService(NotificacionRepository repository) {
        this.repository = repository;
        this.notificationSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    /**
     * Obtiene un flujo infinito de notificaciones en tiempo real para un usuario usando SSE
     * Solo envía notificaciones NUEVAS, no las existentes (estas se cargan por el endpoint REST)
     */
    public Flux<Notificacion> getNotificacionesEnTiempoReal(String usuario) {
        // Solo escuchamos nuevas notificaciones en tiempo real
        // Las notificaciones existentes se cargan una sola vez por el endpoint /api/{usuario}
        return notificationSink
                .asFlux()
                .filter(n -> n.getUsuario().equals(usuario))
                .delayElements(Duration.ofMillis(100)); // Pequeño delay para evitar problemas de concurrencia
    }

    /**
     * Agrega una nueva notificación
     */
    public Mono<Notificacion> addNotificacion(Notificacion notificacion) {
        if (notificacion.getFecha() == null) {
            notificacion.setFecha(new Date());
        }
        if (notificacion.getLeido() == null) {
            notificacion.setLeido(false);
        }
        
        return repository.save(notificacion)
                .doOnNext(saved -> {
                    // Emitir la notificación al sink para SSE
                    notificationSink.tryEmitNext(saved);
                });
    }

    /**
     * Marca una notificación como leída
     */
    public Mono<Notificacion> marcarLeido(String id) {
        return repository.findById(id)
                .flatMap(notificacion -> {
                    notificacion.setLeido(true);
                    return repository.save(notificacion)
                            .doOnNext(saved -> {
                                // Emitir la actualización al sink para SSE
                                notificationSink.tryEmitNext(saved);
                            });
                });
    }

    /**
     * Filtra notificaciones por tipo para un usuario
     */
    public Flux<Notificacion> filtrarPorTipo(String usuario, String tipo) {
        return repository.findByUsuarioAndTipo(usuario, tipo)
                .sort((n1, n2) -> n2.getFecha().compareTo(n1.getFecha()));
    }

    /**
     * Elimina una notificación
     */
    public Mono<Void> eliminarNotificacion(String id) {
        // Primero verificar que existe, luego eliminar
        return repository.findById(id)
                .flatMap(notificacion -> {
                    System.out.println("Eliminando notificación con ID: " + id);
                    return repository.delete(notificacion);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("No se encontró notificación con ID: " + id + ", intentando deleteById");
                    return repository.deleteById(id);
                }));
    }

    /**
     * Obtiene todas las notificaciones de un usuario
     */
    public Flux<Notificacion> getNotificacionesPorUsuario(String usuario) {
        return repository.findByUsuarioOrderByFechaDesc(usuario);
    }
}

