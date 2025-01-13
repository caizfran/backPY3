package es.uv.etse.twcam.backend.business;

public class Incidencia {

    private Integer id_inc;
    private String estado;
    private String descripcion;
    private String ubicacion_bici;
    private String situacion;
    private Integer tecnico_id;
    private static int nextId = 5;

    /**
     * Crea un comentario vacío
     */
    public Incidencia() {
    }

    /**
     * Crea una Incidencia a partir de sus campos
     *
     * @param estado
     * @param descripcion
     * @param ubicacion_bici
     * @param situacion
     * @param tecnico_id
     */
    private static synchronized int generarID() {
        return nextId++;
    }

    public Incidencia(String estado, String descripcion, String ubicacion_bici, String situacion, Integer tecnico_id) {
        this.id_inc = generarID();
        this.estado = estado;
        this.descripcion = descripcion;
        this.ubicacion_bici = ubicacion_bici;
        this.situacion = situacion;
        this.tecnico_id = tecnico_id;
    }

    public static void resetIdGenerator() {
        nextId = 1;
    }

    // Getters y setters
    public Integer getid_inc() {
        return id_inc;
    }

    public void setid_inc(Integer id_inc) {
        this.id_inc = id_inc;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion_bici() {
        return ubicacion_bici;
    }

    public void setUbicacion_bici(String ubicacion_bici) {
        this.ubicacion_bici = ubicacion_bici;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public Integer getTecnico_id() {
        return tecnico_id;
    }

    public void setTecnico_id(Integer tecnico_id) {
        this.tecnico_id = tecnico_id;
    }

    @Override
    public String toString() {
        return "{\n"
                + "  \"id_inc\": " + id_inc + ",\n"
                + "  \"estado\": \"" + estado + "\",\n"
                + "  \"descripcion\": \"" + descripcion + "\",\n"
                + "  \"ubicacion_bici\": \"" + ubicacion_bici + "\",\n"
                + "  \"situacion\": \"" + situacion + "\",\n"
                + "  \"tecnico_id\": " + tecnico_id + "\n"
                + "}";
    }

}
