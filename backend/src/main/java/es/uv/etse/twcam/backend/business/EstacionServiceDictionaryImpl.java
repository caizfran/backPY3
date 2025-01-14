
// EstacionServiceDictionaryImpl.java
package es.uv.etse.twcam.backend.business;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstacionServiceDictionaryImpl implements EstacionService {
    private static EstacionServiceDictionaryImpl the;
    protected Map<Integer, Estacion> dictionary;
    private BicicletaService bicicletaService;

    private EstacionServiceDictionaryImpl() {
        dictionary = new Hashtable<>();
        bicicletaService = BicicletaServiceDictionaryImpl.getInstance();
    }

    public static EstacionServiceDictionaryImpl getInstance() {
        if (the == null) {
            the = new EstacionServiceDictionaryImpl();
        }
        return the;
    }

    @Override
    public List<Estacion> listarEstaciones() {
        return new ArrayList<>(dictionary.values());
    }

    @Override
    public Estacion obtenerEstacion(int id) throws EstacionException {
        Estacion estacion = dictionary.get(id);
        if (estacion == null) {
            throw new EstacionException("Estación no encontrada: " + id);
        }
        return estacion;
    }

    @Override
    public List<Estacion> listarEstacionesConEspacios() {
        return dictionary.values().stream()
                .filter(e -> e.getEspaciosLibres() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public void agregarBicicleta(int estacionId, Bicicleta bicicleta) 
            throws EstacionException, EstacionFullException {
        Estacion estacion = obtenerEstacion(estacionId);
        if (estacion.getEspaciosLibres() <= 0) {
            throw new EstacionFullException(String.valueOf(estacionId));
        }
        estacion.agregarBicicleta(bicicleta);
        dictionary.put(estacionId, estacion);
    }

    @Override
    public void retirarBicicleta(int estacionId, int bicicletaId) 
            throws EstacionException, BicicletaException {
        Estacion estacion = obtenerEstacion(estacionId);
        Bicicleta bicicleta = bicicletaService.obtenerBicicleta(bicicletaId);
        estacion.retirarBicicleta(bicicleta);
        dictionary.put(estacionId, estacion);
    }
}