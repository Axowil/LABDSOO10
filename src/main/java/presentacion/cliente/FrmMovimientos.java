package presentacion.cliente;

import gestor.GestorBanco;
import modelo.cuentas.Cuenta;
import presentacion.MainGUI;

import javax.swing.*;
import java.awt.*;

public class FrmMovimientos extends JFrame {
    
    private final GestorBanco gestorBanco;
    private JTextField txtNumeroCuenta;
    private JTextArea txtMovimientos;
    
    public FrmMovimientos() {
        this.gestorBanco = MainGUI.getGestorBanco();
        initComponents();
        configurarVentana();
    }
    
    private void configurarVentana() {
        setTitle("Historial de Movimientos - Sistema Bancario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setResizable(true);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(245, 245, 245));
        
        // Panel superior - Entrada
        JPanel panelEntrada = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelEntrada.setBackground(new Color(245, 245, 245));
        
        JLabel lblCuenta = new JLabel("Número de Cuenta:");
        lblCuenta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelEntrada.add(lblCuenta);
        
        txtNumeroCuenta = new JTextField(18);
        txtNumeroCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNumeroCuenta.setToolTipText("Ingrese el número de cuenta");
        panelEntrada.add(txtNumeroCuenta);
        
        JButton btnConsultar = new JButton(" Consultar Movimientos");
        btnConsultar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConsultar.setBackground(new Color(52, 152, 219));
        btnConsultar.setForeground(Color.DARK_GRAY);
        btnConsultar.addActionListener(e -> consultarMovimientos());
        btnConsultar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelEntrada.add(btnConsultar);
        
        // Área de movimientos
        JLabel lblTituloMovimientos = new JLabel(" Historial de Movimientos:");
        lblTituloMovimientos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloMovimientos.setForeground(new Color(41, 128, 185));
        
        txtMovimientos = new JTextArea();
        txtMovimientos.setEditable(false);
        txtMovimientos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtMovimientos.setBackground(new Color(250, 250, 250));
        txtMovimientos.setForeground(new Color(44, 62, 80));
        JScrollPane scrollMovimientos = new JScrollPane(txtMovimientos);
        scrollMovimientos.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Panel inferior - Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(245, 245, 245));
        
        JButton btnExportar = new JButton(" Exportar");
        btnExportar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnExportar.setToolTipText("Función disponible próximamente");
        btnExportar.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            " Función de exportación en desarrollo", "Próximamente", JOptionPane.INFORMATION_MESSAGE));
        panelBotones.add(btnExportar);
        
        JButton btnLimpiar = new JButton(" Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnLimpiar.addActionListener(e -> limpiar());
        panelBotones.add(btnLimpiar);
        
        JButton btnCerrar = new JButton(" Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.darkGray);
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        
        // Organizar el panel principal
        panelPrincipal.add(panelEntrada, BorderLayout.NORTH);
        panelPrincipal.add(lblTituloMovimientos, BorderLayout.WEST);
        panelPrincipal.add(scrollMovimientos, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    /**
     * Consulta y muestra los movimientos de la cuenta.
     */
    private void consultarMovimientos() {
        String numeroCuenta = txtNumeroCuenta.getText().trim();
        
        if (numeroCuenta.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                " Debe ingresar un número de cuenta", 
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Cuenta cuenta = gestorBanco.buscarCuenta(numeroCuenta);
        
        if (cuenta != null) {
            StringBuilder movimientos = new StringBuilder();
            movimientos.append("═══════════════════════════════════════════════════════════════\n");
            movimientos.append("                HISTORIAL DE MOVIMIENTOS\n");
            movimientos.append("═══════════════════════════════════════════════════════════════\n\n");
            movimientos.append(String.format("Cuenta: %s\n", cuenta.getNumeroCuenta()));
            movimientos.append(String.format("Cliente: %s\n", cuenta.getCliente().getNombreCompleto()));
            movimientos.append(String.format("Fecha de Consulta: %s\n\n", new java.util.Date()));
            movimientos.append("───────────────────────────────────────────────────────────────\n");
            
            if (cuenta.getMovimientos().isEmpty()) {
                movimientos.append("\n         No hay movimientos registrados en esta cuenta.\n");
            } else {
                for (String mov : cuenta.getMovimientos()) {
                    movimientos.append(mov).append("\n");
                }
            }
            
            movimientos.append("\n───────────────────────────────────────────────────────────────\n");
            movimientos.append(String.format("SALDO ACTUAL: $%,.2f\n", cuenta.getSaldo()));
            movimientos.append("═══════════════════════════════════════════════════════════════");
            
            txtMovimientos.setText(movimientos.toString());
            txtMovimientos.setCaretPosition(0); // Scroll al inicio
        } else {
            JOptionPane.showMessageDialog(this, 
                " Cuenta no encontrada\n" +
                "Verifique el número de cuenta y sus permisos.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            txtMovimientos.setText("");
        }
    }
    
    /**
     * Limpia los campos del formulario.
     */
    private void limpiar() {
        txtNumeroCuenta.setText("");
        txtMovimientos.setText("");
        txtNumeroCuenta.requestFocus();
    }
}