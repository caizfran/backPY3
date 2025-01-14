// Bicicleta.java
package es.uv.etse.twcam.backend.business;

public class Bicicleta {
    private int id;
    private String nombre;
    private EstadoBicicleta estado;

    public Bicicleta() {
    }

    public Bicicleta(int id, String nombre, EstadoBicicleta estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
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

    public EstadoBicicleta getEstado() {
        return estado;
    }

    public void setEstado(EstadoBicicleta estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Bicicleta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                '}';
    }
}