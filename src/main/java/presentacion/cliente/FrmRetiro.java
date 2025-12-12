package presentacion.cliente;

import gestor.GestorBanco;
import modelo.transacciones.Transaccion;
import presentacion.MainGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario para realizar retiros de una cuenta bancaria.
 * Incluye validaciones de saldo, límites y métodos de retiro.
 * 
 * @author TuNombre
 * @version 1.0
 */
public class FrmRetiro extends JFrame {
    
    private final GestorBanco gestorBanco;
    private JTextField txtNumeroCuenta;
    private JTextField txtMonto;
    private JComboBox<String> cmbMetodo;
    private JTextField txtUbicacion;
    
    public FrmRetiro() {
        this.gestorBanco = MainGUI.getGestorBanco();
        initComponents();
        configurarVentana();
    }
    
    private void configurarVentana() {
        setTitle("Retiro Bancario - Sistema Bancario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 480);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panelPrincipal.setBackground(new Color(255, 240, 240)); // Light red background
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel lblTitulo = new JLabel(" REALIZAR RETIRO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(231, 76, 60));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);
        
        // Número de cuenta
        JLabel lblCuenta = new JLabel("Número de Cuenta:");
        lblCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panelPrincipal.add(lblCuenta, gbc);
        
        txtNumeroCuenta = new JTextField(20);
        txtNumeroCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNumeroCuenta.setToolTipText("Cuenta de donde retirará el dinero");
        gbc.gridx = 1; gbc.gridy = 1;
        panelPrincipal.add(txtNumeroCuenta, gbc);
        
        // Monto
        JLabel lblMonto = new JLabel("Monto a Retirar:");
        lblMonto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        panelPrincipal.add(lblMonto, gbc);
        
        txtMonto = new JTextField(20);
        txtMonto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMonto.setToolTipText("Monto mayor a cero y menor o igual al saldo disponible");
        gbc.gridx = 1; gbc.gridy = 2;
        panelPrincipal.add(txtMonto, gbc);
        
        // Método de retiro
        JLabel lblMetodo = new JLabel("Método de Retiro:");
        lblMetodo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 3;
        panelPrincipal.add(lblMetodo, gbc);
        
        cmbMetodo = new JComboBox<>(new String[]{"CAJERO", "VENTANILLA", "OFICINA"});
        cmbMetodo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 3;
        panelPrincipal.add(cmbMetodo, gbc);
        
        // Ubicación
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 4;
        panelPrincipal.add(lblUbicacion, gbc);
        
        txtUbicacion = new JTextField(20);
        txtUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUbicacion.setToolTipText("Ej: Sucursal Principal, Cajero Plaza");
        gbc.gridx = 1; gbc.gridy = 4;
        panelPrincipal.add(txtUbicacion, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setOpaque(false);
        
        JButton btnRetirar = new JButton(" Retirar");
        btnRetirar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRetirar.setBackground(new Color(39, 174, 96));
        btnRetirar.setForeground(Color.DARK_GRAY);
        btnRetirar.addActionListener(e -> realizarRetiro());
        panelBotones.add(btnRetirar);
        
        JButton btnLimpiar = new JButton(" Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnLimpiar.addActionListener(e -> limpiar());
        panelBotones.add(btnLimpiar);
        
        JButton btnCancelar = new JButton(" Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.DARK_GRAY);
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panelPrincipal.add(panelBotones, gbc);
        
        add(panelPrincipal);
    }
    
    /**
     * Valida y procesa el retiro.
     */
    private void realizarRetiro() {
        String numeroCuenta = txtNumeroCuenta.getText().trim();
        String montoStr = txtMonto.getText().trim();
        String metodo = cmbMetodo.getSelectedItem().toString();
        String ubicacion = txtUbicacion.getText().trim();
        
        // Validaciones básicas
        if (numeroCuenta.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                " Debe ingresar número de cuenta y monto", 
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validación del monto
        double monto;
        try {
            monto = Double.parseDouble(montoStr);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, 
                    " El monto debe ser mayor a cero", 
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                " El monto debe ser un número válido", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Realizar el retiro
        Transaccion transaccion = gestorBanco.realizarRetiro(numeroCuenta, monto, metodo, ubicacion);
        
        if (transaccion != null) {
            JOptionPane.showMessageDialog(this, 
                "🎉 Retiro realizado con éxito!\n\n" +
                "ID Transacción: " + transaccion.getIdTransaccion() + "\n" +
                "Cuenta: " + numeroCuenta + "\n" +
                "Monto: $" + String.format("%,.2f", monto) + "\n" +
                "Método: " + metodo + "\n" +
                "Ubicación: " + ubicacion, 
                "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, 
                " Error al realizar el retiro.\n" +
                "Posibles causas:\n" +
                "• Saldo insuficiente\n" +
                "• Cuenta inactiva o no encontrada\n" +
                "• No tiene permisos\n" +
                "• Excede el límite diario", 
                "Error en Operación", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpia los campos del formulario.
     */
    private void limpiar() {
        txtNumeroCuenta.setText("");
        txtMonto.setText("");
        txtUbicacion.setText("");
        cmbMetodo.setSelectedIndex(0);
        txtNumeroCuenta.requestFocus();
    }
}