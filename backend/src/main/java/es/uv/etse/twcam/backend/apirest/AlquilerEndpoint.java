
// AlquilerEndpoint.java
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

@WebServlet("/api/alquileres/*")
public class AlquilerEndpoint extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(AlquilerEndpoint.class.getName());
    private final Gson g = new GsonBuilder().create();
    private static AlquilerService service = AlquilerServiceDictionaryImpl.getInstance();

    public AlquilerEndpoint() {
        super();
        logger.info("Alquiler Endpoint creado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String result = null;
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String tipo = request.getParameter("tipo");
            
            if ("activos".equals(tipo)) {
                List<Alquiler> alquileres = service.listarAlquileresActivos(userId);
                result = g.toJson(alquileres);
            } else {
                List<Alquiler> alquileres = service.listarHistorialAlquileres(userId);
                result = g.toJson(alquileres);
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error("Error al obtener alquileres", e);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String result = null;
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int estacionId = Integer.parseInt(request.getParameter("estacionId"));
            int bicicletaId = Integer.parseInt(request.getParameter("bicicletaId"));
            
            Alquiler alquiler = service.iniciarAlquiler(userId, estacionId, bicicletaId);
            result = g.toJson(alquiler);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            logger.error("Error al crear alquiler", e);
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String result = null;
        try {
            int alquilerId = Integer.parseInt(request.getPathInfo().substring(1));
            int estacionId = Integer.parseInt(request.getParameter("estacionId"));
            
            service.finalizarAlquiler(alquilerId, estacionId);
            result = "{\"message\": \"Alquiler finalizado correctamente\"}";
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error("Error al finalizar alquiler", e);
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
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "authorization,content-type");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }
}
