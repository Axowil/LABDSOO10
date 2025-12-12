package modelo.transacciones;

import modelo.cuentas.Cuenta;
import modelo.personas.Usuario;

public class Transferencia extends Transaccion {
    private double comision;
    private String concepto;
    private String referencia;
    
    // Constructor
    public Transferencia(double monto, Cuenta cuentaOrigen, Cuenta cuentaDestino, 
                        String concepto, String referencia, String descripcion, 
                        Usuario usuarioEjecutor) {
        super(monto, cuentaOrigen, cuentaDestino, "TRANSFERENCIA", descripcion, usuarioEjecutor);
        this.concepto = concepto;
        this.referencia = referencia;
        this.comision = calcularComision();
    }
    
    // Getters específicos
    public double getComision() { return comision; }
    public String getConcepto() { return concepto; }
    public String getReferencia() { return referencia; }
    
    // Implementación de métodos abstractos
    @Override
    public boolean validar() {
        // Validaciones básicas
        if (monto <= 0) {
            rechazarTransaccion("El monto debe ser positivo");
            return false;
        }
        
        if (getCuentaOrigen() == null || getCuentaDestino() == null) {
            rechazarTransaccion("Cuentas origen y destino deben ser especificadas");
            return false;
        }
        
        if (getCuentaOrigen().equals(getCuentaDestino())) {
            rechazarTransaccion("No se puede transferir a la misma cuenta");
            return false;
        }
        
        if (!getCuentaOrigen().estaActiva() || !getCuentaDestino().estaActiva()) {
            rechazarTransaccion("Una de las cuentas no está activa");
            return false;
        }
        
        // Validar fondos disponibles (monto + comisión)
        double totalDebitar = monto + comision;
        if (totalDebitar > getCuentaOrigen().getSaldo()) {
            rechazarTransaccion("Fondos insuficientes para transferencia + comisión");
            return false;
        }
        
        // Validar límites de transferencia
        if (monto > getLimiteTransferencia()) {
            rechazarTransaccion("Excede el límite de transferencia");
            return false;
        }
        
        System.out.println("Validación de transferencia exitosa");
        return true;
    }
    
    @Override
    public boolean ejecutar() {
        if (!validar()) {
            return false;
        }
        
        try {
            // Ejecutar la transferencia
            boolean exito = getCuentaOrigen().transferir(getCuentaDestino(), monto);
            
            if (exito) {
                // Aplicar comisión si existe
                if (comision > 0) {
                    getCuentaOrigen().retirar(comision);
                    getCuentaOrigen().registrarMovimiento("COMISION_TRANSFERENCIA", -comision, 
                        "Comisión por transferencia " + getIdTransaccion());
                }
                
                completarTransaccion();
                registrarTransferencia();
                return true;
            } else {
                rechazarTransaccion("Error al procesar la transferencia entre cuentas");
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
            // Revertir la transferencia (transferir de vuelta)
            boolean exito = getCuentaDestino().transferir(getCuentaOrigen(), monto);
            
            if (exito) {
                // Revertir también la comisión
                if (comision > 0) {
                    getCuentaOrigen().depositar(comision);
                    getCuentaOrigen().registrarMovimiento("REVERSION_COMISION", comision, 
                        "Reversión de comisión " + getIdTransaccion());
                }
                
                setEstado(CANCELADA);
                System.out.println("Transferencia revertida exitosamente");
            } else {
                System.out.println("Error al revertir la transferencia");
            }
        } catch (Exception e) {
            System.out.println("Error al revertir transferencia: " + e.getMessage());
        }
    }
    
    // Métodos específicos de transferencia
    private double calcularComision() {
        // Comisión basada en el monto
        if (monto <= 1000) {
            return 1.0;
        } else if (monto <= 5000) {
            return 2.5;
        } else {
            return 5.0;
        }
    }
    
    private double getLimiteTransferencia() {
        // Límites de transferencia
        return 10000.0; // Límite máximo por transferencia
    }
    
    private void registrarTransferencia() {
        // Registro adicional específico para transferencias
        System.out.println("Transferencia registrada - Concepto: " + concepto + 
                         ", Referencia: " + referencia + ", Comisión: $" + comision);
    }
    
    public boolean esTransferenciaInterna() {
        // Simplificación: considerar interna si las cuentas tienen el mismo banco
        // En un sistema real, esto sería más complejo
        return getCuentaOrigen() != null && getCuentaDestino() != null &&
               getCuentaOrigen().getClass() == getCuentaDestino().getClass();
    }
    
    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("--- DETALLES ESPECÍFICOS DE TRANSFERENCIA ---");
        System.out.println("Concepto: " + concepto);
        System.out.println("Referencia: " + referencia);
        System.out.println("Comisión: $" + comision);
        System.out.println("Total a debitar: $" + (monto + comision));
        System.out.println("Es transferencia interna: " + (esTransferenciaInterna() ? "Sí" : "No"));
        if (getCuentaOrigen() != null) {
            System.out.println("Saldo cuenta origen: $" + getCuentaOrigen().getSaldo());
        }
        if (getCuentaDestino() != null) {
            System.out.println("Saldo cuenta destino: $" + getCuentaDestino().getSaldo());
        }
    }
}