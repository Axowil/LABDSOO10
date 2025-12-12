package modelo.personas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona {
    private String idCliente;
    private LocalDate fechaRegistro;
    private String categoria; // VIP, Regular, Nuevo
    private double limiteCredito;
    private List<String> cuentasAsociadas;
    
    // Constructores
    public Cliente(String dni, String nombre, String apellido, String email, 
                   String telefono, LocalDate fechaNacimiento, String direccion,
                   String idCliente, String categoria, double limiteCredito) {
        super(dni, nombre, apellido, email, telefono, fechaNacimiento, direccion);
        this.idCliente = idCliente;
        this.fechaRegistro = LocalDate.now();
        this.categoria = categoria;
        this.limiteCredito = limiteCredito;
        this.cuentasAsociadas = new ArrayList<>();
    }
    
    public Cliente(String dni, String nombre, String apellido, String idCliente) {
        this(dni, nombre, apellido, "", "", LocalDate.now(), "", 
             idCliente, "Regular", 1000.0);
    }
    
    // Getters y Setters específicos
    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }
    
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public double getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(double limiteCredito) { 
        this.limiteCredito = limiteCredito; 
    }
    
    public List<String> getCuentasAsociadas() { return cuentasAsociadas; }
    
    // Métodos específicos del Cliente
    public void agregarCuenta(String numeroCuenta) {
        if (!cuentasAsociadas.contains(numeroCuenta)) {
            cuentasAsociadas.add(numeroCuenta);
            System.out.println("Cuenta " + numeroCuenta + " agregada al cliente " + getNombreCompleto());
        }
    }
    
    public void eliminarCuenta(String numeroCuenta) {
        if (cuentasAsociadas.remove(numeroCuenta)) {
            System.out.println("Cuenta " + numeroCuenta + " eliminada del cliente " + getNombreCompleto());
        }
    }
    
    public void mostrarCuentas() {
        System.out.println("=== CUENTAS DEL CLIENTE " + getNombreCompleto() + " ===");
        if (cuentasAsociadas.isEmpty()) {
            System.out.println("No tiene cuentas asociadas");
        } else {
            for (String cuenta : cuentasAsociadas) {
                System.out.println("- " + cuenta);
            }
        }
    }
    
    public boolean esClienteVIP() {
        return "VIP".equalsIgnoreCase(categoria);
    }
    
    @Override
    public void mostrarInformacion() {
        System.out.println("=== INFORMACIÓN DEL CLIENTE ===");
        super.mostrarInformacion();
        System.out.println("ID Cliente: " + idCliente);
        System.out.println("Fecha Registro: " + fechaRegistro);
        System.out.println("Categoría: " + categoria);
        System.out.println("Límite Crédito: $" + limiteCredito);
        System.out.println("Número de cuentas: " + cuentasAsociadas.size());
        System.out.println("Cliente VIP: " + (esClienteVIP() ? "Sí" : "No"));
    }
}