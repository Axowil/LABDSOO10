package modelo.personas;

public class UsuarioCliente extends Usuario {
    private Cliente cliente;
    
    public UsuarioCliente(String nombreUsuario, String contraseña, Cliente cliente) {
        super(nombreUsuario, contraseña, cliente);
        this.cliente = cliente;
    }
    
    // Getter específico
    public Cliente getCliente() { return cliente; }
    
    @Override
    public void mostrarPermisos() {
        System.out.println("=== PERMISOS DE CLIENTE ===");
        System.out.println(" Consultar saldo de cuentas propias");
        System.out.println(" Realizar depósitos en cuentas propias");
        System.out.println(" Realizar retiros de cuentas propias");
        System.out.println(" Ver movimientos de cuentas propias");
        System.out.println(" Solicitar estados de cuenta");
        System.out.println(" No puede acceder a información de otros clientes");
        System.out.println(" No puede crear/eliminar cuentas");
        System.out.println("===========================");
    }
    
    @Override
    public void mostrarMenu() {
        // Este método se implementará luego con Scanner
        System.out.println("Mostrando menú específico para CLIENTE...");
        mostrarPermisos();
        if (cliente != null) {
            cliente.mostrarInformacion();
        }
    }
    
    // Métodos específicos del usuario cliente
    public void consultarInformacionCliente() {
        if (cliente != null) {
            cliente.mostrarInformacion();
            cliente.mostrarCuentas();
        }
    }
    
    public boolean puedeRealizarTransaccion(double monto) {
        return cliente != null && monto <= cliente.getLimiteCredito();
    }
}