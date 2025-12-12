package modelo.cuentas;

import modelo.personas.Cliente;

public class CuentaCorriente extends Cuenta {
    private double sobregiroMaximo;
    private double sobregiroUtilizado;
    private double comisionManejo; // Comisión mensual
    
    // Constructor
    public CuentaCorriente(String numeroCuenta, double saldoInicial, Cliente cliente,
                          double sobregiroMaximo, double comisionManejo) {
        super(numeroCuenta, saldoInicial, cliente);
        this.sobregiroMaximo = sobregiroMaximo;
        this.sobregiroUtilizado = 0.0;
        this.comisionManejo = comisionManejo;
    }
    
    // Constructor simplificado
    public CuentaCorriente(String numeroCuenta, double saldoInicial, Cliente cliente) {
        this(numeroCuenta, saldoInicial, cliente, 500.0, 10.0);
    }
    
    // Getters y Setters específicos
    public double getSobregiroMaximo() { return sobregiroMaximo; }
    public void setSobregiroMaximo(double sobregiroMaximo) { 
        this.sobregiroMaximo = sobregiroMaximo; 
    }
    
    public double getSobregiroUtilizado() { return sobregiroUtilizado; }
    
    public double getComisionManejo() { return comisionManejo; }
    public void setComisionManejo(double comisionManejo) { 
        this.comisionManejo = comisionManejo; 
    }
    
    // Implementación de métodos abstractos
    @Override
    public boolean retirar(double monto) {
        if (monto <= 0) {
            System.out.println("Error: El monto debe ser positivo");
            return false;
        }
        
        if (!estaActiva()) {
            System.out.println("Error: La cuenta no está activa");
            return false;
        }
        
        double disponibleTotal = saldo + (sobregiroMaximo - sobregiroUtilizado);
        
        if (monto > disponibleTotal) {
            System.out.println("Error: Fondos insuficientes (incluyendo sobregiro)");
            System.out.println("Disponible: $" + disponibleTotal);
            return false;
        }
        
        // Calcular cuánto del retiro usa el sobregiro
        if (monto > saldo) {
            double excedente = monto - saldo;
            sobregiroUtilizado += excedente;
            saldo = 0;
        } else {
            saldo -= monto;
        }
        
        registrarMovimiento("RETIRO", -monto, "Retiro de efectivo" + 
                          (sobregiroUtilizado > 0 ? " (Con sobregiro)" : ""));
        
        System.out.println("Retiro exitoso: -$" + monto + 
                         (sobregiroUtilizado > 0 ? " (Sobregiro utilizado: $" + sobregiroUtilizado + ")" : "") +
                         " | Saldo disponible: $" + (saldo + (sobregiroMaximo - sobregiroUtilizado)));
        return true;
    }
    
@Override
public boolean transferir(Cuenta cuentaDestino, double monto) {
    // Para transferencias, no permitimos usar sobregiro
    if (monto <= 0) {
        System.out.println("Error: El monto debe ser positivo");
        return false;
    }

    if (!estaActiva()) {
        System.out.println("Error: La cuenta no está activa");
        return false;
    }

    if (cuentaDestino == null) {
        System.out.println("Error: Cuenta destino no válida");
        return false;
    }

    if (monto > saldo) {
        System.out.println("Error: Saldo insuficiente para transferencia");
        System.out.println("Saldo disponible (sin sobregiro): $" + saldo);
        return false;
    }

    // Ejecutar transferencia: descontar de esta cuenta y depositar en destino
    saldo -= monto;
    registrarMovimiento("TRANSFERENCIA_SALIDA", -monto, "Transferencia a cuenta " + cuentaDestino.getNumeroCuenta());

    boolean depositado = cuentaDestino.depositar(monto);
    if (depositado) {
        System.out.println("Transferencia exitosa: -$" + monto + " a cuenta " + cuentaDestino.getNumeroCuenta());
        return true;
    } else {
        // Si no se pudo depositar en destino, revertir
        saldo += monto;
        registrarMovimiento("TRANSFERENCIA_FALLIDA", 0, "Rollback por fallo en depósito destino");
        System.out.println("Error: No se pudo completar la transferencia al depositar en destino");
        return false;
    }
}
    
    @Override
    public boolean depositar(double monto) {
        // Primero cubrir el sobregiro si existe
        if (sobregiroUtilizado > 0) {
            if (monto <= sobregiroUtilizado) {
                sobregiroUtilizado -= monto;
                registrarMovimiento("DEPOSITO", monto, "Depósito (Cubriendo sobregiro)");
                System.out.println("Depósito aplicado a sobregiro: -$" + monto + 
                                 " | Sobregiro restante: $" + sobregiroUtilizado);
                return true;
            } else {
                double excedente = monto - sobregiroUtilizado;
                saldo += excedente;
                registrarMovimiento("DEPOSITO", monto, 
                                  "Depósito (Cubriendo sobregiro: $" + sobregiroUtilizado + ")");
                sobregiroUtilizado = 0;
                System.out.println("Depósito exitoso: +$" + monto + 
                                 " | Sobregiro cubierto | Saldo actual: $" + saldo);
                return true;
            }
        }
        
        return super.depositar(monto);
    }
    
    // Métodos específicos de cuenta corriente
    public void aplicarComisionManejo() {
        if (saldo >= comisionManejo) {
            saldo -= comisionManejo;
            registrarMovimiento("COMISION", -comisionManejo, "Comisión mensual de manejo");
            System.out.println("Comisión de manejo aplicada: -$" + comisionManejo);
        } else {
            System.out.println("No se pudo aplicar comisión: saldo insuficiente");
        }
    }
    
    public double getSaldoDisponible() {
        return saldo + (sobregiroMaximo - sobregiroUtilizado);
    }
    
    public double getLineaSobregiroDisponible() {
        return sobregiroMaximo - sobregiroUtilizado;
    }
    
    public void reducirSobregiro(double monto) {
        if (monto <= 0 || monto > sobregiroUtilizado) {
            System.out.println("Error: Monto no válido para reducir sobregiro");
            return;
        }
        
        sobregiroUtilizado -= monto;
        registrarMovimiento("REDUCCION_SOBREGIRO", monto, "Reducción de sobregiro");
        System.out.println("Sobregiro reducido en: $" + monto + 
                         " | Sobregiro actual: $" + sobregiroUtilizado);
    }
    
    @Override
    public void generarResumen() {
        super.generarResumen();
        System.out.println("Tipo: Cuenta Corriente");
        System.out.println("Línea de sobregiro: $" + sobregiroMaximo);
        System.out.println("Sobregiro utilizado: $" + sobregiroUtilizado);
        System.out.println("Sobregiro disponible: $" + getLineaSobregiroDisponible());
        System.out.println("Comisión mensual: $" + comisionManejo);
        System.out.println("Saldo total disponible: $" + getSaldoDisponible());
    }
}