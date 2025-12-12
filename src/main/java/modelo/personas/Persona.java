package modelo.personas;

import java.time.LocalDate;

public abstract class Persona {
    protected String dni;
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String telefono;
    protected LocalDate fechaNacimiento;
    protected String direccion;
    
    // Constructor
    public Persona(String dni, String nombre, String apellido, String email, 
                   String telefono, LocalDate fechaNacimiento, String direccion) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
    }
    
    // Constructor simplificado
    public Persona(String dni, String nombre, String apellido) {
        this(dni, nombre, apellido, "", "", LocalDate.now(), "");
    }
    
    // Getters y Setters
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    // Métodos comunes
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    public int getEdad() {
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }
    
    public void mostrarInformacion() {
        System.out.println("DNI: " + dni);
        System.out.println("Nombre: " + getNombreCompleto());
        System.out.println("Email: " + email);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Edad: " + getEdad() + " años");
        System.out.println("Dirección: " + direccion);
    }
    
    @Override
    public String toString() {
        return getNombreCompleto() + " (" + dni + ")";
    }
}