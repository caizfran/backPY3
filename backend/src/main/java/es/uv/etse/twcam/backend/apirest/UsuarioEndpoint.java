package es.uv.etse.twcam.backend.apirest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.uv.etse.twcam.backend.business.Usuario;
import es.uv.etse.twcam.backend.business.UsuarioService;
import es.uv.etse.twcam.backend.business.UsuarioServiceDictionaryImpl;
import es.uv.etse.twcam.backend.business.UsuarioNotExistException;
import org.apache.logging.log4j.*;

@WebServlet("/api/usuarios/*")
public class UsuarioEndpoint extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(UsuarioEndpoint.class.getName());
    private final Gson g = new GsonBuilder().create();
    private static UsuarioService service = UsuarioServiceDictionaryImpl.getInstance();

    public UsuarioEndpoint() {
        super();
        logger.info("User Endpoint creado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result = null;

        try {
            String rol = request.getParameter("rol"); // Obtener el rol del parámetro
            String action = request.getParameter("action"); // Obtener acción del parámetro

            if ("trabajadores".equals(action)) {
                // Listar usuarios por rol de trabajadores
                List<Usuario> usuariosTrabajadores = service.listarPorTrabajadores();
                result = g.toJson(usuariosTrabajadores);
            } else if (rol != null) {
                // Listar usuarios por rol
                List<Usuario> usuariosPorRol = service.listarUsuariosPorRoles(rol);
                result = g.toJson(usuariosPorRol);
            } else {
                // Si no se proporciona un rol, se listan todos los usuarios
                List<Usuario> usuarios = service.listarUsuarios();
                result = g.toJson(usuarios);
            }
        } catch (Exception e) {
            logger.error("Error al obtener usuarios", e);
        }

        addCORSHeaders(response);

        if (result != null) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            result = "{}";
        }

        PrintWriter pw = response.getWriter();
        pw.println(result);
        pw.flush();
        pw.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result = null;

        try {
            // Leer el cuerpo de la solicitud
            BufferedReader reader = request.getReader();
            String action = request.getParameter("action");

            if ("login".equals(action)) {
                // Autenticar usuario
                String nombreUsuario = request.getParameter("nombreUsuario");
                String password = request.getParameter("password");
                Usuario usuario = service.autenticarUsuario(nombreUsuario, password);
                result = g.toJson(usuario);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                // Manejar error si el action no es reconocido
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                result = "{\"error\": \"Acción no válida\"}";
            }
        } catch (UsuarioNotExistException e) {
            logger.error("Credenciales incorrectas", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            result = "{\"error\": \"Credenciales incorrectas\"}";
        } catch (Exception e) {
            logger.error("Error al procesar la solicitud", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            result = "{}";
        }

        PrintWriter pw = response.getWriter();
        pw.println(result);
        pw.flush();
        pw.close();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        addCORSHeaders(response);
        try {
            super.doOptions(request, response);
        } catch (ServletException | IOException e) {
            logger.error("Error en OPTIONS", e);
        }
    }

    protected static Integer getUsuarioId(HttpServletRequest request) throws APIRESTException {
        String url = request.getRequestURL().toString();
        int posIni = url.lastIndexOf("/");
        int posEnd = url.lastIndexOf("?");

        if (posEnd < 0) {
            posEnd = url.length();
        }

        String id = url.substring(posIni + 1, posEnd);

        logger.debug("ID: {}", id);

        if (id.trim().isEmpty() || id.equals("usuarios")) {
            return null;
        }

        return Integer.valueOf(id);
    }

    private void addCORSHeaders(HttpServletResponse response) {
        response.addHeader("Content-Type", "application/json");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
        response.addHeader("Access-Control-Allow-Headers", "authorization,content-type");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }
}
