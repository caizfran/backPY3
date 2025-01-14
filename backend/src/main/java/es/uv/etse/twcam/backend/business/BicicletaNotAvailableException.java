
// BicicletaNotAvailableException.java
package es.uv.etse.twcam.backend.business;

public class BicicletaNotAvailableException extends BicicletaException {
    private static final long serialVersionUID = 1L;

    public BicicletaNotAvailableException(String id) {
        super("La bicicleta " + id + " no está disponible");
    }
}
