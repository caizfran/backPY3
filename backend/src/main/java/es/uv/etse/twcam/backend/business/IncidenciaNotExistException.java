package es.uv.etse.twcam.backend.business;

public class IncidenciaNotExistException extends IncidenciaException {

	private static final long serialVersionUID = 1L;

	public IncidenciaNotExistException(Integer id) {
		super("El producto "+id+" no existe");
	}

}
