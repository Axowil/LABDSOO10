package presentacion.administrador;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import modelo.personas.*;
import gestor.GestorUsuarios;

public class FrmAgregarUsuarios extends JFrame {
    
    // Componentes
    private JTextField txtNombreUsuario;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmarContrasena;
    private JComboBox<String> cboTipoUsuario;
    private JComboBox<String> cboPersonas;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnLimpiar;
    
    private GestorUsuarios gestorUsuarios;
    
    public FrmAgregarUsuarios(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;
        initComponents();
        configurarVentana();
        cargarPersonas("Cliente"); // Cargar clientes por defecto
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal con padding
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel lblTitulo = new JLabel("Agregar Nuevo Usuario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelContenido.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Tipo de Usuario
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelContenido.add(new JLabel("Tipo de Usuario:"), gbc);
        
        gbc.gridx = 1;
        String[] tiposUsuario = {"Cliente", "Empleado"};
        cboTipoUsuario = new JComboBox<>(tiposUsuario);
        cboTipoUsuario.addActionListener(e -> cambiarTipoPersona());
        panelContenido.add(cboTipoUsuario, gbc);
        
        // Persona (Cliente/Empleado)
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelContenido.add(new JLabel("Seleccionar Persona:"), gbc);
        
        gbc.gridx = 1;
        cboPersonas = new JComboBox<>();
        cboPersonas.setPreferredSize(new Dimension(250, 25));
        panelContenido.add(cboPersonas, gbc);
        
        // Nombre de Usuario
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelContenido.add(new JLabel("Nombre de Usuario:"), gbc);
        
        gbc.gridx = 1;
        txtNombreUsuario = new JTextField(20);
        panelContenido.add(txtNombreUsuario, gbc);
        
        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelContenido.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        txtContrasena = new JPasswordField(20);
        panelContenido.add(txtContrasena, gbc);
        
        // Confirmar Contraseña
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelContenido.add(new JLabel("Confirmar Contraseña:"), gbc);
        
        gbc.gridx = 1;
        txtConfirmarContrasena = new JPasswordField(20);
        panelContenido.add(txtConfirmarContrasena, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(100, 30));
        btnGuardar.addActionListener(e -> guardarUsuario());
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setPreferredSize(new Dimension(100, 30));
        btnLimpiar.addActionListener(e -> limpiarCampos());
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 30));
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCancelar);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelContenido.add(panelBotones, gbc);
        
        add(panelContenido, BorderLayout.CENTER);
    }
    
    private void configurarVentana() {
        setTitle("Agregar Usuario - Administrador");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }
    
    private void cambiarTipoPersona() {
        String tipoSeleccionado = (String) cboTipoUsuario.getSelectedItem();
        cargarPersonas(tipoSeleccionado);
    }
    
    private void cargarPersonas(String tipo) {
        cboPersonas.removeAllItems();
        
        if (tipo.equals("Cliente")) {
            // Cargar clientes desde el gestor
            List<Cliente> clientes = gestorUsuarios.getClientes();
            if (clientes != null && !clientes.isEmpty()) {
                for (Cliente cliente : clientes) {
                    // Verificar si el cliente ya tiene usuario
                    if (!clienteTieneUsuario(cliente.getDni())) {
                        String item = cliente.getDni() + " - " + 
                                     cliente.getNombre() + " " + cliente.getApellido() +
                                     " (" + cliente.getIdCliente() + ")";
                        cboPersonas.addItem(item);
                    }
                }
            }
        } else if (tipo.equals("Empleado")) {
            // Cargar empleados desde el gestor
            List<Empleado> empleados = gestorUsuarios.getEmpleados();
            if (empleados != null && !empleados.isEmpty()) {
                for (Empleado empleado : empleados) {
                    // Verificar si el empleado ya tiene usuario
                    if (!empleadoTieneUsuario(empleado.getDni())) {
                        String item = empleado.getDni() + " - " + 
                                     empleado.getNombre() + " " + empleado.getApellido() +
                                     " (" + empleado.getCargo() + ")";
                        cboPersonas.addItem(item);
                    }
                }
            }
        }
        
        if (cboPersonas.getItemCount() == 0) {
            cboPersonas.addItem("No hay personas disponibles");
            btnGuardar.setEnabled(false);
        } else {
            btnGuardar.setEnabled(true);
        }
    }
    
    // CORRECCIÓN 1: Validar que getDni() no sea null
    private boolean clienteTieneUsuario(String dni) {
        for (modelo.personas.Usuario usuario : gestorUsuarios.getUsuarios()) {
            if (usuario instanceof modelo.personas.UsuarioCliente) {
                modelo.personas.UsuarioCliente usuarioCliente = (modelo.personas.UsuarioCliente) usuario;
                modelo.personas.Cliente cliente = usuarioCliente.getCliente();
                
                // AGREGAMOS: "cliente.getDni() != null"
                if (cliente != null && cliente.getDni() != null && cliente.getDni().equals(dni)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // CORRECCIÓN 2: Lo mismo para empleados (para evitar futuro error)
    private boolean empleadoTieneUsuario(String dni) {
        for (modelo.personas.Usuario usuario : gestorUsuarios.getUsuarios()) {
            if (usuario instanceof modelo.personas.UsuarioEmpleado) {
                modelo.personas.UsuarioEmpleado usuarioEmpleado = (modelo.personas.UsuarioEmpleado) usuario;
                modelo.personas.Empleado empleado = usuarioEmpleado.getEmpleado();
                
                // AGREGAMOS: "empleado.getDni() != null"
                if (empleado != null && empleado.getDni() != null && empleado.getDni().equals(dni)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void guardarUsuario() {
        // Validaciones básicas
        if (cboPersonas.getSelectedIndex() == -1 ||
            cboPersonas.getSelectedItem().toString().equals("No hay personas disponibles")) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una persona",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String nombreUsuario = txtNombreUsuario.getText().trim();
        if (nombreUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre de usuario es obligatorio",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            txtNombreUsuario.requestFocus();
            return;
        }
        String contrasena = new String(txtContrasena.getPassword());
        String confirmar = new String(txtConfirmarContrasena.getPassword());
        
        if (contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La contraseña es obligatoria",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            txtContrasena.requestFocus();
            return;
        }
        
        if (!contrasena.equals(confirmar)) {
            JOptionPane.showMessageDialog(this,
                "Las contraseñas no coinciden",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            txtConfirmarContrasena.requestFocus();
            return;
        }
        
        try {
            // Obtener la persona seleccionada
            String personaSeleccionada = (String) cboPersonas.getSelectedItem();
            String dni = personaSeleccionada.split(" - ")[0];
            
            String tipoSeleccionado = (String) cboTipoUsuario.getSelectedItem();
            Usuario nuevoUsuario = null;
            
            switch (tipoSeleccionado) {
                case "Cliente":
                    // Buscar cliente por DNI usando el gestor
                    Cliente cliente = gestorUsuarios.buscarClientePorDni(dni);
                    if (cliente != null) {
                        nuevoUsuario = new UsuarioCliente(nombreUsuario, contrasena, cliente);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Cliente no encontrado",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                    
                case "Empleado":
                    Empleado empleado = gestorUsuarios.buscarEmpleado(dni);
                    if (empleado != null) {
                        nuevoUsuario = new UsuarioEmpleado(nombreUsuario, contrasena, empleado);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Empleado no encontrado",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
            }
            
            if (nuevoUsuario != null) {
                boolean resultado = gestorUsuarios.agregarUsuario(nuevoUsuario);
                
                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                        "Usuario creado exitosamente\n" +
                        "Nombre de usuario: " + nombreUsuario + "\n" +
                        "Tipo: " + tipoSeleccionado + "\n" +
                        "Persona asociada: " + dni,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    limpiarCampos();
                    // Recargar la lista de personas
                    cargarPersonas(tipoSeleccionado);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error: El nombre de usuario ya existe",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al crear el usuario: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    // En FrmAgregarUsuarios.java
public FrmAgregarUsuarios(GestorUsuarios gestorUsuarios, boolean soloClientes) {
    this.gestorUsuarios = gestorUsuarios;
    initComponents();
    configurarVentana();

    if (soloClientes) {
        // Forzar tipo "Cliente" y bloquear cambios
        cboTipoUsuario.setSelectedItem("Cliente");
        cboTipoUsuario.setEnabled(false);
        cargarPersonas("Cliente");   // solo clientes sin usuario
    } else {
        // Comportamiento normal (admin)
        cargarPersonas("Cliente");   // y luego puede cambiar a "Empleado"
    }
}

    private void limpiarCampos() {
        txtNombreUsuario.setText("");
        txtContrasena.setText("");
        txtConfirmarContrasena.setText("");
        txtNombreUsuario.requestFocus();
    }
}