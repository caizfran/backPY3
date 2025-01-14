package es.uv.etse.twcam.backend.business;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de usuarios basado en un diccionario sin
 * persistencia.
 * 
 * La implementación sigue el patrón de singleton.
 * 
 * @author <a href="mailto:raul.penya@uv.es">Ra&uacute;l Pe&ntilde;a-Ortiz</a>
 */
public class UsuarioServiceDictionaryImpl implements UsuarioService {

    /**
     * Instancia única.
     */
    private static UsuarioServiceDictionaryImpl the;

    /**
     * Diccionario para el almacenamiento de usuarios.
     */
    protected Map<Integer, Usuario> dictionary;

    /**
     * Crea un servicio sin datos.
     */
    private UsuarioServiceDictionaryImpl() {
        dictionary = new Hashtable<>();
    }

    /**
     * Obtiene la instancia única del servicio.
     * 
     * @return Instancia única.
     */
    public static UsuarioServiceDictionaryImpl getInstance() {
        if (the == null) {
            the = new UsuarioServiceDictionaryImpl();
        }
        return the;
    }

    /**
     * Limpia la instancia única del servicio.
     */
    public static void clearInstance() {
        if (the != null) {
            the.dictionary.clear();
            the = null;
        }
    }

    @Override
    public List<Usuario> listarUsuarios() {
        // Retorna una lista con todos los usuarios almacenados en el diccionario.
        return new ArrayList<>(dictionary.values());
    }

    @Override
    public Usuario autenticarUsuario(String nombreUsuario, String password) throws UsuarioNotExistException {
        // Busca un usuario por su nombre de usuario y contraseña.
        return dictionary.values().stream()
                .filter(usuario -> usuario.getNombreUsuario().equals(nombreUsuario) && usuario.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new UsuarioNotExistException("Credenciales incorrectas")); // Lanza excepción si no se encuentra.
    }

    @Override
    public void create(Usuario usuario) {
        // Asigna un ID único al usuario y lo agrega al diccionario.
        int id = dictionary.size() + 1; // Genera un nuevo ID (puedes mejorar la lógica si es necesario)
        usuario.setId(id);
        dictionary.put(id, usuario); // Agrega el usuario al diccionario
    }

    @Override
    public List<Usuario> listarUsuariosPorRoles(String rol) {
        // Devuelve los usuarios que tienen el rol especificado.
        return dictionary.values().stream()
                .filter(usuario -> usuario.getRol().equalsIgnoreCase(rol))
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> listarPorTrabajadores() {
        // Devuelve todos los usuarios que tienen rol "técnico" o "mantenimiento".
        return dictionary.values().stream()
                .filter(usuario -> {
                    String rol = usuario.getRol();
                    return rol.equalsIgnoreCase("tecnico") || rol.equalsIgnoreCase("mantenimiento");
                })
                .collect(Collectors.toList());
    }
 /// ajuste para Obtener usuario
    @Override
    public Usuario obtenerUsuario(int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerUsuario'");
    }
}
