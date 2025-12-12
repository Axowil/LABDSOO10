package modelo.personas;

import java.time.LocalDate;

public class Empleado extends Persona {
    private String idEmpleado;
    private String cargo;
    private String departamento;
    private double salario;
    private LocalDate fechaContratacion;
    private String turno; // Mañana, Tarde, Noche
    private boolean activo;
    
    // Constructores
    public Empleado(String dni, String nombre, String apellido, String email,
                    String telefono, LocalDate fechaNacimiento, String direccion,
                    String idEmpleado, String cargo, String departamento, 
                    double salario, String turno) {
        super(dni, nombre, apellido, email, telefono, fechaNacimiento, direccion);
        this.idEmpleado = idEmpleado;
        this.cargo = cargo;
        this.departamento = departamento;
        this.salario = salario;
        this.fechaContratacion = LocalDate.now();
        this.turno = turno;
        this.activo = true;
    }
    
    public Empleado(String dni, String nombre, String apellido, 
                    String idEmpleado, String cargo, String departamento) {
        this(dni, nombre, apellido, "", "", LocalDate.now(), "",
             idEmpleado, cargo, departamento, 0.0, "Mañana");
    }
    
    // Getters y Setters específicos
    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }
    
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    
    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
    
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    // Métodos específicos del Empleado
    public void aumentarSalario(double porcentaje) {
        if (porcentaje > 0) {
            double aumento = salario * (porcentaje / 100);
            salario += aumento;
            System.out.println("Salario aumentado en " + porcentaje + "%. Nuevo salario: $" + salario);
        }
    }
    
    public void cambiarDepartamento(String nuevoDepartamento) {
        this.departamento = nuevoDepartamento;
        System.out.println(getNombreCompleto() + " transferido al departamento: " + nuevoDepartamento);
    }
    
    public void cambiarTurno(String nuevoTurno) {
        this.turno = nuevoTurno;
        System.out.println(getNombreCompleto() + " cambió al turno: " + nuevoTurno);
    }
    
    public int antiguedadEnMeses() {
        return (LocalDate.now().getYear() - fechaContratacion.getYear()) * 12 +
               (LocalDate.now().getMonthValue() - fechaContratacion.getMonthValue());
    }
    
    @Override
    public void mostrarInformacion() {
        System.out.println("=== INFORMACIÓN DEL EMPLEADO ===");
        super.mostrarInformacion();
        System.out.println("ID Empleado: " + idEmpleado);
        System.out.println("Cargo: " + cargo);
        System.out.println("Departamento: " + departamento);
        System.out.println("Salario: $" + salario);
        System.out.println("Fecha Contratación: " + fechaContratacion);
        System.out.println("Turno: " + turno);
        System.out.println("Activo: " + (activo ? "Sí" : "No"));
        System.out.println("Antigüedad: " + antiguedadEnMeses() + " meses");
    }
    
    public boolean esGerente() {
        return cargo != null && cargo.toLowerCase().contains("gerente");
    }
}