// En presentacion.empleado.FrmEliminarUsuarioCliente.java
package presentacion.empleado;

import gestor.GestorUsuarios;
import modelo.personas.Usuario;
import modelo.personas.UsuarioCliente;

import javax.swing.*;
import java.awt.*;

public class FrmEliminarUsuarioCliente extends JFrame {

    private GestorUsuarios gestorUsuarios;
    private JComboBox<String> cboUsuarios;

    public FrmEliminarUsuarioCliente(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;
        initComponents();
        cargarUsuariosCliente();
    }

    private void initComponents() {
        setTitle("Eliminar usuario de cliente");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuario cliente:"));
        cboUsuarios = new JComboBox<>();
        panel.add(cboUsuarios);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        add(panel, BorderLayout.CENTER);
        add(btnEliminar, BorderLayout.SOUTH);
    }

    private void cargarUsuariosCliente() {
        cboUsuarios.removeAllItems();
        for (Usuario u : gestorUsuarios.getUsuarios()) {   // lee desde BD
            if (u instanceof UsuarioCliente) {
                cboUsuarios.addItem(u.getNombreUsuario());
            }
        }
        if (cboUsuarios.getItemCount() == 0) {
            cboUsuarios.addItem("No hay usuarios cliente");
        }
    }

    private void eliminarSeleccionado() {
        String seleccionado = (String) cboUsuarios.getSelectedItem();
        if (seleccionado == null || seleccionado.equals("No hay usuarios cliente")) {
            JOptionPane.showMessageDialog(this, "No hay usuario válido seleccionado",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el usuario cliente '" + seleccionado + "'?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        boolean ok = gestorUsuarios.eliminarUsuario(seleccionado);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarUsuariosCliente(); // refrescar lista
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
