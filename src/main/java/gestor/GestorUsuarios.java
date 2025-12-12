package gestor;

import clasesDAO.ClienteDAO;
import clasesDAO.UsuarioDAO;
import modelo.personas.*;
import java.util.List;
import clasesDAO.EmpleadoDAO;

public class GestorUsuarios {
    
    private Usuario usuarioActual;
    private ClienteDAO clienteDAO;
    private UsuarioDAO usuarioDAO;
    private EmpleadoDAO empleadoDAO;

    public GestorUsuarios() {
        this.clienteDAO = new ClienteDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.empleadoDAO = new EmpleadoDAO();
    }
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public boolean autenticarUsuario(String usuario, String password) {
        Usuario u = usuarioDAO.login(usuario, password);
        if (u != null) {
            this.usuarioActual = u;
            return true;
        }
        return false;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public String getTipoUsuarioActual() {
        if (usuarioActual == null) return "NINGUNO";
        if (usuarioActual instanceof UsuarioAdministrador) return "ADMINISTRADOR";
        if (usuarioActual instanceof UsuarioEmpleado) return "EMPLEADO";
        if (usuarioActual instanceof UsuarioCliente) return "CLIENTE";
        return "DESCONOCIDO";
    }
    public boolean tienePermiso(String permiso) {
    if (usuarioActual instanceof UsuarioAdministrador) return true;
    if (usuarioActual instanceof UsuarioEmpleado) return true;
    
    if (usuarioActual instanceof UsuarioCliente) {
         // AGREGA ESTAS LÍNEAS AQUÍ:
         return permiso.equals("consultar_saldo") || 
                permiso.equals("consultar_movimientos") ||
                permiso.equals("realizar_depositos") ||
                permiso.equals("realizar_retiros") ||
                permiso.equals("realizar_transferencias");
    }
    return false;
}
    public boolean activarUsuario(String nombreUsuario) {
        return usuarioDAO.cambiarEstado(nombreUsuario, true);
    }

    public boolean desactivarUsuario(String nombreUsuario) {
        return usuarioDAO.cambiarEstado(nombreUsuario, false);
    }

    public boolean eliminarUsuario(String nombreUsuario) {
        return usuarioDAO.eliminar(nombreUsuario);
    }

    public boolean agregarUsuario(Usuario nuevoUsuario) {
    return usuarioDAO.crearUsuario(
        nuevoUsuario.getNombreUsuario(),
        nuevoUsuario.getContraseña(),
        obtenerTipoString(nuevoUsuario)  // "CLIENTE", "EMPLEADO" o "ADMIN"
    );
}

    
    private String obtenerTipoString(Usuario u) {
    if (u instanceof UsuarioCliente)  return "CLIENTE";
    if (u instanceof UsuarioEmpleado) return "EMPLEADO";
    return "ADMIN";
}

    // --- MÉTODOS DE CLIENTES ---
    
    public boolean agregarCliente(Cliente cliente) {
        return clienteDAO.guardar(cliente);
    }
    
    public List<Cliente> getClientes() {
        return clienteDAO.listarTodos();
    }
    
    public Cliente buscarCliente(String dni) {
        return clienteDAO.buscarPorDni(dni);
    }
    
    public Cliente buscarClientePorDni(String dni) {
        return clienteDAO.buscarPorDni(dni);
    }
    public boolean agregarEmpleado(Empleado empleado) {
        return empleadoDAO.guardar(empleado);
    }
    public List<Empleado> getEmpleados() {
        return empleadoDAO.listarTodos();
    }
    
    public Empleado buscarEmpleado(String dni) {
        return empleadoDAO.buscarPorDni(dni);
    }
    public java.util.List<modelo.personas.Usuario> getUsuarios() {
        return usuarioDAO.listarTodos();
    }
    public void actualizarCalificacion(String idCliente, String nuevaCalificacion) throws Exception {
        ClienteDAO.actualizarCalificacionDB(idCliente, nuevaCalificacion);
    }
}