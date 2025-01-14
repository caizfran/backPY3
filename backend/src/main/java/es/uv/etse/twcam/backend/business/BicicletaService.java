// BicicletaService.java
package es.uv.etse.twcam.backend.business;

import java.util.List;

public interface BicicletaService {
    /**
     * Lista todas las bicicletas del sistema.
     */
    List<Bicicleta> listarBicicletas();

    /**
     * Lista las bicicletas por estado.
     */
    List<Bicicleta> listarBicicletasPorEstado(EstadoBicicleta estado);

    /**
     * Obtiene una bicicleta por su ID.
     */
    Bicicleta obtenerBicicleta(int id) throws BicicletaException;

    /**
     * Crea una nueva bicicleta.
     */
    void create(Bicicleta bicicleta);

    /**
     * Actualiza el estado de una bicicleta.
     */
    void actualizarEstado(int id, EstadoBicicleta estado) throws BicicletaException;

    /**
     * Elimina una bicicleta del sistema.
     * Solo se pueden eliminar bicicletas que no estén en uso.
     * @param id ID de la bicicleta a eliminar
     * @throws BicicletaException si la bicicleta no existe o está en uso
     */
    void deleteBicicleta(int id) throws BicicletaException;
}