package modelo.transacciones;

import modelo.personas.Usuario;
import java.time.LocalDateTime;

public class Validado {
    private Transaccion transaccion;
    private Usuario validador;
    private LocalDateTime fechaValidacion;
    private String estadoValidacion; // APROBADA, RECHAZADA, PENDIENTE
    private String comentarios;
    private int nivelValidacion; // 1 = Básico, 2 = Supervisor, 3 = Gerente
    
    // Constantes para estados de validación
    public static final String APROBADA = "APROBADA";
    public static final String RECHAZADA = "RECHAZADA";
    public static final String PENDIENTE = "PENDIENTE";
    
    // Constructor
    public Validado(Transaccion transaccion, Usuario validador, int nivelValidacion) {
        this.transaccion = transaccion;
        this.validador = validador;
        this.fechaValidacion = LocalDateTime.now();
        this.estadoValidacion = PENDIENTE;
        this.comentarios = "";
        this.nivelValidacion = nivelValidacion;
    }
    
    // Getters y Setters
    public Transaccion getTransaccion() { return transaccion; }
    public void setTransaccion(Transaccion transaccion) { this.transaccion = transaccion; }
    
    public Usuario getValidador() { return validador; }
    public void setValidador(Usuario validador) { this.validador = validador; }
    
    public LocalDateTime getFechaValidacion() { return fechaValidacion; }
    
    public String getEstadoValidacion() { return estadoValidacion; }
    public void setEstadoValidacion(String estadoValidacion) { this.estadoValidacion = estadoValidacion; }
    
    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
    
    public int getNivelValidacion() { return nivelValidacion; }
    public void setNivelValidacion(int nivelValidacion) { this.nivelValidacion = nivelValidacion; }
    
    // Métodos de validación
    public boolean aprobar(String comentarios) {
        if (transaccion == null) {
            System.out.println("Error: No hay transacción asociada");
            return false;
        }
        
        this.estadoValidacion = APROBADA;
        this.comentarios = comentarios;
        this.fechaValidacion = LocalDateTime.now();
        
        System.out.println("Transacción " + transaccion.getIdTransaccion() + " APROBADA por " + 
                         validador.getNombreUsuario() + " (Nivel " + nivelValidacion + ")");
        System.out.println("Comentarios: " + comentarios);
        
        // Si la transacción estaba pendiente, ejecutarla después de la aprobación
        if (transaccion.estaPendiente()) {
            return transaccion.ejecutar();
        }
        
        return true;
    }
    
    public boolean rechazar(String comentarios) {
        if (transaccion == null) {
            System.out.println("Error: No hay transacción asociada");
            return false;
        }
        
        this.estadoValidacion = RECHAZADA;
        this.comentarios = comentarios;
        this.fechaValidacion = LocalDateTime.now();
        
        System.out.println("Transacción " + transaccion.getIdTransaccion() + " RECHAZADA por " + 
                         validador.getNombreUsuario() + " (Nivel " + nivelValidacion + ")");
        System.out.println("Comentarios: " + comentarios);
        
        // Rechazar la transacción
        transaccion.rechazarTransaccion("Validación rechazada: " + comentarios);
        return true;
    }
    
    public boolean requiereValidacion() {
        return estadoValidacion.equals(PENDIENTE);
    }
    
    public boolean estaAprobada() {
        return estadoValidacion.equals(APROBADA);
    }
    
    public boolean estaRechazada() {
        return estadoValidacion.equals(RECHAZADA);
    }
    
    // Métodos de información
    public void mostrarInformacionValidacion() {
        System.out.println("=== INFORMACIÓN DE VALIDACIÓN ===");
        System.out.println("Transacción: " + (transaccion != null ? transaccion.getIdTransaccion() : "N/A"));
        System.out.println("Validador: " + (validador != null ? validador.getNombreUsuario() : "N/A"));
        System.out.println("Fecha validación: " + fechaValidacion.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        System.out.println("Estado validación: " + estadoValidacion);
        System.out.println("Nivel validación: " + nivelValidacion);
        System.out.println("Comentarios: " + comentarios);
        
        if (transaccion != null) {
            System.out.println("--- DETALLES DE TRANSACCIÓN ---");
            System.out.println("Tipo: " + transaccion.getTipo());
            System.out.println("Monto: $" + transaccion.getMonto());
            System.out.println("Estado transacción: " + transaccion.getEstado());
        }
    }
    
    public String getResumenValidacion() {
        return String.format("Validación[Trans: %s, Validador: %s, Estado: %s, Nivel: %d]",
                transaccion != null ? transaccion.getIdTransaccion() : "N/A",
                validador != null ? validador.getNombreUsuario() : "N/A",
                estadoValidacion, nivelValidacion);
    }
    
    @Override
    public String toString() {
        return getResumenValidacion();
    }
}