package presentacion.administrador;

import gestor.GestorBanco;
import gestor.GestorUsuarios;
import modelo.cuentas.Cuenta;
import modelo.cuentas.CuentaAhorros;
import modelo.cuentas.CuentaCorriente;
import modelo.transacciones.Transaccion;
import presentacion.MainGUI;

import javax.swing.*;
import java.awt.*;


public class FrmReportesAdministrador extends JFrame {
    
    private final GestorBanco gestorBanco;
    private final GestorUsuarios gestorUsuarios;
    
    public FrmReportesAdministrador() {
        this.gestorBanco = MainGUI.getGestorBanco();
        this.gestorUsuarios = gestorBanco.getGestorUsuarios();
        initComponents();
        configurarVentana();
    }
    
    private void configurarVentana() {
        setTitle("Reportes y Estadísticas - Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setResizable(true);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel(" REPORTES Y ESTADÍSTICAS COMPLETAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel de pestañas
        JTabbedPane panelPestanas = new JTabbedPane();
        panelPestanas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Pestaña Resumen General
        panelPestanas.addTab(" Resumen General", crearPanelResumenGeneral());
        
        // Pestaña Usuarios
        panelPestanas.addTab(" Usuarios", crearPanelUsuarios());
        
        // Pestaña Cuentas
        panelPestanas.addTab(" Cuentas", crearPanelCuentas());
        
        // Pestaña Transacciones
        panelPestanas.addTab(" Transacciones", crearPanelTransacciones());
        
        // Panel botón cerrar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCerrar = new JButton(" Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.DARK_GRAY);
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        
        // Organizar
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelPestanas, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelResumenGeneral() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBackground(new Color(250, 250, 250));
        
        StringBuilder resumen = new StringBuilder();
        resumen.append("═══════════════════════════════════════════════════════\n");
        resumen.append("         RESUMEN GENERAL DEL SISTEMA BANCARIO\n");
        resumen.append("═══════════════════════════════════════════════════════\n\n");
        resumen.append(String.format("Fecha de Generación: %s\n\n", new java.util.Date()));
        
        // Totales generales
        resumen.append(" TOTALES GENERALES:\n");
        resumen.append(String.format("Total Usuarios:      %,d\n", gestorUsuarios.getUsuarios().size()));
        resumen.append(String.format("Total Clientes:      %,d\n", gestorUsuarios.getClientes().size()));
        resumen.append(String.format("Total Empleados:     %,d\n", gestorUsuarios.getEmpleados().size()));
        resumen.append(String.format("Total Cuentas:       %,d\n", gestorBanco.getCuentas().size()));
        resumen.append(String.format("Total Transacciones: %,d\n\n", gestorBanco.getTransacciones().size()));
        
        // Dinero en el sistema
        double totalDinero = gestorBanco.getCuentas().stream()
            .mapToDouble(Cuenta::getSaldo).sum();
        resumen.append(" DINERO EN EL SISTEMA:\n");
        resumen.append(String.format("Total en Cuentas:    $%,.2f\n\n", totalDinero));
        
        // Tipos de cuentas
        long ahorros = gestorBanco.getCuentas().stream()
            .filter(c -> c instanceof CuentaAhorros).count();
        long corriente = gestorBanco.getCuentas().stream()
            .filter(c -> c instanceof CuentaCorriente).count();
        
        resumen.append(" TIPOS DE CUENTAS:\n");
        resumen.append(String.format("Cuentas de Ahorros:  %,d (%.1f%%)\n", ahorros, (ahorros * 100.0 / gestorBanco.getCuentas().size())));
        resumen.append(String.format("Cuentas Corriente:   %,d (%.1f%%)\n\n", corriente, (corriente * 100.0 / gestorBanco.getCuentas().size())));
        
        resumen.append("═══════════════════════════════════════════════════════");
        
        textArea.setText(resumen.toString());
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 500));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JPanel panelStats = new JPanel(new GridLayout(3, 2, 15, 15));
        panelStats.setBorder(BorderFactory.createTitledBorder("Estadísticas de Usuarios"));
        
