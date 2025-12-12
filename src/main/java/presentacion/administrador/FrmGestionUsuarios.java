package presentacion.administrador;

import gestor.GestorBanco;
import presentacion.MainGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.stream.Stream;

public class FrmGestionUsuarios extends JFrame {

    private final GestorBanco gb = MainGUI.getGestorBanco();
    private JTable tabla;

    public FrmGestionUsuarios() {
        super("Gestión de Usuarios - Administrador");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(new Color(248, 249, 250));

        JLabel tit = new JLabel(" Gestión de Usuarios");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tit.setForeground(new Color(52, 73, 94));
        tit.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Usuario", "Tipo", "Estado"}, 0);
        gb.getGestorUsuarios().getUsuarios().forEach(u ->
                model.addRow(new Object[]{
                        u.getNombreUsuario(),
                        u.getClass().getSimpleName().replace("Usuario", ""),
                        u.isEstado() ? "Activo" : "Inactivo"})
        );
        tabla = new JTable(model);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane sp = new JScrollPane(tabla);

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bot.setOpaque(false);

        JButton btnAct = new JButton(" Activar");
        JButton btnDes = new JButton(" Desactivar");
        JButton btnEli = new JButton(" Eliminar");
        Stream.of(btnAct, btnDes, btnEli).forEach(b -> b.setFont(new Font("Segoe UI", Font.PLAIN, 14)));
        bot.add(btnAct);
        bot.add(btnDes);
        bot.add(btnEli);

        btnAct.addActionListener(ev -> cambiarEstado(true));
        btnDes.addActionListener(ev -> cambiarEstado(false));
        btnEli.addActionListener(ev -> eliminarUsuario());

        p.add(tit, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        p.add(bot, BorderLayout.SOUTH);

        add(p);
    }

    private void cambiarEstado(boolean activar) {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String user = (String) tabla.getValueAt(row, 0);
        if (activar) gb.getGestorUsuarios().activarUsuario(user);
        else gb.getGestorUsuarios().desactivarUsuario(user);
        tabla.setValueAt(activar ? "Activo" : "Inactivo", row, 2);
    }

    private void eliminarUsuario() {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String user = (String) tabla.getValueAt(row, 0);
        int ok = JOptionPane.showConfirmDialog(this, "¿Confirma eliminar " + user + "?", "Eliminar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            boolean exito = gb.getGestorUsuarios().eliminarUsuario(user);
            if (exito) ((DefaultTableModel) tabla.getModel()).removeRow(row);
        }
    }
}