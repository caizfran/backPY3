
// AlquilerServiceDictionaryImpl.java
package es.uv.etse.twcam.backend.business;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlquilerServiceDictionaryImpl implements AlquilerService {
    private static AlquilerServiceDictionaryImpl the;
    protected Map<Integer, Alquiler> dictionary;
    private UsuarioService usuarioService;
    private BicicletaService bicicletaService;
    private EstacionService estacionService;

    private AlquilerServiceDictionaryImpl() {
        dictionary = new Hashtable<>();
        usuarioService = UsuarioServiceDictionaryImpl.getInstance();
        bicicletaService = BicicletaServiceDictionaryImpl.getInstance();
        estacionService = EstacionServiceDictionaryImpl.getInstance();
    }

    public static AlquilerServiceDictionaryImpl getInstance() {
        if (the == null) {
            the = new AlquilerServiceDictionaryImpl();
        }
        return the;
    }

    @Override
    public Alquiler iniciarAlquiler(int userId, int estacionId, int bicicletaId)
            throws UsuarioException, EstacionException, BicicletaException, AlquilerActiveException {
        // Verificar si el usuario tiene alquileres activos
        if (!listarAlquileresActivos(userId).isEmpty()) {
            throw new AlquilerActiveException(String.valueOf(userId));
        }

        Usuario usuario = usuarioService.obtenerUsuario(userId);
        Bicicleta bicicleta = bicicletaService.obtenerBicicleta(bicicletaId);
        Estacion estacion = estacionService.obtenerEstacion(estacionId);

        if (bicicleta.getEstado() != EstadoBicicleta.DISPONIBLE) {
            throw new BicicletaNotAvailableException(String.valueOf(bicicletaId));
        }

        // Crear nuevo alquiler
        Alquiler alquiler = new Alquiler(dictionary.size() + 1, usuario, bicicleta, estacion);
        
        // Actualizar estado de la bicicleta y retirarla de la estación
        bicicleta.setEstado(EstadoBicicleta.EN_USO);
        estacionService.retirarBicicleta(estacionId, bicicletaId);
        
        dictionary.put(alquiler.getId(), alquiler);
        return alquiler;
    }

    @Override
    public void finalizarAlquiler(int alquilerId, int estacionId)
            throws AlquilerException, EstacionException, EstacionFullException {
        Alquiler alquiler = dictionary.get(alquilerId);
        if (alquiler == null) {
            throw new AlquilerException("Alquiler no encontrado: " + alquilerId);
        }

        Estacion estacionDestino = estacionService.obtenerEstacion(estacionId);
        if (estacionDestino.getEspaciosLibres() <= 0) {
            throw new EstacionFullException(String.valueOf(estacionId));
        }

        alquiler.finalizarAlquiler(estacionDestino);
        dictionary.put(alquilerId, alquiler);
    }

    @Override
    public List<Alquiler> listarAlquileresActivos(int userId) throws UsuarioException {
        Usuario usuario = usuarioService.obtenerUsuario(userId);
        return dictionary.values().stream()
                .filter(a -> a.getUsuario().getId() == usuario.getId() && a.getFechaFin() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alquiler> listarHistorialAlquileres(int userId) throws UsuarioException {
        Usuario usuario = usuarioService.obtenerUsuario(userId);
        return dictionary.values().stream()
                .filter(a -> a.getUsuario().getId() == usuario.getId())
                .collect(Collectors.toList());
    }
}
