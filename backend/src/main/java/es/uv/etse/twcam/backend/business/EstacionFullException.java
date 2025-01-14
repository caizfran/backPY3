
// EstacionFullException.java
package es.uv.etse.twcam.backend.business;

public class EstacionFullException extends EstacionException {
    private static final long serialVersionUID = 1L;

    public EstacionFullException(String id) {
        super("La estación " + id + " está llena");
    }
}