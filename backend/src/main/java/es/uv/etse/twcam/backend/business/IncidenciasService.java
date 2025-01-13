package es.uv.etse.twcam.backend.business;

import java.util.List;

/**
 * Servicio sobre productos. Implementa toda la funcionalidad sobre los
 * productos.
 */
public interface IncidenciasService {

    //Lista todas las incidencias
    public List<Incidencia> listAll();

    //Lista todas las incidencias cuyo estado="Asignada"
    public List<Incidencia> listAsignadas();

    //Obtiene una incidencia apartir de su id
    public Incidencia find(Integer id) throws IncidenciaNotExistException;

    //Actualiza la información de una incidencia
    public Incidencia update(Incidencia inc) throws IncidenciaException;

    //Asgina una incidencia a un técnico
    public Incidencia asignarIncidencia(Integer idIncidencia, Integer tecnico_id) throws IncidenciaNotExistException;

}
