
// BicicletaServiceDictionaryImpl.java
package es.uv.etse.twcam.backend.business;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BicicletaServiceDictionaryImpl implements BicicletaService {
    private static BicicletaServiceDictionaryImpl the;
    protected Map<Integer, Bicicleta> dictionary;

    private BicicletaServiceDictionaryImpl() {
        dictionary = new Hashtable<>();
    }

    public static BicicletaServiceDictionaryImpl getInstance() {
        if (the == null) {
            the = new BicicletaServiceDictionaryImpl();
        }
        return the;
    }

    @Override
    public List<Bicicleta> listarBicicletas() {
        return new ArrayList<>(dictionary.values());
    }

    @Override
    public List<Bicicleta> listarBicicletasPorEstado(EstadoBicicleta estado) {
        return dictionary.values().stream()
                .filter(b -> b.getEstado() == estado)
                .collect(Collectors.toList());
    }

    @Override
    public Bicicleta obtenerBicicleta(int id) throws BicicletaException {
        Bicicleta bici = dictionary.get(id);
        if (bici == null) {
            throw new BicicletaException("Bicicleta no encontrada: " + id);
        }
        return bici;
    }

    @Override
    public void create(Bicicleta bicicleta) {
        int id = dictionary.size() + 1;
        bicicleta.setId(id);
        dictionary.put(id, bicicleta);
    }

    @Override
    public void actualizarEstado(int id, EstadoBicicleta estado) throws BicicletaException {
        Bicicleta bici = obtenerBicicleta(id);
        bici.setEstado(estado);
        dictionary.put(id, bici);
    }
    @Override
    public void deleteBicicleta(int id) throws BicicletaException {
        Bicicleta bici = obtenerBicicleta(id);
        if (bici.getEstado() == EstadoBicicleta.EN_USO) {
            throw new BicicletaException("No se puede eliminar una bicicleta que está en uso: " + id);
        }
        dictionary.remove(id);
    }
}