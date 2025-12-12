package clasesDAO;

import gestor.ConexionBD;
import modelo.personas.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class EmpleadoDAO {
    private ConexionBD conexion;

    public EmpleadoDAO() {
        this.conexion = new ConexionBD();
    }

    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        // Hacemos JOIN para traer datos personales + datos laborales
        String sql = "SELECT p.*, e.cargo, e.departamento, e.salario, e.fecha_contratacion, e.turno, e.id_persona " +
                     "FROM personas p " +
                     "JOIN empleados e ON p.id = e.id_persona";
        
        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                // Recuperar fechas manejando posibles nulos
                Date sqlDateNac = rs.getDate("fecha_nacimiento");
                LocalDate fecNac = (sqlDateNac != null) ? sqlDateNac.toLocalDate() : LocalDate.now();
                
                // Construir el objeto Empleado usando el constructor completo
                Empleado emp = new Empleado(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    fecNac,
                    rs.getString("direccion"),
                    "EMP-" + rs.getInt("id_persona"), // Generamos un ID visual basado en la BD
                    rs.getString("cargo"),
                    rs.getString("departamento"),
                    rs.getDouble("salario"),
                    rs.getString("turno")
                );
                
                // Por defecto asumimos que están activos si están en la base de datos
                // Si tienes columna 'activo' en tabla empleados, úsala aquí:
                // emp.setActivo(rs.getBoolean("activo"));
                
                lista.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}