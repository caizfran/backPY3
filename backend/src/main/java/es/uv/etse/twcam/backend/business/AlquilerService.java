
// AlquilerService.java
package es.uv.etse.twcam.backend.business;

import java.util.List;

public interface AlquilerService {
    /**
     * Crea un nuevo alquiler.
     */
    Alquiler iniciarAlquiler(int userId, int estacionId, int bicicletaId) 
        throws UsuarioException, EstacionException, BicicletaException, AlquilerActiveException;

    /**
     * Finaliza un alquiler existente.
     */
    void finalizarAlquiler(int alquilerId, int estacionId) 
        throws AlquilerException, EstacionException, EstacionFullException;

    /**
     * Lista los alquileres activos de un usuario.
     */
    List<Alquiler> listarAlquileresActivos(int userId) throws UsuarioException;

    /**
     * Lista el historial de alquileres de un usuario.
     */
    List<Alquiler> listarHistorialAlquileres(int userId) throws UsuarioException;
}
