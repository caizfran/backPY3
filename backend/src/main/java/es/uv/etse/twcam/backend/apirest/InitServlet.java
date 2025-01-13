package es.uv.etse.twcam.backend.apirest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

import org.apache.logging.log4j.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import es.uv.etse.twcam.backend.business.Producto;
import es.uv.etse.twcam.backend.business.Incidencia;
import es.uv.etse.twcam.backend.business.IncidenciaException;
import es.uv.etse.twcam.backend.business.IncidenciasService;
import es.uv.etse.twcam.backend.business.IncidenciasServiceDictionaryImpl;
import es.uv.etse.twcam.backend.business.ProductException;
import es.uv.etse.twcam.backend.business.ProductsService;
import es.uv.etse.twcam.backend.business.ProductsServiceDictionaryImpl;
import es.uv.etse.twcam.backend.business.Usuario;
import es.uv.etse.twcam.backend.business.UsuarioService;
import es.uv.etse.twcam.backend.business.UsuarioServiceDictionaryImpl;

/**
 * Servlet de inicializaci&oacute;n
 * 
 * @author <a href="mailto:raul.penya@uv.es">Ra&uacute;l Pe&ntilde;a-Ortiz</a>
 */
public class InitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected static Logger logger = LogManager.getLogger(InitServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            logger.info("Starting angular-j2e-example apirest ...");

            String jsonFile = getServletConfig().getInitParameter("json-database");

            InputStream jsonStream = getServletContext().getResourceAsStream(jsonFile);

            // Crear el lector y el objeto Gson
            Reader jsonReader = new InputStreamReader(jsonStream);
            Gson gson = new GsonBuilder().create();

            // Leer y deserializar el JSON
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();

            // Inicializar servicios de productos, usuarios e incidencias
            initProductsService(jsonObject, gson);
            initUsersService(jsonObject, gson);
            initIncidenciasService(jsonObject, gson);

            logger.info("plc-pls-mps-tutorial apirest is started");

        } catch (Exception e) {
            logger.error("plc-pls-mps-tutorial apirest is not able to be started: ", e);
            throw new ServletException(e);
        }
    }

    /**
     * Crea el servicio de productos y lo inicializa a partir de un objeto JSON.
     * @param jsonObject Objeto JSON
     * @param gson Instancia de Gson
     * @throws ProductException Indicador de errores
     */
    public static ProductsService initProductsService(JsonObject jsonObject, Gson gson)
            throws ProductException {

        ProductsServiceDictionaryImpl service = ProductsServiceDictionaryImpl.getInstance();
        JsonArray productosJson = jsonObject.getAsJsonArray("productos");

        for (int i = 0; i < productosJson.size(); i++) {
            Producto producto = gson.fromJson(productosJson.get(i), Producto.class);
            service.create(producto);
        }

        logger.info("Cargados {} productos", productosJson.size());

        return service;
    }

    /**
     * Crea el servicio de usuarios y lo inicializa a partir de un objeto JSON.
     * @param jsonObject Objeto JSON
     * @param gson Instancia de Gson
     * @throws Exception Indicador de errores
     */
    public static UsuarioService initUsersService(JsonObject jsonObject, Gson gson)
            throws Exception {

        UsuarioServiceDictionaryImpl service = UsuarioServiceDictionaryImpl.getInstance();
        JsonArray usuariosJson = jsonObject.getAsJsonArray("usuarios");

        for (int i = 0; i < usuariosJson.size(); i++) {
            Usuario usuario = gson.fromJson(usuariosJson.get(i), Usuario.class);
            service.create(usuario);
        }

        logger.info("Cargados {} usuarios", usuariosJson.size());

        return service;
    }

    /**
     * Crea el servicio de incidencias y lo inicializa a partir de un objeto JSON.
     * @param jsonObject Objeto JSON
     * @param gson Instancia de Gson
     * @throws IncidenciaException Indicador de errores
     */
    public static IncidenciasService initIncidenciasService(JsonObject jsonObject, Gson gson)
            throws IncidenciaException {

        // Conseguimos una instancia del servicio de incidencias
        IncidenciasServiceDictionaryImpl service = IncidenciasServiceDictionaryImpl.getInstance();

        // Obtener el array de incidencias desde el objeto JSON
        JsonArray incidenciasJson = jsonObject.getAsJsonArray("incidencias");

        // Iterar sobre las incidencias y crearlas en el servicio
        for (int i = 0; i < incidenciasJson.size(); i++) {
            Incidencia incidencia = gson.fromJson(incidenciasJson.get(i), Incidencia.class);
            service.create(incidencia);
        }

        logger.info("Cargadas {} incidencias", incidenciasJson.size());

        return service;
    }

}
