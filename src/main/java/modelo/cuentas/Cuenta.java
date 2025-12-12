package modelo.cuentas;

import modelo.personas.Cliente;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Cuenta {
    protected String numeroCuenta;
    protected double saldo;
    protected Cliente cliente;
    protected LocalDateTime fechaApertura;
    protected String estado; // ACTIVA, INACTIVA, BLOQUEADA, etc.
    protected List<String> movimientos;
    
    // Constantes para estados
    public static final String ACTIVA = "ACTIVA";
    public static final String INACTIVA = "INACTIVA";
    public static final String BLOQUEADA = "BLOQUEADA";
    public static final String SUSPENDIDA = "SUSPENDIDA";
    
    // Constructor
    public Cuenta(String numeroCuenta, double saldoInicial, Cliente cliente) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
        this.cliente = cliente;
        this.fechaApertura = LocalDateTime.now();
        this.estado = ACTIVA;
        this.movimientos = new ArrayList<>();
        
        // Registrar apertura de cuenta
        registrarMovimiento("APERTURA_DE_CUENTA", saldoInicial, "Saldo inicial");
    }
    
    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    
    public double getSaldo() { return saldo; }
    protected void setSaldo(double saldo) { this.saldo = saldo; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public LocalDateTime getFechaApertura() { return fechaApertura; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public List<String> getMovimientos() { return new ArrayList<>(movimientos); }
    
    // Métodos comunes a todas las cuentas
    public boolean depositar(double monto) {
        if (monto <= 0) {
            System.out.println("Error: El monto debe ser positivo");
            return false;
        }
        
        if (!estado.equals(ACTIVA)) {
            System.out.println("Error: La cuenta no está activa");
            return false;
        }
        
        saldo += monto;
        registrarMovimiento("DEPOSITO", monto, "Depósito realizado");
        System.out.println("Depósito exitoso: +$" + monto + " | Saldo actual: $" + saldo);
        return true;
    }
    
    public abstract boolean retirar(double monto);
    public abstract boolean transferir(Cuenta cuentaDestino, double monto);
    
    // Métodos de estado
    public void activarCuenta() {
        this.estado = ACTIVA;
        registrarMovimiento("CAMBIO_ESTADO", 0, "Cuenta activada");
        System.out.println("Cuenta " + numeroCuenta + " activada");
    }
    
    public void desactivarCuenta() {
        this.estado = INACTIVA;
        registrarMovimiento("CAMBIO_ESTADO", 0, "Cuenta desactivada");
        System.out.println("Cuenta " + numeroCuenta + " desactivada");
    }
    
    public void bloquearCuenta() {
        this.estado = BLOQUEADA;
        registrarMovimiento("CAMBIO_ESTADO", 0, "Cuenta bloqueada");
        System.out.println("Cuenta " + numeroCuenta + " bloqueada");
    }
    
    public boolean estaActiva() {
        return estado.equals(ACTIVA);
    }
    
    // Métodos de movimientos
    public void registrarMovimiento(String tipo, double monto, String descripcion) {
        String movimiento = String.format("[%s] %s: $%.2f - %s | Saldo: $%.2f",
                LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                tipo, monto, descripcion, saldo);
        movimientos.add(movimiento);
    }
    
    public void consultarMovimientos() {
        System.out.println("=== MOVIMIENTOS DE CUENTA " + numeroCuenta + " ===");
        if (movimientos.isEmpty()) {
            System.out.println("No hay movimientos registrados");
        } else {
            for (String movimiento : movimientos) {
                System.out.println(movimiento);
            }
        }
        System.out.println("Saldo actual: $" + saldo);
    }
    
    public void consultarSaldo() {
        System.out.println("=== SALDO DE CUENTA ===");
        System.out.println("Cuenta: " + numeroCuenta);
        System.out.println("Cliente: " + (cliente != null ? cliente.getNombreCompleto() : "N/A"));
        System.out.println("Saldo disponible: $" + saldo);
        System.out.println("Estado: " + estado);
        System.out.println("Fecha apertura: " + fechaApertura.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    
    // Método para generar resumen
    public void generarResumen() {
        System.out.println("=== RESUMEN DE CUENTA ===");
        System.out.println("Número de cuenta: " + numeroCuenta);
        System.out.println("Tipo de cuenta: " + this.getClass().getSimpleName());
        System.out.println("Cliente: " + (cliente != null ? cliente.getNombreCompleto() : "N/A"));
        System.out.println("Saldo: $" + saldo);
        System.out.println("Estado: " + estado);
        System.out.println("Fecha apertura: " + fechaApertura.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Total movimientos: " + movimientos.size());
    }
    
    @Override
    public String toString() {
        return numeroCuenta + " - " + (cliente != null ? cliente.getNombreCompleto() : "Sin cliente") + " - Saldo: $" + saldo;
    }
}