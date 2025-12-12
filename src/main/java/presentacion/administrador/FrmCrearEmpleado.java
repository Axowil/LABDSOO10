package presentacion.administrador;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;
import presentacion.MainGUI;
import gestor.GestorUsuarios;

public class FrmCrearEmpleado extends JFrame {
    private JTextField txtNombre, txtApellido, txtDni, txtEmail, txtTelefono, txtDireccion;
    private JButton btnCrear;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern TELEFONO_VALIDO = Pattern.compile("^9\\d{8}$");
    private static final Pattern DNI_VALIDO = Pattern.compile("^\\d{8}$");
    private static final Pattern CREDENCIALES_VALIDAS = Pattern.compile("^[a-z A-Z]{3,50}$");

    public FrmCrearEmpleado() {
        setTitle("Crear Empleado");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("DNI:"));
        txtDni = new JTextField();
        panel.add(txtDni);
        
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        
        panel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panel.add(txtApellido);
        
        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);
        
        panel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);
        
        panel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panel.add(txtDireccion);
        
        btnCrear = new JButton("Crear");
        panel.add(btnCrear);
        add(panel, BorderLayout.CENTER);

        btnCrear.addActionListener(e -> crearCliente());
    }

    private void crearCliente() {
        String dni = txtDni.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        // Validación de campos obligatorios
        if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "DNI, Nombre y Apellido son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación de email
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Email inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación de teléfono
        if (!telefono.isEmpty() && !TELEFONO_VALIDO.matcher(telefono).matches()) {
            JOptionPane.showMessageDialog(this, "Teléfono debe contener solo 9 números.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //validación de DNI
        if ((!dni.isEmpty() && !DNI_VALIDO.matcher(dni).matches())) {
            JOptionPane.showMessageDialog(this, "Dni solo puede tener 8 digitos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Validación de nombre y apellido
        if ((!nombre.isEmpty() && !CREDENCIALES_VALIDAS.matcher(nombre).matches()) ||
            (!apellido.isEmpty() && !CREDENCIALES_VALIDAS.matcher(apellido).matches())) {
            JOptionPane.showMessageDialog(this, "Las credenciales solo pueden contener letras.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            GestorUsuarios gestorUsuarios = MainGUI.getGestorBanco().getGestorUsuarios();
            String idEmpleado = String.format("CLI%03d", gestorUsuarios.getClientes().size() + 1);
            String cargo = "Cajero";
            String departamento = "Atencion al Cliente";
            double salario = 2500.0;
            String turno = "Mañana";
            modelo.personas.Empleado empleado = new modelo.personas.Empleado(
                dni, nombre, apellido, email, telefono,
                java.time.LocalDate.now(), direccion,
                idEmpleado, cargo, departamento, salario, turno
            );
            
            boolean creado = gestorUsuarios.agregarEmpleado(empleado);

            if (creado) {
                JOptionPane.showMessageDialog(this, "Empleado creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe un empleado con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtDni.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
    }
}