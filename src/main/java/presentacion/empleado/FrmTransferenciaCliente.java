package presentacion.empleado;

import javax.swing.*;
import java.awt.*;

public class FrmTransferenciaCliente extends JFrame {
	private JTextField txtDniOrigen, txtCuentaOrigen, txtDniDestino, txtCuentaDestino, txtMonto;
	private JButton btnTransferir;

	public FrmTransferenciaCliente() {
		setTitle("Transferencia entre Clientes");
		setSize(450, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel panel = new JPanel(new GridLayout(6,2,10,10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.add(new JLabel("DNI Origen:"));
		txtDniOrigen = new JTextField();
		panel.add(txtDniOrigen);
		panel.add(new JLabel("Cuenta Origen:"));
		txtCuentaOrigen = new JTextField();
		panel.add(txtCuentaOrigen);
		panel.add(new JLabel("DNI Destino:"));
		txtDniDestino = new JTextField();
		panel.add(txtDniDestino);
		panel.add(new JLabel("Cuenta Destino:"));
		txtCuentaDestino = new JTextField();
		panel.add(txtCuentaDestino);
		panel.add(new JLabel("Monto:"));
		txtMonto = new JTextField();
		panel.add(txtMonto);
		btnTransferir = new JButton("Transferir");
		panel.add(btnTransferir);
		add(panel, BorderLayout.CENTER);

		btnTransferir.addActionListener(e -> transferir());
	}

	private void transferir() {
		String dniOrigen = txtDniOrigen.getText().trim();
		String cuentaOrigen = txtCuentaOrigen.getText().trim();
		String dniDestino = txtDniDestino.getText().trim();
		String cuentaDestino = txtCuentaDestino.getText().trim();
		String montoStr = txtMonto.getText().trim();
		double monto;
		try {
			monto = Double.parseDouble(montoStr);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (dniOrigen.isEmpty() || cuentaOrigen.isEmpty() || dniDestino.isEmpty() || cuentaDestino.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		gestor.GestorUsuarios gestorUsuarios = presentacion.MainGUI.getGestorBanco().getGestorUsuarios();
		modelo.personas.Cliente clienteOrigen = gestorUsuarios.buscarCliente(dniOrigen);
		modelo.personas.Cliente clienteDestino = gestorUsuarios.buscarCliente(dniDestino);
		if (clienteOrigen == null || clienteDestino == null) {
			JOptionPane.showMessageDialog(this, "DNI de origen o destino no existe.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		gestor.GestorBanco gestorBanco = presentacion.MainGUI.getGestorBanco();
		modelo.cuentas.Cuenta cuentaO = gestorBanco.buscarCuenta(cuentaOrigen);
		modelo.cuentas.Cuenta cuentaD = gestorBanco.buscarCuenta(cuentaDestino);
		if (cuentaO == null || cuentaD == null || !cuentaO.getCliente().getDni().equals(dniOrigen) || !cuentaD.getCliente().getDni().equals(dniDestino)) {
			JOptionPane.showMessageDialog(this, "Las cuentas no existen o no pertenecen a los clientes indicados.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		var transferencia = gestorBanco.realizarTransferencia(cuentaOrigen, cuentaDestino, monto, "Transferencia entre clientes", "");
		if (transferencia != null) {
			JOptionPane.showMessageDialog(this, "Transferencia realizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			txtDniOrigen.setText("");
			txtCuentaOrigen.setText("");
			txtDniDestino.setText("");
			txtCuentaDestino.setText("");
			txtMonto.setText("");
		} else {
			JOptionPane.showMessageDialog(this, "No se pudo realizar la transferencia.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}

