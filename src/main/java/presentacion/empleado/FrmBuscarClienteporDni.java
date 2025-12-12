package presentacion.empleado;

import javax.swing.*;
import java.awt.*;
import presentacion.MainGUI;
import gestor.GestorUsuarios;
import modelo.personas.Cliente;

public class FrmBuscarClienteporDni extends JFrame {
    private JTextField txtDni;
    private JButton btnBuscar;
    private JTextArea txtResultado;

    public FrmBuscarClienteporDni() {
        setTitle("Buscar Cliente por DNI");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior para búsqueda
        JPanel panelBusqueda = new JPanel(new GridLayout(2, 2, 10, 10));
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBusqueda.add(new JLabel("DNI:"));
        txtDni = new JTextField();
        panelBusqueda.add(txtDni);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        // Panel central para resultados
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        panelResultados.add(new JLabel("Resultados:"), BorderLayout.NORTH);
        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        panelResultados.add(scrollPane, BorderLayout.CENTER);
        add(panelResultados, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscarCliente());
    }

    private void buscarCliente() {
        String dni = txtDni.getText().trim();

        // Validación
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa un DNI.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            GestorUsuarios gestorUsuarios = MainGUI.getGestorBanco().getGestorUsuarios();
            Cliente cliente = gestorUsuarios.buscarClientePorDni(dni);

            if (cliente != null) {
                mostrarResultados(cliente);
            } else {
                txtResultado.setText("No se encontró cliente con DNI: " + dni);
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            txtResultado.setText("Error al buscar: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarResultados(Cliente cliente) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("ID Cliente: ").append(cliente.getIdCliente()).append("\n");
        resultado.append("DNI: ").append(cliente.getDni()).append("\n");
        resultado.append("Nombre: ").append(cliente.getNombre()).append("\n");
        resultado.append("Apellido: ").append(cliente.getApellido()).append("\n");
        resultado.append("Email: ").append(cliente.getEmail()).append("\n");
        resultado.append("Teléfono: ").append(cliente.getTelefono()).append("\n");
        resultado.append("Dirección: ").append(cliente.getDireccion()).append("\n");
        resultado.append("Categoría: ").append(cliente.getCategoria()).append("\n");
        resultado.append("Límite de Crédito: $").append(cliente.getLimiteCredito()).append("\n");
        resultado.append("Fecha de Registro: ").append(cliente.getFechaRegistro()).append("\n");

        txtResultado.setText(resultado.toString());
        JOptionPane.showMessageDialog(this, "Cliente encontrado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}