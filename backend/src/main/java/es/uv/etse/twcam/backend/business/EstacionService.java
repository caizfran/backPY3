// EstacionService.java
package es.uv.etse.twcam.backend.business;

import java.util.List;

public interface EstacionService {
    /**
     * Lista todas las estaciones.
     */
    List<Estacion> listarEstaciones();

    /**
     * Obtiene una estación por su ID.
     */
    Estacion obtenerEstacion(int id) throws EstacionException;

    /**
     * Lista las estaciones con espacios disponibles.
     */
    List<Estacion> listarEstacionesConEspacios();

    /**
     * Añade una bicicleta a una estación.
     */
    void agregarBicicleta(int estacionId, Bicicleta bicicleta) throws EstacionException, EstacionFullException;

    /**
     * Retira una bicicleta de una estación.
     */
    void retirarBicicleta(int estacionId, int bicicletaId) throws EstacionException, BicicletaException;
}