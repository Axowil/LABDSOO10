package presentacion.administrador;

import gestor.GestorBanco;
import presentacion.MainGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FrmGestionClientes extends JFrame {
    private static final Pattern TEXTO_VALIDO = Pattern.compile("^[a-z A-Z]{3,50}$");
    private final GestorBanco gb = MainGUI.getGestorBanco();
    private JTable tabla;

    public FrmGestionClientes() {
        super("Gestión de Clientes - Administrador");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(new Color(248, 249, 250));

        JLabel tit = new JLabel(" Gestión de Clientes");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tit.setForeground(new Color(44, 62, 80));
        tit.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableModel m = new DefaultTableModel(new String[]{"ID", "Nombre", "Califcacion crediticia"}, 0);
        gb.getGestorUsuarios().getClientes().forEach(emp -> m.addRow(new Object[]{
            emp.getIdCliente(),
            emp.getNombreCompleto(),
            emp.getCategoria()
        }));
        tabla = new JTable(m);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane sp = new JScrollPane(tabla);

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bot.setOpaque(false);

        JButton btnCal = new JButton("Cambiar calificacion crediticia");
        Stream.of(btnCal).forEach(b -> b.setFont(new Font("Segoe UI", Font.PLAIN, 14)));
        bot.add(btnCal);

        btnCal.addActionListener(ev -> cambiarCalificacion());

        p.add(tit, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        p.add(bot, BorderLayout.SOUTH);

        add(p);
    }
    private void cambiarCalificacion() {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String newCal = JOptionPane.showInputDialog(this, "Nueva calificacion creditica:");
        if ((!newCal.isEmpty() && !TEXTO_VALIDO.matcher(newCal).matches())) {
            JOptionPane.showMessageDialog(this, "El formato de la calificacion no es valido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String idCliente = (String) tabla.getValueAt(row, 0);
        try {
            gb.getGestorUsuarios().actualizarCalificacion(idCliente.substring(4), newCal);
            tabla.setValueAt(newCal, row, 2);
            JOptionPane.showMessageDialog(this, "Calificación de cliente " + idCliente + " actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar la calificación: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}