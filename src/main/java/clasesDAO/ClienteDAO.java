package clasesDAO;

import gestor.ConexionBD;
import modelo.personas.Cliente;
import java.sql.*;
import java.util.*;
public class ClienteDAO {
    private ConexionBD conexion;
    public ClienteDAO() {
        this.conexion = new ConexionBD();
    }
    public boolean guardar(Cliente c) {
        Connection conn = null;
        try {
            conn = conexion.getConnection();
            conn.setAutoCommit(false);
            String sqlPersona = "INSERT INTO personas (dni, nombre, apellido, email, telefono, direccion, fecha_nacimiento) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stmtP = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            stmtP.setString(1, c.getDni());
            stmtP.setString(2, c.getNombre());
            stmtP.setString(3, c.getApellido());
            stmtP.setString(4, c.getEmail());
            stmtP.setString(5, c.getTelefono());
            stmtP.setString(6, c.getDireccion());
            stmtP.setDate(7, java.sql.Date.valueOf(c.getFechaNacimiento())); // Asumiendo que usas LocalDate
            stmtP.executeUpdate();
            ResultSet rs = stmtP.getGeneratedKeys();
            int idPersona = 0;
            if (rs.next()) idPersona = rs.getInt(1);
            String sqlCliente = "INSERT INTO clientes (id_persona, calificacion_crediticia) VALUES (?, ?)";
            PreparedStatement stmtC = conn.prepareStatement(sqlCliente);
            stmtC.setInt(1, idPersona);
            stmtC.setString(2, c.getCategoria());
            stmtC.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.calificacion_crediticia FROM personas p JOIN clientes c ON p.id = c.id_persona";
        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                Cliente c = new Cliente(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getString("direccion"),
                    "CLI-"+rs.getInt("id"),
                    rs.getString("calificacion_crediticia"),
                    1000.0
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    public Cliente buscarPorDni(String dni) {
        String sql = "SELECT p.*, c.calificacion_crediticia FROM personas p JOIN clientes c ON p.id = c.id_persona WHERE p.dni = ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getString("direccion"),
                    "CLI-"+rs.getInt("id"),
                    rs.getString("calificacion_crediticia"),
                    1000.0
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    public static void actualizarCalificacionDB(String idCliente, String nuevaCalificacion) throws Exception {
        String SQL = "UPDATE clientes SET calificacion_crediticia = ? WHERE id_persona = ?";
        ConexionBD conexion = new ConexionBD();
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            
            ps.setString(1, nuevaCalificacion);
            ps.setString(2, idCliente);
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new Exception("No se encontró el cliente con ID: " + idCliente);
            }
        }
    }
}