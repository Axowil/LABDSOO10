package clasesDAO;

import gestor.ConexionBD;
import modelo.cuentas.*;
import modelo.personas.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {
    
    private ConexionBD conexion;

    public CuentaDAO() {
        this.conexion = new ConexionBD();
    }

    // 1. MÉTODO QUE TE FALTA: Guardar una cuenta nueva
    public boolean guardarCuenta(Cuenta cuenta) {
        String sql = "INSERT INTO cuentas (numero_cuenta, id_cliente, tipo_cuenta, saldo, estado, tasa_interes, sobregiro_maximo) "
           + "VALUES (?, (SELECT id FROM personas WHERE dni = ?), ?, ?, ?, ?, ?)";

        
        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cuenta.getNumeroCuenta());
            // Usamos el DNI del cliente para encontrar su ID en la BD automáticamente
            stmt.setString(2, cuenta.getCliente().getDni()); 
            
            if (cuenta instanceof CuentaAhorros) {
                stmt.setString(3, "AHORROS");
                stmt.setDouble(6, ((CuentaAhorros) cuenta).getTasaInteres());
                stmt.setObject(7, null); // Sobregiro null
            } else if (cuenta instanceof CuentaCorriente) {
                stmt.setString(3, "CORRIENTE");
                stmt.setObject(6, null); // Tasa null
                stmt.setDouble(7, ((CuentaCorriente) cuenta).getSobregiroMaximo());
            } else {
                stmt.setString(3, "GENERICA");
            }
            
            stmt.setDouble(4, cuenta.getSaldo());
            stmt.setString(5, "ACTIVA"); // Estado inicial
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al guardar cuenta: " + e.getMessage());
            return false;
        }
    }
    public boolean eliminarCuenta(String numeroCuenta) {
    String sql = "DELETE FROM cuentas WHERE numero_cuenta = ?";
    try (Connection conn = conexion.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, numeroCuenta);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Error al eliminar cuenta: " + e.getMessage());
        return false;
    }
}
    // 2. Buscar cuenta por número
    public Cuenta buscar(String numeroCuenta) {
        String sql = "SELECT c.*, p.dni, p.nombre, p.apellido " +
                     "FROM cuentas c " +
                     "JOIN personas p ON c.id_cliente = p.id " + // Asumiendo relación por ID
                     "WHERE c.numero_cuenta = ?";
        
        // NOTA: Si tu BD usa 'dni_cliente' en vez de ID, cambia el JOIN
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeroCuenta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Reconstruir Cliente
                Cliente cliente = new Cliente(
                    rs.getString("dni"), 
                    rs.getString("nombre"), 
                    rs.getString("apellido"),
                    "N/A" // Argumento extra para constructor temporal
                );

                String tipo = rs.getString("tipo_cuenta");
                double saldo = rs.getDouble("saldo");
                Cuenta cuenta;

                if ("AHORROS".equalsIgnoreCase(tipo)) {
                    double tasa = rs.getDouble("tasa_interes");
                    cuenta = new CuentaAhorros(numeroCuenta, saldo, cliente);
                    ((CuentaAhorros)cuenta).setTasaInteres(tasa);
                } else {
                    double sobregiro = rs.getDouble("sobregiro_maximo");
                    cuenta = new CuentaCorriente(numeroCuenta, saldo, cliente);
                    ((CuentaCorriente)cuenta).setSobregiroMaximo(sobregiro);
                }
                
                cuenta.setEstado(rs.getString("estado"));
                return cuenta;
            }
        } catch (SQLException e) {
            System.out.println("Error buscando cuenta: " + e.getMessage());
        }
        return null;
    }

    // 3. Actualizar saldo
    public boolean actualizarSaldo(Cuenta cuenta) {
        String sql = "UPDATE cuentas SET saldo = ? WHERE numero_cuenta = ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, cuenta.getSaldo());
            stmt.setString(2, cuenta.getNumeroCuenta());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Listar TODAS las cuentas (Para reportes de Admin)
    public List<Cuenta> listarTodas() {
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM cuentas"; 
        // Nota: Idealmente harías un JOIN con clientes aquí también
        
        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Cliente dummy = new Cliente("???", "Cliente", "BD", "N/A");
                String num = rs.getString("numero_cuenta");
                double saldo = rs.getDouble("saldo");
                String tipo = rs.getString("tipo_cuenta");
                
                Cuenta c;
                if ("AHORROS".equals(tipo)) c = new CuentaAhorros(num, saldo, dummy);
                else c = new CuentaCorriente(num, saldo, dummy);
                
                c.setEstado(rs.getString("estado"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 5. Listar cuentas de un cliente específico (Para 'Mis Cuentas')
// En clasesDAO/CuentaDAO.java

    public List<Cuenta> listarPorCliente(String dniCliente) {
        List<Cuenta> lista = new ArrayList<>();
        // Consultamos las cuentas uniendo con la tabla de personas para filtrar por DNI
        String sql = "SELECT c.* FROM cuentas c " +
                     "JOIN personas p ON c.id_cliente = p.id " +
                     "WHERE p.dni = ?";

        try (Connection conn = conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dniCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Reconstruimos el objeto Cuenta
                // Nota: Usamos un cliente dummy porque ya sabemos de quién son las cuentas
                Cliente clienteTemp = new Cliente(dniCliente, "Usuario", "Actual", "N/A");

                String numero = rs.getString("numero_cuenta");
                double saldo = rs.getDouble("saldo");
                String tipo = rs.getString("tipo_cuenta");
                String estado = rs.getString("estado");

                Cuenta cuenta;
                if ("AHORROS".equalsIgnoreCase(tipo)) {
                    cuenta = new CuentaAhorros(numero, saldo, clienteTemp);
                } else {
                    cuenta = new CuentaCorriente(numero, saldo, clienteTemp);
                }
                cuenta.setEstado(estado);
                lista.add(cuenta);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar cuentas del cliente: " + e.getMessage());
        }
        return lista;
    }
}