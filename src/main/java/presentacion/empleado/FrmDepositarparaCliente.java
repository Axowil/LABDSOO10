package presentacion.empleado;

import javax.swing.*;
import java.awt.*;

public class FrmDepositarparaCliente extends JFrame {
	private JTextField txtDni, txtMonto, txtCuenta;
	private JButton btnDepositar;

	public FrmDepositarparaCliente() {
		setTitle("Depositar para Cliente");
		setSize(400, 220);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel panel = new JPanel(new GridLayout(4,2,10,10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.add(new JLabel("DNI Cliente:"));
		txtDni = new JTextField();
		panel.add(txtDni);
		panel.add(new JLabel("Número de Cuenta:"));
		txtCuenta = new JTextField();
		panel.add(txtCuenta);
		panel.add(new JLabel("Monto:"));
		txtMonto = new JTextField();
		panel.add(txtMonto);
		btnDepositar = new JButton("Depositar");
		panel.add(btnDepositar);
		add(panel, BorderLayout.CENTER);

		btnDepositar.addActionListener(e -> depositar());
	}

	private void depositar() {
		String dni = txtDni.getText().trim();
		String numeroCuenta = txtCuenta.getText().trim();
		String montoStr = txtMonto.getText().trim();
		double monto;
		try {
			monto = Double.parseDouble(montoStr);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (dni.isEmpty() || numeroCuenta.isEmpty()) {
			JOptionPane.showMessageDialog(this, "DNI y número de cuenta son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		gestor.GestorUsuarios gestorUsuarios = presentacion.MainGUI.getGestorBanco().getGestorUsuarios();
		modelo.personas.Cliente cliente = gestorUsuarios.buscarCliente(dni);
		if (cliente == null) {
			JOptionPane.showMessageDialog(this, "No existe cliente con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		gestor.GestorBanco gestorBanco = presentacion.MainGUI.getGestorBanco();
		modelo.cuentas.Cuenta cuenta = gestorBanco.buscarCuenta(numeroCuenta);
		if (cuenta == null || !cuenta.getCliente().getDni().equals(dni)) {
			JOptionPane.showMessageDialog(this, "La cuenta no existe o no pertenece al cliente.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		var deposito = gestorBanco.realizarDeposito(numeroCuenta, monto, "Depósito realizado por empleado");
		if (deposito != null) {
			JOptionPane.showMessageDialog(this, "Depósito realizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			txtDni.setText("");
			txtCuenta.setText("");
			txtMonto.setText("");
		} else {
			JOptionPane.showMessageDialog(this, "No se pudo realizar el depósito.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
