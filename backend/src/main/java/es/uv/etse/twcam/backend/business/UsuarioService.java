package es.uv.etse.twcam.backend.business;

import java.util.List;

public interface UsuarioService {

    /**
     * Obtiene la lista completa de usuarios.
     * 
     * @return Lista de usuarios.
     */
    List<Usuario> listarUsuarios();

    /**
     * Verifica las credenciales del usuario.
     * 
     * @param nombreUsuario Nombre de usuario.
     * @param password Contraseña del usuario.
     * @return Usuario si las credenciales son correctas.
     * @throws UsuarioNotExistException Si las credenciales son incorrectas.
     */
    Usuario autenticarUsuario(String nombreUsuario, String password) throws UsuarioNotExistException;

    /**
     * Crea un nuevo usuario en el sistema.
     * 
     * @param usuario El usuario a crear.
     */
    void create(Usuario usuario); 

    /**
     * Lista usuarios filtrados por un rol específico.
     * 
     * @param rol Rol por el que filtrar.
     * @return Lista de usuarios que coinciden con el rol proporcionado.
     */
    List<Usuario> listarUsuariosPorRoles(String rol);

    /**
     * Lista todos los usuarios que tienen el rol de técnico o mantenimiento.
     * 
     * @return Lista de usuarios que tienen el rol de técnico o mantenimiento.
     */
    List<Usuario> listarPorTrabajadores();
}
