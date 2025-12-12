package modelo.transacciones;

import modelo.cuentas.Cuenta;
import modelo.personas.Usuario;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Transaccion {
    protected String idTransaccion;
    protected double monto;
    protected LocalDateTime fechaHora;
    protected String estado; // PENDIENTE, COMPLETADA, RECHAZADA, CANCELADA
    protected String tipo; // DEPOSITO, RETIRO, TRANSFERENCIA, etc.
    protected Cuenta cuentaOrigen;
    protected Cuenta cuentaDestino;
    protected String descripcion;
    protected Usuario usuarioEjecutor;
    
    // Constantes para estados
    public static final String PENDIENTE = "PENDIENTE";
    public static final String COMPLETADA = "COMPLETADA";
    public static final String RECHAZADA = "RECHAZADA";
    public static final String CANCELADA = "CANCELADA";
    
    // Constructor para transacciones simples (depósito/retiro)
    public Transaccion(double monto, Cuenta cuentaOrigen, String tipo, String descripcion, Usuario usuarioEjecutor) {
        this.idTransaccion = generarIdTransaccion();
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
        this.estado = PENDIENTE;
        this.tipo = tipo;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = null;
        this.descripcion = descripcion;
        this.usuarioEjecutor = usuarioEjecutor;
    }
    
    // Constructor para transferencias
    public Transaccion(double monto, Cuenta cuentaOrigen, Cuenta cuentaDestino, String tipo, String descripcion, Usuario usuarioEjecutor) {
        this(monto, cuentaOrigen, tipo, descripcion, usuarioEjecutor);
        this.cuentaDestino = cuentaDestino;
    }
    
    // Generar ID único para transacción
    private String generarIdTransaccion() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    // Getters y Setters
    public String getIdTransaccion() { return idTransaccion; }
    
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    
    public LocalDateTime getFechaHora() { return fechaHora; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public Cuenta getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(Cuenta cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }
    
    public Cuenta getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(Cuenta cuentaDestino) { this.cuentaDestino = cuentaDestino; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Usuario getUsuarioEjecutor() { return usuarioEjecutor; }
    public void setUsuarioEjecutor(Usuario usuarioEjecutor) { this.usuarioEjecutor = usuarioEjecutor; }
    
    // Métodos abstractos que deben implementar las clases hijas
    public abstract boolean ejecutar();
    public abstract boolean validar();
    public abstract void revertir();
    
    // Métodos comunes
    public void completarTransaccion() {
        this.estado = COMPLETADA;
        System.out.println("Transacción " + idTransaccion + " completada exitosamente");
    }
    
    public void rechazarTransaccion(String motivo) {
        this.estado = RECHAZADA;
        this.descripcion += " | RECHAZADA: " + motivo;
        System.out.println("Transacción " + idTransaccion + " rechazada: " + motivo);
    }
    
    public void cancelarTransaccion() {
        this.estado = CANCELADA;
        System.out.println("Transacción " + idTransaccion + " cancelada");
    }
    
    public boolean estaCompletada() {
        return estado.equals(COMPLETADA);
    }
    
    public boolean estaPendiente() {
        return estado.equals(PENDIENTE);
    }
    
    // Método para mostrar información de la transacción
    public void mostrarInformacion() {
        System.out.println("=== INFORMACIÓN DE TRANSACCIÓN ===");
        System.out.println("ID: " + idTransaccion);
        System.out.println("Tipo: " + tipo);
        System.out.println("Monto: $" + monto);
        System.out.println("Fecha/Hora: " + fechaHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        System.out.println("Estado: " + estado);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Cuenta Origen: " + (cuentaOrigen != null ? cuentaOrigen.getNumeroCuenta() : "N/A"));
        System.out.println("Cuenta Destino: " + (cuentaDestino != null ? cuentaDestino.getNumeroCuenta() : "N/A"));
        System.out.println("Ejecutado por: " + (usuarioEjecutor != null ? usuarioEjecutor.getNombreUsuario() : "N/A"));
    }
    
    // Método para formato corto
    public String toStringCorto() {
        return String.format("%s | %s | $%.2f | %s", 
                idTransaccion, tipo, monto, estado);
    }
    
    @Override
    public String toString() {
        return String.format("Transaccion[%s, %s, $%.2f, %s]", 
                idTransaccion, tipo, monto, estado);
    }
}