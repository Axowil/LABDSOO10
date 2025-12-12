package clasesDAO;

import gestor.ConexionBD;
import modelo.transacciones.Transaccion;
import modelo.transacciones.Deposito;
import modelo.cuentas.CuentaAhorros;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class TransaccionDAO {
    private ConexionBD conexion;
    public TransaccionDAO() { this.conexion = new ConexionBD(); }
    public boolean registrar(Transaccion t) {
        String sql =
        "INSERT INTO transacciones (id_transaccion, tipo, monto, descripcion, cuenta_origen, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = conexion.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getIdTransaccion());
            stmt.setString(2, t.getTipo());
            stmt.setDouble(3, t.getMonto());
            stmt.setString(4, t.getDescripcion());
            if (t.getCuentaOrigen() != null) {
                stmt.setString(5, t.getCuentaOrigen().getNumeroCuenta());
            } else { stmt.setNull(5, Types.VARCHAR);}
            stmt.setString(6, t.getEstado());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error registrando transacción: " + e.getMessage());
            return false;
        }
    }
    public List<Transaccion> listarTodas() {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacciones ORDER BY fecha_hora DESC";
        try (Connection conn = conexion.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                double monto = rs.getDouble("monto");
                String descripcion = rs.getString("descripcion");
                CuentaAhorros dummy = new CuentaAhorros(
                    "N/A",
                    0,
                    null
                );
                Transaccion t = new Deposito(monto, dummy, descripcion, null);
                lista.add(t);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
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
                CuentaAhorros dummy = new CuentaAhorros(
                    "N/A",
                    0,
                    null
                );
                Transaccion t = new Deposito(monto, dummy, descripcion, null);
                lista.add(t);
            }
        } catch (SQLException e) { e.printStackTrace();}
        return lista;
    }
}