package modelo.transacciones;

import modelo.cuentas.Cuenta;
import modelo.personas.Usuario;

public class Deposito extends Transaccion {
    
    // Constructor
    public Deposito(double monto, Cuenta cuentaDestino, String descripcion, Usuario usuarioEjecutor) {
        super(monto, null, cuentaDestino, "DEPOSITO", descripcion, usuarioEjecutor);
    }
    
    // Implementación de métodos abstractos
    @Override
    public boolean validar() {
        // Validaciones básicas
        if (monto <= 0) {
            rechazarTransaccion("El monto debe ser positivo");
            return false;
        }
        
        if (getCuentaDestino() == null) {
            rechazarTransaccion("Cuenta destino no especificada");
            return false;
        }
        
        if (!getCuentaDestino().estaActiva()) {
            rechazarTransaccion("La cuenta destino no está activa");
            return false;
        }
        
        if (monto > 10000) { // Límite para depósitos sin validación adicional
            System.out.println("Advertencia: Depósito mayor a $10,000 - requiere validación adicional");
        }
        
        System.out.println("Validación de depósito exitosa");
        return true;
    }
    
    @Override
    public boolean ejecutar() {
        if (!validar()) {
            return false;
        }
        
        try {
            // Ejecutar el depósito en la cuenta destino
            boolean exito = getCuentaDestino().depositar(monto);
            
            if (exito) {
                completarTransaccion();
                registrarEnCuenta();
                return true;
            } else {
                rechazarTransaccion("Error al procesar el depósito en la cuenta");
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
            // Revertir el depósito (retirar el monto depositado)
            boolean exito = getCuentaDestino().retirar(monto);
            
            if (exito) {
                setEstado(CANCELADA);
                System.out.println("Depósito revertido exitosamente");
                
                // Registrar movimiento de reversión
                getCuentaDestino().registrarMovimiento("REVERSION_DEPOSITO", -monto, 
                    "Reversión de depósito " + getIdTransaccion());
            } else {
                System.out.println("Error al revertir el depósito - saldo insuficiente");
            }
        } catch (Exception e) {
            System.out.println("Error al revertir depósito: " + e.getMessage());
        }
    }
    
    private void registrarEnCuenta() {
        // El registro del movimiento ya se hace en el método depositar() de la cuenta
        // Aquí podríamos agregar registro adicional si es necesario
        System.out.println("Depósito registrado en el sistema");
    }
    
    // Métodos específicos de depósito
    public boolean esDepositoGrande() {
        return monto > 5000; // Definir umbral para depósitos grandes
    }
    
    public void solicitarValidacionEspecial() {
        if (esDepositoGrande()) {
            System.out.println("Depósito grande detectado - solicitando validación del supervisor");
            // Aquí se implementaría la lógica de validación adicional
        }
    }
    
    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("--- DETALLES ESPECÍFICOS DE DEPÓSITO ---");
        System.out.println("Es depósito grande: " + (esDepositoGrande() ? "Sí" : "No"));
        if (getCuentaDestino() != null) {
            System.out.println("Saldo actual cuenta: $" + getCuentaDestino().getSaldo());
        }
    }
}