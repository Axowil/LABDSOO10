package presentacion.cliente;

import gestor.GestorBanco;
import modelo.transacciones.Transaccion;
import presentacion.MainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class FrmTransferenciaCliente extends JFrame {

    private final GestorBanco gestorBanco;
    private JTextField txtCuentaOrigen;
    private JTextField txtCuentaDestino;
    private JTextField txtMonto;
    private JTextField txtConcepto;
    private JTextField txtReferencia;

    public FrmTransferenciaCliente() {
        this.gestorBanco = MainGUI.getGestorBanco();
        initComponents();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Transferencia - Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 250, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel(" REALIZAR TRANSFERENCIA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(52, 152, 219));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Cuenta origen
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Cuenta origen:"), gbc);
        txtCuentaOrigen = new JTextField(18);
        gbc.gridx = 1;
        panel.add(txtCuentaOrigen, gbc);

        // Cuenta destino
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Cuenta destino:"), gbc);
        txtCuentaDestino = new JTextField(18);
        gbc.gridx = 1;
        panel.add(txtCuentaDestino, gbc);

        // Monto
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Monto:"), gbc);
        txtMonto = new JTextField(18);
        gbc.gridx = 1;
        panel.add(txtMonto, gbc);

        // Concepto
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Concepto:"), gbc);
        txtConcepto = new JTextField(18);
        gbc.gridx = 1;
        panel.add(txtConcepto, gbc);

        // Referencia
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Referencia:"), gbc);
        txtReferencia = new JTextField(18);
        gbc.gridx = 1;
        panel.add(txtReferencia, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setOpaque(false);

        JButton btnTransferir = new JButton(" Transferir");
        btnTransferir.setBackground(new Color(39, 174, 96));
        btnTransferir.setForeground(Color.DARK_GRAY);
        btnTransferir.addActionListener(this::realizarTransferencia);
        panelBotones.add(btnTransferir);

        JButton btnLimpiar = new JButton(" Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());
        panelBotones.add(btnLimpiar);

        JButton btnCancelar = new JButton(" Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.DARK_GRAY);
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        add(panel);
    }

    private void realizarTransferencia(ActionEvent e) {
        String origen  = txtCuentaOrigen.getText().trim();
        String destino = txtCuentaDestino.getText().trim();
        String montoStr= txtMonto.getText().trim();
        String concepto= txtConcepto.getText().trim();
        String ref     = txtReferencia.getText().trim();

        if (origen.isEmpty() || destino.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                " Complete cuenta origen, destino y monto",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
            if (monto <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                " Monto debe ser un número mayor a cero",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Transaccion t = gestorBanco.realizarTransferencia(origen, destino, monto, concepto, ref);
        if (t != null) {
            JOptionPane.showMessageDialog(this,
                "🎉 Transferencia exitosa\n\n" +
                "ID: " + t.getIdTransaccion() + "\n" +
                "Origen: " + origen + "\n" +
                "Destino: " + destino + "\n" +
                "Monto: $" + String.format("%,.2f", monto),
                "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this,
                " Error al transferir\n" +
                "• Verifique que ambas cuentas existan y estén activas\n" +
                "• Saldo suficiente en cuenta origen\n" +
                "• No exceda límites diarios",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtCuentaOrigen.setText("");
        txtCuentaDestino.setText("");
        txtMonto.setText("");
        txtConcepto.setText("");
        txtReferencia.setText("");
        txtCuentaOrigen.requestFocus();
    }
}