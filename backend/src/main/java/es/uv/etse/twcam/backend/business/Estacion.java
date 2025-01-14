
// Estacion.java
package es.uv.etse.twcam.backend.business;

import java.util.ArrayList;
import java.util.List;

public class Estacion {
    private int id;
    private String nombre;
    private String ubicacion;
    private int capacidad;
    private List<Bicicleta> bicicletas;

    public Estacion() {
        this.bicicletas = new ArrayList<>();
    }

    public Estacion(int id, String nombre, String ubicacion, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
        this.bicicletas = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public List<Bicicleta> getBicicletas() {
        return new ArrayList<>(bicicletas);
    }

    public void setBicicletas(List<Bicicleta> bicicletas) {
        this.bicicletas = new ArrayList<>(bicicletas);
    }

    // Métodos de negocio
    public int getEspaciosLibres() {
        return capacidad - bicicletas.size();
    }

    public List<Bicicleta> getBicicletasDisponibles() {
        List<Bicicleta> disponibles = new ArrayList<>();
        for (Bicicleta bici : bicicletas) {
            if (bici.getEstado() == EstadoBicicleta.DISPONIBLE) {
                disponibles.add(bici);
            }
        }
        return disponibles;
    }

    public boolean retirarBicicleta(Bicicleta bicicleta) {
        return bicicletas.remove(bicicleta);
    }

    public boolean agregarBicicleta(Bicicleta bicicleta) {
        if (getEspaciosLibres() > 0) {
            return bicicletas.add(bicicleta);
        }
        return false;
    }
}