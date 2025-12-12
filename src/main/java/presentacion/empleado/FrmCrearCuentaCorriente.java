package presentacion.empleado;

import javax.swing.*;
import java.awt.*;

public class FrmCrearCuentaCorriente extends JFrame {
	private JTextField txtDni, txtSaldoInicial;
	private JButton btnCrear;

	public FrmCrearCuentaCorriente() {
		setTitle("Crear Cuenta Corriente");
		setSize(350, 180);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel panel = new JPanel(new GridLayout(3,2,10,10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.add(new JLabel("DNI Cliente:"));
		txtDni = new JTextField();
		panel.add(txtDni);
		panel.add(new JLabel("Saldo Inicial:"));
		txtSaldoInicial = new JTextField();
		panel.add(txtSaldoInicial);
		btnCrear = new JButton("Crear");
		panel.add(btnCrear);
		add(panel, BorderLayout.CENTER);

		btnCrear.addActionListener(e -> crearCuentaCorriente());
	}

	private void crearCuentaCorriente() {
		String dni = txtDni.getText().trim();
		String saldoStr = txtSaldoInicial.getText().trim();
		double saldoInicial;
		try {
			saldoInicial = Double.parseDouble(saldoStr);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Saldo inicial inválido.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (dni.isEmpty()) {
			JOptionPane.showMessageDialog(this, "DNI es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		gestor.GestorUsuarios gestorUsuarios = presentacion.MainGUI.getGestorBanco().getGestorUsuarios();
		modelo.personas.Cliente cliente = gestorUsuarios.buscarCliente(dni);
		if (cliente == null) {
			JOptionPane.showMessageDialog(this, "No existe cliente con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		gestor.GestorBanco gestorBanco = presentacion.MainGUI.getGestorBanco();
		String numeroCuenta = String.format("CTE-%03d", gestorBanco.getCuentas().size() + 1);
		boolean creada = gestorBanco.crearCuentaCorriente(numeroCuenta, saldoInicial, cliente);
		if (creada) {
			JOptionPane.showMessageDialog(this, "Cuenta corriente creada exitosamente.\nNúmero: " + numeroCuenta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
			txtDni.setText("");
			txtSaldoInicial.setText("");
		} else {
			JOptionPane.showMessageDialog(this, "Ya existe una cuenta con ese número.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}

