package com.concurrente.notificaciones.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "notificaciones")
public class Notificacion {

    @Id
    private String id;
    private String usuario;
    private String mensaje;
    private String tipo; // "INFO", "ALERTA", "URGENTE"
    private Date fecha;
    private Boolean leido;

    public Notificacion() {
        this.fecha = new Date();
        this.leido = false;
    }

    public Notificacion(String usuario, String mensaje, String tipo) {
        this();
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id='" + id + '\'' +
                ", usuario='" + usuario + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fecha=" + fecha +
                ", leido=" + leido +
                '}';
    }
}

