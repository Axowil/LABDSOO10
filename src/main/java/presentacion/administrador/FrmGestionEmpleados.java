package presentacion.administrador;

import gestor.GestorBanco;
import presentacion.MainGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.stream.Stream;

public class FrmGestionEmpleados extends JFrame {

    private final GestorBanco gb = MainGUI.getGestorBanco();
    private JTable tabla;

    public FrmGestionEmpleados() {
        super("Gestión de Empleados - Administrador");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(new Color(248, 249, 250));

        JLabel tit = new JLabel(" Gestión de Empleados");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tit.setForeground(new Color(44, 62, 80));
        tit.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableModel m = new DefaultTableModel(new String[]{"ID", "Nombre", "Cargo", "Departamento", "Salario", "Activo"}, 0);
        gb.getGestorUsuarios().getEmpleados().forEach(emp ->
                m.addRow(new Object[]{
                        emp.getIdEmpleado(),
                        emp.getNombreCompleto(),
                        emp.getCargo(),
                        emp.getDepartamento(),
                        "$" + emp.getSalario(),
                        emp.isActivo() ? "Sí" : "No"})
        );
        tabla = new JTable(m);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane sp = new JScrollPane(tabla);

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bot.setOpaque(false);

        JButton btnAum = new JButton(" Aumentar Salario");
        JButton btnDep = new JButton(" Cambiar Depto");
        JButton btnEst = new JButton(" Cambiar Estado Activo/Desactivo");
        Stream.of(btnAum, btnDep, btnEst).forEach(b -> b.setFont(new Font("Segoe UI", Font.PLAIN, 14)));
        bot.add(btnAum);
        bot.add(btnDep);
        bot.add(btnEst);

        btnAum.addActionListener(ev -> aumentarSalario());
        btnDep.addActionListener(ev -> cambiarDepto());
        btnEst.addActionListener(ev -> toggleEstado());

        p.add(tit, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        p.add(bot, BorderLayout.SOUTH);

        add(p);
    }

    private void aumentarSalario() {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String porcS = JOptionPane.showInputDialog(this, "Porcentaje de aumento (%):");
        if (porcS == null) return;
        try {
            double porc = Double.parseDouble(porcS);
            var emp = gb.getGestorUsuarios().getEmpleados().get(row);
            emp.aumentarSalario(porc);
            tabla.setValueAt("$" + emp.getSalario(), row, 4);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Porcentaje inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarDepto() {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nuevo = JOptionPane.showInputDialog(this, "Nuevo departamento:");
        if (nuevo == null) return;
        var emp = gb.getGestorUsuarios().getEmpleados().get(row);
        emp.cambiarDepartamento(nuevo);
        tabla.setValueAt(nuevo, row, 3);
    }

    private void toggleEstado() {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        var emp = gb.getGestorUsuarios().getEmpleados().get(row);
        emp.setActivo(!emp.isActivo());
        tabla.setValueAt(emp.isActivo() ? "Sí" : "No", row, 5);
    }
}