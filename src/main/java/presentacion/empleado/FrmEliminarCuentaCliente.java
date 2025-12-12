package presentacion.empleado;

import gestor.GestorBanco;
import presentacion.MainGUI;

import javax.swing.*;
import java.awt.*;

public class FrmEliminarCuentaCliente extends JFrame {

    private JTextField txtNumeroCuenta;
    private GestorBanco gestorBanco;

    public FrmEliminarCuentaCliente() {
        this.gestorBanco = MainGUI.getGestorBanco();
        initComponents();
    }

    private void initComponents() {
        setTitle("Eliminar cuenta de cliente");
        setSize(400, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Número de cuenta:"));
        txtNumeroCuenta = new JTextField();
        panel.add(txtNumeroCuenta);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarCuenta());

        add(panel, BorderLayout.CENTER);
        add(btnEliminar, BorderLayout.SOUTH);
    }

    private void eliminarCuenta() {
        String numero = txtNumeroCuenta.getText().trim();
        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de cuenta",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la cuenta " + numero + " (debe tener saldo 0)?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        boolean ok = gestorBanco.eliminarCuenta(numero);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            txtNumeroCuenta.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar la cuenta.\n" +
                    "Verifique que exista y tenga saldo 0.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
