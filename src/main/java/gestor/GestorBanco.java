package gestor;

import clasesDAO.*; // Importante para ver los DAOs
import modelo.cuentas.*;
import modelo.transacciones.*;
import modelo.personas.*;
import java.util.ArrayList;
import java.util.List;

public class GestorBanco {
    private GestorUsuarios gestorUsuarios;
    private CuentaDAO cuentaDAO;
    private TransaccionDAO transaccionDAO;

    public GestorBanco() {
        this.gestorUsuarios = new GestorUsuarios();
        this.cuentaDAO = new CuentaDAO();
        this.transaccionDAO = new TransaccionDAO();
    }

    public GestorUsuarios getGestorUsuarios() { return gestorUsuarios; }

    // =========================================================
    //  MÉTODOS RESTAURADOS PARA QUE COMPILEN TUS REPORTES
    // =========================================================

    // 1. getCuentas(): Va a la BD y trae todas
    public List<Cuenta> getCuentas() {
        return cuentaDAO.listarTodas();
    }

    // 2. getTransacciones(): Va a la BD y trae todas
    public List<Transaccion> getTransacciones() {
        return transaccionDAO.listarTodas();
    }
    
    // 3. getTransaccionesRecientes(): Para el Dashboard
    public List<Transaccion> getTransaccionesRecientes(int cantidad) {
        return transaccionDAO.listarUltimas(cantidad);
    }

    // 4. getTitularidades(): Generamos esto dinámicamente para no romper FrmAuditoria
    public List<Titularidad> getTitularidades() {
        List<Titularidad> lista = new ArrayList<>();
        // Convertimos cada cuenta en una titularidad simple
        for (Cuenta c : getCuentas()) {
            lista.add(new Titularidad(c, c.getCliente()));
        }
        return lista;
    }

    // =========================================================
    //  LÓGICA DEL BANCO (Login, Depósitos, etc.)
    // =========================================================

    public Cuenta buscarCuenta(String numeroCuenta) {
        return cuentaDAO.buscar(numeroCuenta); 
    }
    
    public boolean existeCuenta(String numeroCuenta) {
        return buscarCuenta(numeroCuenta) != null;
    }

    public Transaccion realizarDeposito(String numeroCuenta, double monto, String descripcion) {
        if (!gestorUsuarios.tienePermiso("realizar_depositos")) return null;
        
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta == null) return null;

        Usuario usuarioEjecutor = gestorUsuarios.getUsuarioActual();
        Deposito deposito = new Deposito(monto, cuenta, descripcion, usuarioEjecutor);
        
        if (deposito.ejecutar()) {
            cuentaDAO.actualizarSaldo(cuenta);
            transaccionDAO.registrar(deposito);
            return deposito;
        }
        return null;
    }
    // En gestor/GestorBanco.java
public boolean eliminarCuenta(String numeroCuenta) {
    // Podrías agregar validaciones de negocio aquí, ejemplo:
    // - No permitir eliminar si la cuenta tiene saldo distinto de 0
    Cuenta cuenta = buscarCuenta(numeroCuenta);
    if (cuenta == null) {
        return false;
    }
    if (cuenta.getSaldo() != 0) {
        // Regla de negocio básica: solo eliminar cuentas con saldo 0
        return false;
    }
    return cuentaDAO.eliminarCuenta(numeroCuenta);
}

    public Transaccion realizarRetiro(String numeroCuenta, double monto, String metodo, String ubicacion) {
        if (!gestorUsuarios.tienePermiso("realizar_retiros")) return null;
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta == null) return null;
        
        Usuario usuario = gestorUsuarios.getUsuarioActual();
        Retiro retiro = new Retiro(monto, cuenta, metodo, ubicacion, "Retiro", usuario);
        
        if (retiro.ejecutar()) {
            cuentaDAO.actualizarSaldo(cuenta);
            transaccionDAO.registrar(retiro);
            return retiro;
        }
        return null;
    }
    
    public Transaccion realizarTransferencia(String origen, String destino, double monto, String concepto, String ref) {
        if (!gestorUsuarios.tienePermiso("realizar_transferencias")) return null;
        Cuenta cOrigen = buscarCuenta(origen);
        Cuenta cDestino = buscarCuenta(destino);
        
        if (cOrigen == null || cDestino == null) return null;
        
        Usuario usuario = gestorUsuarios.getUsuarioActual();
        Transferencia t = new Transferencia(monto, cOrigen, cDestino, concepto, ref, "Transf", usuario);
        
        if (t.ejecutar()) {
            cuentaDAO.actualizarSaldo(cOrigen);
            cuentaDAO.actualizarSaldo(cDestino);
            transaccionDAO.registrar(t);
            return t;
        }
        return null;
    }

    // Métodos para crear cuentas (Solución al error "cannot find symbol guardarCuenta")
    public boolean crearCuentaAhorros(String numero, double saldo, Cliente cliente) {
        if (existeCuenta(numero)) return false;
        CuentaAhorros nueva = new CuentaAhorros(numero, saldo, cliente);
        return cuentaDAO.guardarCuenta(nueva); // <--- Esto llama al DAO
    }

    public boolean crearCuentaCorriente(String numero, double saldo, Cliente cliente) {
        if (existeCuenta(numero)) return false;
        CuentaCorriente nueva = new CuentaCorriente(numero, saldo, cliente);
        return cuentaDAO.guardarCuenta(nueva); // <--- Esto llama al DAO
    }
    
    // En gestor/GestorBanco.java

    public List<Cuenta> obtenerCuentasDeCliente(String dniCliente) {
        // Antes devolvía una lista vacía, ahora llama al DAO
        return cuentaDAO.listarPorCliente(dniCliente);
    }
}