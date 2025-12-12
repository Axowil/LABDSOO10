package modelo.cuentas;

import modelo.personas.Cliente;

public class CuentaAhorros extends Cuenta {
    private double tasaInteres; // Tasa de interés anual en porcentaje
    private double saldoMinimo; // Saldo mínimo para evitar comisiones
    private int retirosGratis; // Número de retiros gratis por mes
    private int retirosRealizados; // Contador de retiros en el mes
    
    // Constructor
    public CuentaAhorros(String numeroCuenta, double saldoInicial, Cliente cliente, 
                         double tasaInteres, double saldoMinimo, int retirosGratis) {
        super(numeroCuenta, saldoInicial, cliente);
        this.tasaInteres = tasaInteres;
        this.saldoMinimo = saldoMinimo;
        this.retirosGratis = retirosGratis;
        this.retirosRealizados = 0;
    }
    
    // Constructor simplificado
    public CuentaAhorros(String numeroCuenta, double saldoInicial, Cliente cliente) {
        this(numeroCuenta, saldoInicial, cliente, 1.5, 100.0, 5);
    }
    
    // Getters y Setters específicos
    public double getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(double tasaInteres) { this.tasaInteres = tasaInteres; }
    
    public double getSaldoMinimo() { return saldoMinimo; }
    public void setSaldoMinimo(double saldoMinimo) { this.saldoMinimo = saldoMinimo; }
    
    public int getRetirosGratis() { return retirosGratis; }
    public void setRetirosGratis(int retirosGratis) { this.retirosGratis = retirosGratis; }
    
    public int getRetirosRealizados() { return retirosRealizados; }
    
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
        
        if (monto > saldo) {
            System.out.println("Error: Saldo insuficiente");
            return false;
        }
        
        // Verificar comisión por retiro
        double comision = calcularComisionRetiro();
        double totalRetiro = monto + comision;
        
        if (totalRetiro > saldo) {
            System.out.println("Error: Saldo insuficiente para retiro + comisión");
            return false;
        }
        
        saldo -= totalRetiro;
        retirosRealizados++;
        
        registrarMovimiento("RETIRO", -monto, "Retiro de efectivo");
        if (comision > 0) {
            registrarMovimiento("COMISION", -comision, "Comisión por retiro");
        }
        
        System.out.println("Retiro exitoso: -$" + monto + 
                         (comision > 0 ? " (Comisión: $" + comision + ")" : "") +
                         " | Saldo actual: $" + saldo);
        return true;
    }
    
    @Override
    public boolean transferir(Cuenta cuentaDestino, double monto) {
        if (cuentaDestino == null) {
            System.out.println("Error: Cuenta destino no válida");
            return false;
        }
        
        if (monto <= 0) {
            System.out.println("Error: El monto debe ser positivo");
            return false;
        }
        
        if (!estaActiva() || !cuentaDestino.estaActiva()) {
            System.out.println("Error: Una de las cuentas no está activa");
            return false;
        }
        
        if (monto > saldo) {
            System.out.println("Error: Saldo insuficiente para transferencia");
            return false;
        }
        
        // Realizar transferencia
        saldo -= monto;
        cuentaDestino.depositar(monto);
        
        registrarMovimiento("TRANSFERENCIA", -monto, 
                          "Transferencia a cuenta " + cuentaDestino.getNumeroCuenta());
        cuentaDestino.registrarMovimiento("TRANSFERENCIA", monto, 
                                        "Transferencia de cuenta " + numeroCuenta);
        
        System.out.println("Transferencia exitosa: -$" + monto + 
                         " a cuenta " + cuentaDestino.getNumeroCuenta() +
                         " | Saldo actual: $" + saldo);
        return true;
    }
    
    // Métodos específicos de cuenta de ahorros
    private double calcularComisionRetiro() {
        if (retirosRealizados < retirosGratis) {
            return 0.0;
        } else {
            return 2.0; // Comisión fija por retiro excedente
        }
    }
    
    public void aplicarInteres() {
        double interes = saldo * (tasaInteres / 100) / 12; // Interés mensual
        saldo += interes;
        registrarMovimiento("INTERES", interes, "Interés aplicado (" + tasaInteres + "%)");
        System.out.println("Interés aplicado: +$" + interes + " | Saldo actual: $" + saldo);
    }
    
    public void reiniciarContadorRetiros() {
        retirosRealizados = 0;
        System.out.println("Contador de retiros reiniciado para el nuevo mes");
    }
    
    public boolean cumpleSaldoMinimo() {
        return saldo >= saldoMinimo;
    }
    
    @Override
    public void generarResumen() {
        super.generarResumen();
        System.out.println("Tipo: Cuenta de Ahorros");
        System.out.println("Tasa interés: " + tasaInteres + "% anual");
        System.out.println("Saldo mínimo: $" + saldoMinimo);
        System.out.println("Retiros gratis: " + retirosGratis + "/mes");
        System.out.println("Retiros realizados: " + retirosRealizados + "/mes");
        System.out.println("Cumple saldo mínimo: " + (cumpleSaldoMinimo() ? "Sí" : "No"));
    }
}