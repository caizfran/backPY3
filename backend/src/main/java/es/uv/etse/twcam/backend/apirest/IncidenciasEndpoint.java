package es.uv.etse.twcam.backend.apirest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.uv.etse.twcam.backend.business.Incidencia;
import es.uv.etse.twcam.backend.business.IncidenciasService;
import es.uv.etse.twcam.backend.business.IncidenciasServiceDictionaryImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Implementación  del Endpoint Incidencias
@WebServlet("/api/incidencias/*") // <1>
public class IncidenciasEndpoint extends HttpServlet {

    // Una instancia del serivcio de incidencias diccionario
    private static final IncidenciasServiceDictionaryImpl service2 = IncidenciasServiceDictionaryImpl.getInstance();
    private static final long serialVersionUID = 1L;

    //Logger como se hizo previamente el el InitServlet
    private static final Logger logger = LogManager.getLogger(IncidenciasEndpoint.class.getName());  // <7>

    //Nombre del endpoint
    private static final String END_POINT_NAME = "incidencias";

    //Parseador Gson
    private final Gson g = new GsonBuilder().create();

    //Servicio sobre incidencias
    private static IncidenciasService service = IncidenciasServiceDictionaryImpl.getInstance();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IncidenciasEndpoint() {
        super();
        logger.info("----------------------------------------");
        logger.info("---- Incidencias EndPoint creado -------"); // <7>
        logger.info("----------------------------------------");

    }

    ////////////////////////////// Implementacion del doGet() del Endpoint Incidencias /////////////////////
    /// 1. Listar 1 incidencia
    /// 2. Listar todas incidencias
    /// 3. Listar todas incidencias Asignadas

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String result = null;
        Integer id = null;

        ///////////////////////////////////// Parte 1 //////////////////////////////
        // Va a intentar ver si en la peticion request hay un identificador y en cuyo caso 
        // va a mostrar el identificador
        
        try {
            id = getIncidenciaId(request);
        } catch (Exception e) {
            logger.info("----------------------------------------");
            logger.info("No se ha podido obtener el identificador del request"); // <7> ////////////////////////// cuando no hy id es como dos zonas
            logger.info(", aun no se ha pasado el identificador por lo que se");
            logger.info("mostrarán todas las incidencias");
            logger.info("----------------------------------------");
        }

        logger.info("----------------------------------------");
        logger.info("GET at {} with ID: {}", request.getContextPath(), id); // <7>
        logger.info("----------------------------------------");

        ///////////////////////////////////// Parte 2 //////////////////////////////
        // Si no hay id existen dos opciones
        // 1. Si hay parametro de Asignada=true, lista todas las asignadas
        // 2. Si no hay parámetro de Asignada, lista todas las indicencias
        if (id == null) {
            List<Incidencia> incidencias = null;
            String oferta = request.getParameter("Asignada");

            if (oferta != null && oferta.equals("true")) {
                logger.info("----------------------------------------");
                logger.info("--------------- GET Asginadas ----------"); // <7>
                logger.info("----------------------------------------");
                incidencias = service.listAsignadas(); // http://localhost:8080/twcam-pls-plcv-mdps-spa/api/incidencias?Asignada=true
            } else {
                logger.info("----------------------------------------");
                logger.info("--------------- GET Todas --------------"); // <7>
                logger.info("----------------------------------------");
                incidencias = service.listAll(); // http://localhost:8080/twcam-pls-plcv-mdps-spa/api/incidencias
            }

            result = g.toJson(incidencias);

            // Si hay id busca con find en dicho servicio y la intenta mostrar
            // en el caso de que no haya lanzará un error en el logger
        } else {
            try {
                Incidencia pro = service.find(id); // http://localhost:8080/twcam-pls-plcv-mdps-spa/api/incidencias/1
                result = g.toJson(pro);
            } catch (Exception e) {
                logger.error("----------------------------------------");
                logger.error("-- Incidencia no encontrado, no se pasa ID"); // <7>
                logger.error("----------------------------------------");

            }
        }

        ///////////////////////////////////// Parte 3 //////////////////////////////
        // Añadir CORSHeaders

        addCORSHeaders(response); // <2>

