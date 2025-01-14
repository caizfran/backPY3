
// EstacionEndpoint.java
package es.uv.etse.twcam.backend.apirest;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.uv.etse.twcam.backend.business.*;
import org.apache.logging.log4j.*;
import java.util.List;

@WebServlet("/api/estaciones/*")
public class EstacionEndpoint extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(EstacionEndpoint.class.getName());
    private final Gson g = new GsonBuilder().create();
    private static EstacionService service = EstacionServiceDictionaryImpl.getInstance();

    public EstacionEndpoint() {
        super();
        logger.info("Estacion Endpoint creado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String result = null;
        try {
            String action = request.getParameter("action");
            
            if ("disponibles".equals(action)) {
                List<Estacion> estaciones = service.listarEstacionesConEspacios();
                result = g.toJson(estaciones);
            } else {
                List<Estacion> estaciones = service.listarEstaciones();
                result = g.toJson(estaciones);
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error("Error al obtener estaciones", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result = "{\"error\": \"" + e.getMessage() + "\"}";
        }

        addCORSHeaders(response);
        PrintWriter pw = response.getWriter();
        pw.println(result);
        pw.flush();
        pw.close();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        addCORSHeaders(response);
    }

    private void addCORSHeaders(HttpServletResponse response) {
        response.addHeader("Content-Type", "application/json");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "authorization,content-type");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }
}