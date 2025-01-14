
// AlquilerActiveException.java
package es.uv.etse.twcam.backend.business;

public class AlquilerActiveException extends AlquilerException {
    private static final long serialVersionUID = 1L;

    public AlquilerActiveException(String userId) {
        super("El usuario " + userId + " ya tiene un alquiler activo");
    }
}