        if (result != null) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);// <3>
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);// <3>
            result = "{}";
        }

        try {
            PrintWriter pw = response.getWriter();
            pw.println(result);
            pw.flush();
            pw.close();
        } catch (IOException ex) {
            logger.error("Imposible enviar respuesta", ex); // <7>
        }
    }

    ////////////////////////////// Implementacion del doPut() del Endpoint Incidencias /////////////////////
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Incidencia inci = null;

        try {

            // Obtener la incidencia desde la solicitud HTTP (JSON en el body)
            inci = getIncidenciaFromRequest(request);

            // Validar que la incidencia extraída no es nula
            if (inci == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                addCORSHeaders(response);
                logger.error("----------------------------------------");
                logger.error("Incidencia no actualizada por no se puede extraer desde JSON");
                logger.error("----------------------------------------");

            } else {
                // Actualizar la incidencia en el servicio
                inci = service.update(inci);

                // Log de actualización exitosa
                logger.info("----------------------------------------");
                logger.info("PUT at: {} with {} ", request.getContextPath(), inci);
                logger.info("----------------------------------------");

                // Responder con Accepted y la incidencia actualizada en JSON
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                addCORSHeaders(response);

                PrintWriter pw = response.getWriter();
                pw.println(g.toJson(inci));
                pw.flush();
                pw.close();
            }

        } catch (Exception e) {
            // Si ocurre un error, responder con Internal Server Error
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            logger.error("----------------------------------------");
            logger.error("----- Incidencia no actualizado --------", e);
            logger.error("----------------------------------------");

        }
    }

    ////////////////////////////// Implementacion del doPost() del Endpoint Incidencias /////////////////////
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Incidencia nuevaIncidencia = null;

        try {
            // Leer el JSON del cuerpo de la solicitud y convertirlo en un objeto Incidencia
            nuevaIncidencia = getIncidenciaFromInputStream(request.getInputStream());

            // Validar los datos obligatorios
            if (nuevaIncidencia == null || nuevaIncidencia.getEstado() == null || nuevaIncidencia.getDescripcion() == null) {
                throw new IllegalArgumentException("Los datos de la incidencia son inválidos o incompletos");
            }

            // Usar el método create del servicio para guardar la incidencia
            Incidencia incidenciaCreada = service2.create(nuevaIncidencia);

            // Responder con éxito (201 Created)
            response.setStatus(HttpServletResponse.SC_CREATED);
            addCORSHeaders(response);

            PrintWriter pw = response.getWriter();
            pw.println(g.toJson(incidenciaCreada)); // Convertir la incidencia creada a JSON y enviarla como respuesta
            pw.flush();
            pw.close();

            logger.info("----------------------------------------");
            logger.info("-Incidencia con id = {}, ha sido añadida", incidenciaCreada.getid_inc()); // Log de éxito
            logger.info("----------------------------------------");

            logger.info("----------------------------------------");
            logger.info("Incidencia creada con éxito: {}", incidenciaCreada); // Log de éxito
            logger.info("----------------------------------------");

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            addCORSHeaders(response);
            logger.error("Error en los datos de la solicitud: {}", e.getMessage(), e); // Log del error
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            addCORSHeaders(response);
            logger.error("Error interno al crear la incidencia: {}", e.getMessage(), e); // Log del error interno
        }
    }

    ////////////////////////////// Implementacion del doOptions() del Endpoint Incidencias /////////////////////
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {

        addCORSHeaders(response); // <2>

        try {
            super.doOptions(request, response);
        } catch (ServletException se) {
            logger.error("Error genérico en la clase padre"); // <7>
        } catch (IOException ioe) {
            logger.error("Error genérico de salida la clase padre"); // <7>
        }
    }

    // Obtiene la Incidencia de un stream JSON
    private Incidencia getIncidenciaFromInputStream(InputStream stream) { // stream json

        Incidencia inci = null;
        try {
            inci = g.fromJson(new InputStreamReader(stream), Incidencia.class); // <4>
        } catch (Exception e) {
            inci = null;
            logger.error("Error al obtener incidencia desde JSON", e); // <7>
        }
        return inci;
    }

    //Obtiene el identificador de un Producto como parte de la URL de la
    // petición HTTP
    protected static Integer getIncidenciaId(HttpServletRequest request) throws APIRESTException {  // <5>

        String url = request.getRequestURL().toString();
        int posIni = url.lastIndexOf("/");
        int posEnd = url.lastIndexOf("?");

        if (posEnd < 0) {
            posEnd = url.length();
        }

        String id = url.substring(posIni + 1, posEnd);
        logger.debug("ID: {}", id);// <7>

        if (id.trim().isEmpty()) {
            id = null;
        }

        if (id == null) {
            throw new APIRESTException("Faltan parámetros en el EndPoint");
        } else {
            if (id.equals(END_POINT_NAME)) {
                id = null;
            }
        }

        Integer valor = null;
        if (id != null) {
            valor = Integer.valueOf(id);
        }
        return valor;
    }

    //Obtiene la incidecia desde la petición HTTP y el identificador como parte
    // de la URL.
    private Incidencia getIncidenciaFromRequest(HttpServletRequest request) { //peticion

        Incidencia inci = null;

        try {
            Integer id = getIncidenciaId(request);
            if (id != null) {
                inci = getIncidenciaFromInputStream(request.getInputStream());
                if (inci != null && !inci.getid_inc().equals(id)) // <6>
                {
                    inci = null;
                }
            }

        } catch (Exception e) {
            inci = null;
        }
        return inci;
    }

    // Añade cabeceras Cross-origin resource sharing (CORS) para poder invocar
    // el API REST desde Angular
    private void addCORSHeaders(HttpServletResponse response) { // <2>
        response.addHeader("Content-Type", "application/json");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
        response.addHeader("Access-Control-Allow-Headers", "authorization,content-type");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }
}
