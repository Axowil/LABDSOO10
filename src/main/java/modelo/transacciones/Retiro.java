package modelo.transacciones;

import modelo.cuentas.Cuenta;
import modelo.personas.Usuario;

public class Retiro extends Transaccion {
    private String metodoRetiro; // CAJERO, VENTANILLA, etc.
    private String ubicacion;
    private boolean requiereValidacion;
    
    // Constructor
    public Retiro(double monto, Cuenta cuentaOrigen, String metodoRetiro, String ubicacion, 
                  String descripcion, Usuario usuarioEjecutor) {
        super(monto, cuentaOrigen, "RETIRO", descripcion, usuarioEjecutor);
        this.metodoRetiro = metodoRetiro;
        this.ubicacion = ubicacion;
        this.requiereValidacion = monto > 1000; // Retiros grandes requieren validación
    }
    
    // Getters específicos
    public String getMetodoRetiro() { return metodoRetiro; }
    public void setMetodoRetiro(String metodoRetiro) { this.metodoRetiro = metodoRetiro; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public boolean isRequiereValidacion() { return requiereValidacion; }
    
    // Implementación de métodos abstractos
    @Override
    public boolean validar() {
        // Validaciones básicas
        if (monto <= 0) {
            rechazarTransaccion("El monto debe ser positivo");
            return false;
        }
        
        if (getCuentaOrigen() == null) {
            rechazarTransaccion("Cuenta origen no especificada");
            return false;
        }
        
        if (!getCuentaOrigen().estaActiva()) {
            rechazarTransaccion("La cuenta origen no está activa");
            return false;
        }
        
        // Validar límites de retiro
        if (monto > getLimiteRetiroDiario()) {
            rechazarTransaccion("Excede el límite de retiro diario");
            return false;
        }
        
        // Validar fondos disponibles
        if (monto > getCuentaOrigen().getSaldo()) {
            rechazarTransaccion("Fondos insuficientes");
            return false;
        }
        
        // Validación adicional para retiros grandes
        if (requiereValidacion && !validarRetiroGrande()) {
            rechazarTransaccion("Validación de retiro grande falló");
            return false;
        }
        
        System.out.println("Validación de retiro exitosa");
        return true;
    }
    
    @Override
    public boolean ejecutar() {
        if (!validar()) {
            return false;
        }
        
        try {
            // Ejecutar el retiro en la cuenta origen
            boolean exito = getCuentaOrigen().retirar(monto);
            
            if (exito) {
                completarTransaccion();
                registrarRetiro();
                return true;
            } else {
                rechazarTransaccion("Error al procesar el retiro en la cuenta");
                return false;
            }
        } catch (Exception e) {
            rechazarTransaccion("Error inesperado: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void revertir() {
        if (!estaCompletada()) {
            System.out.println("No se puede revertir una transacción no completada");
            return;
        }
        
        try {
            // Revertir el retiro (depositar el monto retirado)
            boolean exito = getCuentaOrigen().depositar(monto);
            
            if (exito) {
                setEstado(CANCELADA);
                System.out.println("Retiro revertido exitosamente");
                
                // Registrar movimiento de reversión
                getCuentaOrigen().registrarMovimiento("REVERSION_RETIRO", monto, 
                    "Reversión de retiro " + getIdTransaccion());
            } else {
                System.out.println("Error al revertir el retiro");
            }
        } catch (Exception e) {
            System.out.println("Error al revertir retiro: " + e.getMessage());
        }
    }
    
    // Métodos específicos de retiro
    private double getLimiteRetiroDiario() {
        // Límites basados en el método de retiro
        switch (metodoRetiro.toUpperCase()) {
            case "CAJERO":
                return 1000.0;
            case "VENTANILLA":
                return 5000.0;
            case "OFICINA":
                return 10000.0;
            default:
                return 1000.0;
        }
    }
    
    private boolean validarRetiroGrande() {
        if (monto > 5000) {
            System.out.println("Retiro grande detectado - requiere autorización del gerente");
            // Simular validación (en un sistema real, esto sería más complejo)
            return Math.random() > 0.1; // 90% de probabilidad de aprobación
        }
        return true;
    }
    
    private void registrarRetiro() {
        // Registro adicional específico para retiros
        System.out.println("Retiro registrado - Método: " + metodoRetiro + ", Ubicación: " + ubicacion);
        
        if (requiereValidacion) {
            System.out.println("Retiro grande - registro de validación completado");
        }
    }
    
    public boolean esRetiroCajero() {
        return "CAJERO".equalsIgnoreCase(metodoRetiro);
    }
    
    public boolean esRetiroVentanilla() {
        return "VENTANILLA".equalsIgnoreCase(metodoRetiro);
    }
    
    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("--- DETALLES ESPECÍFICOS DE RETIRO ---");
        System.out.println("Método de retiro: " + metodoRetiro);
        System.out.println("Ubicación: " + ubicacion);
        System.out.println("Requiere validación: " + (requiereValidacion ? "Sí" : "No"));
        System.out.println("Límite diario: $" + getLimiteRetiroDiario());
        if (getCuentaOrigen() != null) {
            System.out.println("Saldo actual cuenta: $" + getCuentaOrigen().getSaldo());
        }
    }
}