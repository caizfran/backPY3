package es.uv.etse.twcam.backend.business;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Implementación del servicio de productos basado en un diccionario sin persistencia.
public class IncidenciasServiceDictionaryImpl implements IncidenciasService {

    // Instancia única.
    private static IncidenciasServiceDictionaryImpl the;

    //Diccionario para el almacenamiento de incidencias
    protected Map<Integer, Incidencia> dictionary;

    //Crea un servicio sin datos/información 
    IncidenciasServiceDictionaryImpl() {
        dictionary = new Hashtable<>();
    }

    // Obtiene la instancia única del servicio
    public static IncidenciasServiceDictionaryImpl getInstance() {
        if (the == null) {
            the = new IncidenciasServiceDictionaryImpl();
        }
        return the;
    }

    //Limpia la instancia única del servicio.
    public static void clearInstance() {
        if (the != null) {
            the.dictionary.clear();
            the = null;
        }
    }

    // Automatico el generador de id
    private static int idCounter = 5; // como ya ponemos de base 4 incidencias, esta, comienza desde el 5

    private Integer generarID() {
        return idCounter++; // Genera un ID único incrementando un contador
    }

    //Añade una incidencia al servicio
    public Incidencia create(Incidencia inci) {
        if (inci != null) {
            if (inci.getid_inc() == null) {
                // Generar y asignar un ID si es null
                inci.setid_inc(generarID());
            }
            dictionary.put(inci.getid_inc(), inci);
        }
        return inci;
    }

    //Override listAll() definido en IndicenciasService
    @Override
    public List<Incidencia> listAll() {
        List<Incidencia> incidencias = new ArrayList<>();

        incidencias.addAll(dictionary.values());

        return incidencias;
    }

    //Override listAsignadas() definido en IndicenciasService
    @Override
    public List<Incidencia> listAsignadas() {
        return dictionary.values().stream()
                .filter(Incidencia -> "Asignada".equals(Incidencia.getEstado()))
                .collect(Collectors.toList());
    }

    //Override update() definido en IndicenciasService
    // Recibe una incidencia y devuelve una incidencia, verifica que no sea nulo y que esté en el diccionario
    // Si existe dicha incidencia id, se actualiza en el diccionario con la nueva información
    @Override
    public Incidencia update(Incidencia inci) {
        if (inci != null && dictionary.containsKey(inci.getid_inc())) {
            dictionary.put(inci.getid_inc(), inci);
        }
        return inci;
    }

    //Override find() definido en IndicenciasService
    @Override
    public Incidencia find(Integer id) throws IncidenciaNotExistException {
        if (dictionary.containsKey(id)) {
            return dictionary.get(id);
        } else {
            throw new IncidenciaNotExistException(id);
        }
    }

    //Override asignarIncidencia() definido en IndicenciasService
    @Override
    public Incidencia asignarIncidencia(Integer id_inc, Integer tecnico_id) throws IncidenciaNotExistException {
        if (!dictionary.containsKey(id_inc)) {
            throw new IncidenciaNotExistException(id_inc);
        }
        Incidencia incidencia = dictionary.get(id_inc);
        incidencia.setTecnico_id(tecnico_id);
        incidencia.setEstado("Asignada");

        dictionary.put(id_inc, incidencia); // Actualizar en el diccionario

        return incidencia;
    }

}
