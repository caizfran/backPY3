// Alquiler.java
package es.uv.etse.twcam.backend.business;

import java.time.LocalDateTime;

public class Alquiler {
    private int id;
    private Usuario usuario;
    private Bicicleta bicicleta;
    private Estacion estacionOrigen;
    private Estacion estacionDestino;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public Alquiler() {
    }

    public Alquiler(int id, Usuario usuario, Bicicleta bicicleta, Estacion estacionOrigen) {
        this.id = id;
        this.usuario = usuario;
        this.bicicleta = bicicleta;
        this.estacionOrigen = estacionOrigen;
        this.fechaInicio = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Bicicleta getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(Bicicleta bicicleta) {
        this.bicicleta = bicicleta;
    }

    public Estacion getEstacionOrigen() {
        return estacionOrigen;
    }

    public void setEstacionOrigen(Estacion estacionOrigen) {
        this.estacionOrigen = estacionOrigen;
    }

    public Estacion getEstacionDestino() {
        return estacionDestino;
    }

    public void setEstacionDestino(Estacion estacionDestino) {
        this.estacionDestino = estacionDestino;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    // Métodos de negocio
    public void finalizarAlquiler(Estacion estacionDestino) {
        this.estacionDestino = estacionDestino;
        this.fechaFin = LocalDateTime.now();
        this.bicicleta.setEstado(EstadoBicicleta.DISPONIBLE);
        estacionDestino.agregarBicicleta(this.bicicleta);
    }
}
