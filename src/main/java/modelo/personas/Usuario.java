package modelo.personas;

public abstract class Usuario {
    protected String nombreUsuario;
    protected String contraseña;
    protected boolean estado; // true = activo, false = inactivo
    protected Persona personaAsociada;
    
    // Constructor
    public Usuario(String nombreUsuario, String contraseña, Persona personaAsociada) {
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.personaAsociada = personaAsociada;
        this.estado = true;
    }
    
    // Getters y Setters
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    
    public Persona getPersonaAsociada() { return personaAsociada; }
    public void setPersonaAsociada(Persona personaAsociada) { 
        this.personaAsociada = personaAsociada; 
    }
    
    // Métodos de autenticación
    public boolean autenticar(String usuario, String contraseña) {
        return this.nombreUsuario.equals(usuario) && 
               this.contraseña.equals(contraseña) && 
               this.estado;
    }
    
    public void cambiarContraseña(String nuevaContraseña) {
        this.contraseña = nuevaContraseña;
        System.out.println("Contraseña cambiada exitosamente para " + nombreUsuario);
    }
    
    public void activarUsuario() {
        this.estado = true;
        System.out.println("Usuario " + nombreUsuario + " activado");
    }
    
    public void desactivarUsuario() {
        this.estado = false;
        System.out.println("Usuario " + nombreUsuario + " desactivado");
    }
    
    // Métodos abstractos que deben implementar las clases hijas
    public abstract void mostrarMenu();
    public abstract void mostrarPermisos();
    
    // Método común para mostrar información básica
    public void mostrarInformacionUsuario() {
        System.out.println("=== INFORMACIÓN DE USUARIO ===");
        System.out.println("Nombre Usuario: " + nombreUsuario);
        System.out.println("Estado: " + (estado ? "Activo" : "Inactivo"));
        System.out.println("Tipo: " + this.getClass().getSimpleName());
        if (personaAsociada != null) {
            System.out.println("Persona Asociada: " + personaAsociada.getNombreCompleto());
        }
    }
    
    @Override
    public String toString() {
        return nombreUsuario + " (" + this.getClass().getSimpleName() + ")";
    }
}