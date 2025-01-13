package es.uv.etse.twcam.backend.business;

public class UsuarioNotExistException extends UsuarioException {

	private static final long serialVersionUID = 1L;

	public UsuarioNotExistException(String id) {
		super("El usuario "+id+" no existe");
	}

}

