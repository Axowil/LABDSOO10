package presentacion.empleado;

import javax.swing.*;
import java.awt.*;

public class FrmListaClientes extends JFrame {
	private JTable tablaClientes;

	public FrmListaClientes() {
		setTitle("Lista de Clientes");
		setSize(500, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		String[] columnas = {"DNI", "Nombre", "Apellido"};
		gestor.GestorUsuarios gestorUsuarios = presentacion.MainGUI.getGestorBanco().getGestorUsuarios();
		java.util.List<modelo.personas.Cliente> clientes = gestorUsuarios.getClientes();
		Object[][] datos = new Object[clientes.size()][3];
		for (int i = 0; i < clientes.size(); i++) {
			datos[i][0] = clientes.get(i).getDni();
			datos[i][1] = clientes.get(i).getNombre();
			datos[i][2] = clientes.get(i).getApellido();
		}
		tablaClientes = new JTable(datos, columnas);
		JScrollPane scrollPane = new JScrollPane(tablaClientes);
		add(scrollPane, BorderLayout.CENTER);
	}
}

