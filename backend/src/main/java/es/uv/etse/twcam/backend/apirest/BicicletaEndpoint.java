
// BicicletaEndpoint.java
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

@WebServlet("/api/bicicletas/*")
public class BicicletaEndpoint extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(BicicletaEndpoint.class.getName());
    private final Gson g = new GsonBuilder().create();
    private static BicicletaService service = BicicletaServiceDictionaryImpl.getInstance();

    public BicicletaEndpoint() {
        super();
        logger.info("Bicicleta Endpoint creado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String result = null;
        try {
            String estado = request.getParameter("estado");
            
            if (estado != null) {
                List<Bicicleta> bicicletas = service.listarBicicletasPorEstado(EstadoBicicleta.valueOf(estado));
                result = g.toJson(bicicletas);
            } else {
                List<Bicicleta> bicicletas = service.listarBicicletas();
                result = g.toJson(bicicletas);
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error("Error al obtener bicicletas", e);
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String result = null;
        try {
            int bicicletaId = Integer.parseInt(request.getPathInfo().substring(1));
            String estado = request.getParameter("estado");
            
            service.actualizarEstado(bicicletaId, EstadoBicicleta.valueOf(estado));
            response.setStatus(HttpServletResponse.SC_OK);
            result = "{\"message\": \"Estado actualizado correctamente\"}";
        } catch (Exception e) {
            logger.error("Error al actualizar estado de bicicleta", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, PUT");
        response.addHeader("Access-Control-Allow-Headers", "authorization,content-type");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }
}
