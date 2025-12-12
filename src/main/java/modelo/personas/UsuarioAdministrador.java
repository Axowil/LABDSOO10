package modelo.personas;

public class UsuarioAdministrador extends Usuario {
    
    public UsuarioAdministrador(String nombreUsuario, String contraseña) {
        super(nombreUsuario, contraseña, null);
    }
    
    @Override
    public void mostrarPermisos() {
        System.out.println("=== PERMISOS DE ADMINISTRADOR ===");
        System.out.println(" Acceso completo al sistema");
        System.out.println(" Gestionar todos los usuarios");
        System.out.println(" Agregar/eliminar empleados");
        System.out.println(" Modificar configuraciones del sistema");
        System.out.println(" Generar reportes completos");
        System.out.println(" Auditoría de todas las operaciones");
        System.out.println(" Backup y restauración de datos");
        System.out.println("================================");
    }
    
    @Override
    public void mostrarMenu() {
        // Este método se implementará luego con Scanner
        System.out.println("Mostrando menú específico para ADMINISTRADOR...");
        mostrarPermisos();
    }
    
    // Métodos específicos del administrador
    public void realizarAuditoria() {
        System.out.println("Realizando auditoría del sistema...");
    }
    
    public void generarReporteCompleto() {
        System.out.println("Generando reporte completo del sistema...");
    }
}