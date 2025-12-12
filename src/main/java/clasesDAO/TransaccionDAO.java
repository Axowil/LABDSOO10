package clasesDAO;

import gestor.ConexionBD;
import modelo.transacciones.Transaccion;
import modelo.transacciones.Deposito; // Lo usamos para recrear objetos genéricos
import modelo.cuentas.CuentaAhorros; // Para la cuenta dummy

// --- ESTOS SON LOS IMPORTS QUE FALTABAN ---
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
// ------------------------------------------

public class TransaccionDAO {
    
    private ConexionBD conexion;

    public TransaccionDAO() {
        this.conexion = new ConexionBD();
    }

    // 1. Guardar transacción en BD
    public boolean registrar(Transaccion t) {
        String sql = "INSERT INTO transacciones (id_transaccion, tipo, monto, descripcion, cuenta_origen, estado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, t.getIdTransaccion());
            stmt.setString(2, t.getTipo());
            stmt.setDouble(3, t.getMonto());
            stmt.setString(4, t.getDescripcion());
            
            // Validación por si la cuenta es null (ej. depósito inicial manual)
            if (t.getCuentaOrigen() != null) {
                stmt.setString(5, t.getCuentaOrigen().getNumeroCuenta());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            
            stmt.setString(6, t.getEstado());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error registrando transacción: " + e.getMessage());
            return false;
        }
    }

    // 2. Listar TODAS las transacciones (Para reportes Admin)
    public List<Transaccion> listarTodas() {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacciones ORDER BY fecha_hora DESC";
        
        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                // Reconstruir objeto
                double monto = rs.getDouble("monto");
                String descripcion = rs.getString("descripcion");
                
                // Truco: Usamos Deposito como contenedor genérico para visualización
                // Creamos una cuenta falsa solo para que no falle el constructor
                CuentaAhorros dummy = new CuentaAhorros("N/A", 0, null);
                
                Transaccion t = new Deposito(monto, dummy, descripcion, null);
                // Aquí podrías setear ID, fecha y estado si tu clase Transaccion tiene setters
                // t.setIdTransaccion(rs.getString("id_transaccion"));
                
                lista.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    // 3. Listar últimas X transacciones (Para Auditoría / Dashboard)
    public List<Transaccion> listarUltimas(int cantidad) {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacciones ORDER BY fecha_hora DESC LIMIT ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, cantidad);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                double monto = rs.getDouble("monto");
                String descripcion = rs.getString("descripcion");
                CuentaAhorros dummy = new CuentaAhorros("N/A", 0, null);
                Transaccion t = new Deposito(monto, dummy, descripcion, null);
                lista.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}