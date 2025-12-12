package clasesDAO;

import gestor.ConexionBD;
import modelo.personas.Empleado;
import java.sql.*;
import java.util.*;
import java.time.LocalDate;

public class EmpleadoDAO {
    private ConexionBD conexion;

    public EmpleadoDAO() {
        this.conexion = new ConexionBD();
    }
    public boolean guardar(Empleado c) {
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
            stmtP.setDate(7, java.sql.Date.valueOf(c.getFechaNacimiento()));
            stmtP.executeUpdate();
            ResultSet rs = stmtP.getGeneratedKeys();
            int idPersona = 0;
            if (rs.next()) idPersona = rs.getInt(1);
            String sqlEmpleado = "INSERT INTO empleados (id_persona, cargo, departamento, salario, fecha_contratacion, turno) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtC = conn.prepareStatement(sqlEmpleado);
            stmtC.setInt(1, idPersona);
            stmtC.setString(2, c.getCargo());
            stmtC.setString(3, c.getDepartamento());
            stmtC.setDouble(4, c.getSalario());
            stmtC.setDate(5, java.sql.Date.valueOf(c.getFechaContratacion()));
            stmtC.setString(6, c.getTurno());
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
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT p.*, e.cargo, e.departamento, e.salario, e.fecha_contratacion, e.turno, e.id_persona " +
                     "FROM personas p " +
                     "JOIN empleados e ON p.id = e.id_persona";
        
        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                java.sql.Date sqlDateNac = rs.getDate("fecha_nacimiento");
                LocalDate fecNac = (sqlDateNac != null) ? sqlDateNac.toLocalDate() : LocalDate.now();
                Empleado emp = new Empleado(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    fecNac,
                    rs.getString("direccion"),
                    "EMP-" + rs.getInt("id_persona"),
                    rs.getString("cargo"),
                    rs.getString("departamento"),
                    rs.getDouble("salario"),
                    rs.getString("turno")
                );
                lista.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}