        // Contar tipos de usuarios
        long clientes = gestorUsuarios.getUsuarios().stream()
            .filter(u -> u instanceof modelo.personas.UsuarioCliente).count();
        long empleados = gestorUsuarios.getUsuarios().stream()
            .filter(u -> u instanceof modelo.personas.UsuarioEmpleado).count();
        long admins = gestorUsuarios.getUsuarios().stream()
            .filter(u -> u instanceof modelo.personas.UsuarioAdministrador).count();
        long activos = gestorUsuarios.getUsuarios().stream().filter(u -> u.isEstado()).count();
        long inactivos = gestorUsuarios.getUsuarios().size() - activos;
        
        addStat(panelStats, "Total Usuarios:", gestorUsuarios.getUsuarios().size());
        addStat(panelStats, "Clientes:", clientes);
        addStat(panelStats, "Empleados:", empleados);
        addStat(panelStats, "Administradores:", admins);
        addStat(panelStats, "Usuarios Activos:", activos);
        addStat(panelStats, "Usuarios Inactivos:", inactivos);
        
        panel.add(panelStats, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCuentas() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JPanel panelStats = new JPanel(new GridLayout(4, 2, 15, 15));
        panelStats.setBorder(BorderFactory.createTitledBorder("Estadísticas de Cuentas"));
        
        long totalCuentas = gestorBanco.getCuentas().size();
        long ahorros = gestorBanco.getCuentas().stream()
            .filter(c -> c instanceof CuentaAhorros).count();
        long corriente = gestorBanco.getCuentas().stream()
            .filter(c -> c instanceof CuentaCorriente).count();
        long activas = gestorBanco.getCuentas().stream()
            .filter(Cuenta::estaActiva).count();
        double totalDinero = gestorBanco.getCuentas().stream()
            .mapToDouble(Cuenta::getSaldo).sum();
        
        addStat(panelStats, "Total Cuentas:", totalCuentas);
        addStat(panelStats, "Cuentas Activas:", activas);
        addStat(panelStats, "Cuentas de Ahorros:", ahorros);
        addStat(panelStats, "Cuentas Corriente:", corriente);
        addStat(panelStats, "Dinero Total:", String.format("$%,.2f", totalDinero));
        
        // Saldo promedio
        double promedio = totalCuentas > 0 ? totalDinero / totalCuentas : 0;
        addStat(panelStats, "Saldo Promedio:", String.format("$%,.2f", promedio));
        
        panel.add(panelStats, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelTransacciones() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JPanel panelStats = new JPanel(new GridLayout(3, 2, 15, 15));
        panelStats.setBorder(BorderFactory.createTitledBorder("Estadísticas de Transacciones"));
        
        long totalTransacciones = gestorBanco.getTransacciones().size();
        
        // Contar por tipo
        long depositos = gestorBanco.getTransacciones().stream()
            .filter(t -> t instanceof modelo.transacciones.Deposito).count();
        long retiros = gestorBanco.getTransacciones().stream()
            .filter(t -> t instanceof modelo.transacciones.Retiro).count();
        long transferencias = gestorBanco.getTransacciones().stream()
            .filter(t -> t instanceof modelo.transacciones.Transferencia).count();
        
        addStat(panelStats, "Total Transacciones:", totalTransacciones);
        addStat(panelStats, "Depósitos:", depositos);
        addStat(panelStats, "Retiros:", retiros);
        addStat(panelStats, "Transferencias:", transferencias);
        
        // Monto promedio
        double montoPromedio = totalTransacciones > 0 ? 
            gestorBanco.getTransacciones().stream().mapToDouble(Transaccion::getMonto).average().orElse(0) : 0;
        addStat(panelStats, "Monto Promedio:", String.format("$%,.2f", montoPromedio));
        
        panel.add(panelStats, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void addStat(JPanel panel, String label, Object value) {
        JPanel statPanel = new JPanel(new BorderLayout());
        statPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JLabel lblLabel = new JLabel(" " + label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblValue = new JLabel(value.toString() + " ");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
        
        statPanel.add(lblLabel, BorderLayout.WEST);
        statPanel.add(lblValue, BorderLayout.EAST);
        panel.add(statPanel);
    }
}