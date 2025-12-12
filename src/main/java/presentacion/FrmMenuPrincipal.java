package presentacion;

import gestor.*;
import presentacion.administrador.*;
import presentacion.cliente.*;
import presentacion.empleado.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FrmMenuPrincipal extends javax.swing.JFrame {
    
    private String tipoUsuario;
    private GestorBanco gestorBanco;
    private GestorUsuarios gestorUsuarios;
    
    public FrmMenuPrincipal(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        this.gestorBanco = MainGUI.getGestorBanco();
        this.gestorUsuarios = gestorBanco.getGestorUsuarios();
        initComponents();
        configurarVentana();
        configurarMenuPorRol();
    }
    
    private void configurarVentana() {
        setTitle("Sistema Bancario - Menú " + tipoUsuario);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 550);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    private void configurarMenuPorRol() {
        String titulo = "MENÚ PRINCIPAL - ";
        switch (tipoUsuario) {
            case "CLIENTE":
                titulo += "CLIENTE";
                break;
            case "EMPLEADO":
                titulo += "EMPLEADO";
                break;
            case "ADMINISTRADOR":
                titulo += "ADMINISTRADOR";
                break;
            default:
                titulo += "USUARIO";
        }
        setTitle(titulo);
    }
    
    private void initComponents() {
        // Panel principal con padding
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panelPrincipal.setBackground(new Color(231, 248, 255)); 
        
        JLabel lblSubtitulo = new JLabel("Sistema Bancario - Laboratorio 10");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblSubtitulo);
        
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        
        switch (tipoUsuario) {
            case "CLIENTE":
                agregarBoton(panelPrincipal, "Consultar Saldo", e -> abrirConsultaSaldo());
                agregarBoton(panelPrincipal, "Realizar Depósito", e -> abrirDeposito());
                agregarBoton(panelPrincipal, "Realizar Retiro", e -> abrirRetiro());
                agregarBoton(panelPrincipal, "Realizar Transferencia", e -> abrirTransferencia());
                agregarBoton(panelPrincipal, "Ver Movimientos", e -> abrirMovimientos());
                agregarBoton(panelPrincipal, "Ver Mis Cuentas", e -> mostrarMisCuentas());
                
                break;
                
            case "EMPLEADO":
                agregarBoton(panelPrincipal, "Ver Lista de clientes", e -> abrirListaClientes());
                agregarBoton(panelPrincipal, "Buscar por dni Cliente", e -> abrirBuscarClienteporDni());
                agregarBoton(panelPrincipal, "Agregar nuevo Cliente", e -> abrirCrearCliente());
                agregarBoton(panelPrincipal, "Crear usuario de cliente", e -> abrirCrearUsuarioCliente());
                agregarBoton(panelPrincipal, "Eliminar usuario de cliente", e -> abrirEliminarUsuarioCliente());
                agregarBoton(panelPrincipal, "Crear Cuenta de Ahorros ", e -> abrirCrearCuentaAhorros());
                agregarBoton(panelPrincipal, "Crear Cuenta Corriente ", e -> abrirCrearCuentaCorriente());
                agregarBoton(panelPrincipal, "Eliminar cuenta de cliente", e -> abrirEliminarCuentaCliente());
                agregarBoton(panelPrincipal, "Depositar para cliente  ", e -> abrirDepositarparaCliente());
                agregarBoton(panelPrincipal, "Retirar para cliente ", e -> abrirRetiroparaCliente());
                agregarBoton(panelPrincipal, "Transferencia para cliente ", e -> abrirTransferenciaCliente());
                break;
                
            case "ADMINISTRADOR":
                agregarLabelSeccion(panelPrincipal, "Administración del sistema");
                agregarBoton(panelPrincipal, "Agregar nuevo Cliente", e -> abrirCrearCliente());
                agregarBoton(panelPrincipal, "Agregar nuevo Empleado", e -> abrirCrearEmpleado());
                agregarBoton(panelPrincipal, "Agregar Usuarios", e -> abrirAgregarUsuarios());
                //gestion cliente
                agregarBoton(panelPrincipal, "Gestionar Empleados", e -> abrirGestionEmpleados());
                agregarBoton(panelPrincipal, "Gestionar Usuarios", e -> abrirGestionUsuarios());
                agregarBoton(panelPrincipal, "Gestión Completa del Sistema", e -> abrirGestionCompleta());
                agregarBoton(panelPrincipal, "Reportes y Estadísticas", e -> abrirReportesAdmin());
                agregarBoton(panelPrincipal, "Auditoría del Sistema", e -> abrirAuditoria());
                agregarLabelSeccion(panelPrincipal, "Gestión operativa");
                agregarBoton(panelPrincipal, "Crear Cuenta de Ahorros", e -> abrirCrearCuentaAhorros());
                agregarBoton(panelPrincipal, "Crear Cuenta Corriente", e -> abrirCrearCuentaCorriente());
                agregarBoton(panelPrincipal, "Depositar para cliente", e -> abrirDepositarparaCliente());
                agregarBoton(panelPrincipal, "Retirar para cliente", e -> abrirRetiroparaCliente());
                agregarBoton(panelPrincipal, "Transferencia para cliente", e -> abrirTransferenciaCliente());
                agregarBoton(panelPrincipal, "Eliminar cuenta de cliente", e -> abrirEliminarCuentaCliente());
                break;
        }
        
        panelPrincipal.add(Box.createVerticalGlue());
        
        // Botón de cerrar sesión
        JButton btnCerrarSesion = new JButton(" Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setBackground(new Color(20, 84, 156));
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(400, 50));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panelPrincipal.add(btnCerrarSesion);
        
        JScrollPane scroll = new JScrollPane(panelPrincipal);
scroll.setBorder(null); // opcional, para que se vea limpio
scroll.getVerticalScrollBar().setUnitIncrement(16); 
scroll.getVerticalScrollBar().setBlockIncrement(100);
setContentPane(scroll);
    }
    
    private void agregarBoton(JPanel panel, String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(500, 60));
        boton.addActionListener(accion);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        
        // Color alternado para botones
        boton.setBackground(new Color(52, 152, 219));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        
        panel.add(boton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    private void agregarLabelSeccion(JPanel panel, String texto) {
    JLabel lbl = new JLabel(texto);
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(Box.createRigidArea(new Dimension(0, 15)));
    panel.add(lbl);
    panel.add(Box.createRigidArea(new Dimension(0, 5)));
}

    // =============== MÉTODOS PARA CLIENTE ===============
    
private void abrirConsultaSaldo() {
    FrmConsultaSaldo frm = new FrmConsultaSaldo();
    frm.setVisible(true);
    frm.setLocationRelativeTo(this);
}
    
private void abrirDeposito() {
    FrmDeposito frm = new FrmDeposito();
    frm.setVisible(true);
    frm.setLocationRelativeTo(this);
}
    
private void abrirRetiro() {
    FrmRetiro frm = new FrmRetiro();
    frm.setVisible(true);
    frm.setLocationRelativeTo(this);
}
    
private void abrirTransferencia() {
    presentacion.cliente.FrmTransferenciaCliente frm = new presentacion.cliente.FrmTransferenciaCliente();
    frm.setVisible(true);
    frm.setLocationRelativeTo(this);
}
    
private void abrirMovimientos() {
    FrmMovimientos frm = new FrmMovimientos();
    frm.setVisible(true);
    frm.setLocationRelativeTo(this);
}
private void mostrarMisCuentas() {
    FrmMisCuentas frm = new FrmMisCuentas(gestorUsuarios, gestorBanco);
    frm.setVisible(true);
    frm.setLocationRelativeTo(this);
}

    // =============== MÉTODOS PARA EMPLEADO ===============
    private void abrirListaClientes() {
        new FrmListaClientes().setVisible(true);
    }

    private void abrirBuscarClienteporDni() {
        new FrmBuscarClienteporDni().setVisible(true);
    }

    private void abrirCrearCliente() {
        new FrmCrearCliente().setVisible(true);
    }
    private void abrirCrearEmpleado() {
        new FrmCrearEmpleado().setVisible(true);
    }
    private void abrirCrearCuentaAhorros() {
        new FrmCrearCuentaAhorros().setVisible(true);
    }

    private void abrirCrearCuentaCorriente() {
        new FrmCrearCuentaCorriente().setVisible(true);
    }

    private void abrirDepositarparaCliente() {
        new FrmDepositarparaCliente().setVisible(true);
    }

    private void abrirRetiroparaCliente() {
        new FrmRetiroparaCliente().setVisible(true);
    }

    private void abrirTransferenciaCliente() {
        new presentacion.empleado.FrmTransferenciaCliente().setVisible(true);
    }
    private void abrirEliminarUsuarioCliente() {
    FrmEliminarUsuarioCliente frm =
            new FrmEliminarUsuarioCliente(gestorUsuarios);
    frm.setVisible(true);
    frm.setLocationRelativeTo(this);
}   
    private void abrirEliminarCuentaCliente() {
    FrmEliminarCuentaCliente frm = new FrmEliminarCuentaCliente();
    frm.setVisible(true);
    frm.setLocationRelativeTo(this);
}
private void abrirCrearUsuarioCliente() {
    FrmAgregarUsuarios frm = new FrmAgregarUsuarios(gestorUsuarios, true); // modo solo CLIENTE
    frm.setVisible(true);
    frm.setLocationRelativeTo(this);
}
    
    // =============== MÉTODOS PARA ADMINISTRADOR ===============

    private void abrirGestionUsuarios() {
    new presentacion.administrador.FrmGestionUsuarios().setVisible(true);
}

    
private void abrirGestionEmpleados() {
    new presentacion.administrador.FrmGestionEmpleados().setVisible(true);
}
    
    private void abrirGestionCompleta() {
    new presentacion.administrador.FrmGestionCompletaAdmin().setVisible(true);
}
    
   private void abrirReportesAdmin() {
    new presentacion.administrador.FrmReportesAdministrador().setVisible(true);
}
private void abrirAgregarUsuarios() {
    FrmAgregarUsuarios frmAgregar = new FrmAgregarUsuarios(gestorUsuarios);
    frmAgregar.setVisible(true);
}
    
    private void abrirAuditoria() {
        // Auditoría en un área de texto
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        
        StringBuilder auditoria = new StringBuilder();
        auditoria.append(" AUDITORÍA DEL SISTEMA\n\n");
        auditoria.append("Fecha: ").append(new java.util.Date()).append("\n");
        auditoria.append("Usuario actual: ").append(gestorUsuarios.getUsuarioActual().getNombreUsuario()).append("\n\n");
        
        auditoria.append("USUARIOS REGISTRADOS:\n");
        gestorUsuarios.getUsuarios().forEach(u -> 
            auditoria.append("- ").append(u.getNombreUsuario()).append(" (")
                     .append(u.getClass().getSimpleName()).append(")\n")
        );
        
        textArea.setText(auditoria.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Auditoría", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Cierra la sesión actual y vuelve al login.
     */
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de cerrar sesión?", 
            "Confirmar Cierre de Sesión", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            gestorUsuarios.cerrarSesion();
            JOptionPane.showMessageDialog(this, 
                " Sesión cerrada exitosamente", 
                "Cierre de Sesión", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose(); // Cerrar menú
            
            // Volver al login
            FrmLogin login = new FrmLogin();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
        }
    }
}
