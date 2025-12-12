package modelo.personas;

public class UsuarioEmpleado extends Usuario {
    private Empleado empleado;
    
    public UsuarioEmpleado(String nombreUsuario, String contraseña, Empleado empleado) {
        super(nombreUsuario, contraseña, empleado);
        this.empleado = empleado;
    }
    
    // Getter específico
    public Empleado getEmpleado() { return empleado; }
    
    @Override
    public void mostrarPermisos() {
        System.out.println("=== PERMISOS DE EMPLEADO ===");
        System.out.println(" Crear cuentas bancarias");
        System.out.println(" Atender transacciones de clientes");
        System.out.println(" Consultar información de clientes");
        System.out.println(" Generar reportes básicos");
        System.out.println(" Gestionar solicitudes de clientes");
        System.out.println(" No puede modificar datos de otros empleados");
        System.out.println(" No puede acceder a configuración del sistema");
        System.out.println("============================");
    }
    
    @Override
    public void mostrarMenu() {
        // Este método se implementará luego con Scanner
        System.out.println("Mostrando menú específico para EMPLEADO...");
        mostrarPermisos();
        if (empleado != null) {
            empleado.mostrarInformacion();
        }
    }
    
    // Métodos específicos del usuario empleado
    public void consultarInformacionEmpleado() {
        if (empleado != null) {
            empleado.mostrarInformacion();
        }
    }
    
    public boolean puedeGestionarClientes() {
        return empleado != null && 
               (empleado.getCargo().toLowerCase().contains("cajero") ||
                empleado.getCargo().toLowerCase().contains("atencion"));
    }
}