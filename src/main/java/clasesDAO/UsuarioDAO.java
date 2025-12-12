package clasesDAO;

import modelo.personas.*;
import java.sql.*;
import gestor.ConexionBD;
public class UsuarioDAO{
    private ConexionBD conexion;
    public UsuarioDAO() { this.conexion = new ConexionBD(); }
    public Usuario login(String user, String pass) {
        Usuario usuarioLogueado = null;
        String sql = "SELECT u.*, p.dni, p.nombre, p.apellido " +
                     "FROM usuarios u " +
                     "LEFT JOIN personas p ON u.id_persona = p.id " +
                     "WHERE u.nombre_usuario = ? AND u.contrasena = ? AND u.estado = 1";
        try (Connection conn = conexion.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String tipo = rs.getString("tipo_usuario");
                String nombreUsuario = rs.getString("nombre_usuario");
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                switch (tipo) {
                    case "CLIENTE":
                        Cliente cliente = new Cliente(dni, nombre, apellido, "N/A");
                        usuarioLogueado = new UsuarioCliente(nombreUsuario, pass, cliente);
                        break;
                    case "EMPLEADO":
                        Empleado empleado = new Empleado(dni, nombre, apellido,
                            "EMP-00" + rs.getInt("id"),
                            "Staff",
                            "General");
                        usuarioLogueado = new UsuarioEmpleado(nombreUsuario, pass, empleado);
                        break;
                    case "ADMIN":
                        usuarioLogueado = new UsuarioAdministrador(nombreUsuario, pass);
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en login: " + e.getMessage());
        }
        return usuarioLogueado;
    }
    public boolean crearUsuario(String nombreUsuario, String password, String tipo) {
        String sql =
        "INSERT INTO usuarios (nombre_usuario, contrasena, tipo_usuario, estado) VALUES (?, ?, ?, 1)";
        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, password);
            stmt.setString(3, tipo);
            stmt.executeUpdate();
            System.out.println("Usuario " + nombreUsuario + " creado exitosamente.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }
    public boolean cambiarEstado(String nombreUsuario, boolean activo) {
        String sql = "UPDATE usuarios SET estado = ? WHERE nombre_usuario = ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, activo ? 1 : 0);
            stmt.setString(2, nombreUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean eliminar(String nombreUsuario) {
        String sql = "DELETE FROM usuarios WHERE nombre_usuario = ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public java.util.List<modelo.personas.Usuario> listarTodos() {
        java.util.List<modelo.personas.Usuario> lista = new java.util.ArrayList<>();
        String sql = "SELECT u.*, p.dni, p.nombre, p.apellido " +
                     "FROM usuarios u " +
                     "LEFT JOIN personas p ON u.id_persona = p.id";
        try (java.sql.Connection conn = conexion.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String user = rs.getString("nombre_usuario");
                String pass = rs.getString("contrasena");
                String tipo = rs.getString("tipo_usuario");
                boolean activo = rs.getInt("estado") == 1;
                modelo.personas.Cliente personaTemp = new modelo.personas.Cliente(
                    rs.getString("dni"), rs.getString("nombre"), rs.getString("apellido"), "N/A"
                );
                modelo.personas.Usuario u = null;
                switch (tipo) {
                    case "ADMIN": u = new modelo.personas.UsuarioAdministrador(user, pass); break;
                    case "EMPLEADO": u = new modelo.personas.UsuarioEmpleado(user, pass, null); break;
                    case "CLIENTE": u = new modelo.personas.UsuarioCliente(user, pass, personaTemp); break;
                }
                if (u != null) {
                    u.setEstado(activo);
                    lista.add(u);
                }
            }
        } catch (java.sql.SQLException e) { e.printStackTrace(); }
        return lista;
    }
}