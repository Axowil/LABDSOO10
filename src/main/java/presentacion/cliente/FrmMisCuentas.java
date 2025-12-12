package presentacion.cliente;

import gestor.GestorBanco;
import gestor.GestorUsuarios;
import modelo.cuentas.Cuenta;
import modelo.personas.UsuarioCliente;

import javax.swing.*;
import java.awt.*;

public class FrmMisCuentas extends JFrame {

    private GestorUsuarios gestorUsuarios;
    private GestorBanco gestorBanco;

    public FrmMisCuentas(GestorUsuarios gestorUsuarios, GestorBanco gestorBanco) {
        this.gestorUsuarios = gestorUsuarios;
        this.gestorBanco = gestorBanco;

        setTitle("Mis Cuentas Bancarias");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    // En presentacion/cliente/FrmMisCuentas.java -> método initUI()

    private void initUI() {
        UsuarioCliente usuarioActual = (UsuarioCliente) gestorUsuarios.getUsuarioActual();

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(250, 250, 250));

        StringBuilder info = new StringBuilder();

        if (usuarioActual != null && usuarioActual.getCliente() != null) {
            info.append("═══════════════════════════════════════════════════════\n");
            info.append("                  MIS CUENTAS BANCARIAS\n");
            info.append("═══════════════════════════════════════════════════════\n\n");
            info.append(String.format("Cliente: %s\n", usuarioActual.getCliente().getNombreCompleto()));
            info.append(String.format("DNI:     %s\n", usuarioActual.getCliente().getDni()));
            
            // --- CAMBIO CLAVE AQUÍ ---
            // 1. Obtenemos el DNI del usuario logueado
            String dni = usuarioActual.getCliente().getDni();
            
            // 2. Le pedimos al Gestor que busque las cuentas en la Base de Datos
            java.util.List<Cuenta> misCuentas = gestorBanco.obtenerCuentasDeCliente(dni);
            
            if (misCuentas.isEmpty()) {
                info.append("\n  No tiene cuentas asociadas o activas.\n");
            } else {
                info.append("\nCuentas Asociadas:\n");
                // 3. Iteramos directamente sobre los objetos Cuenta que trajo la BD
                for (Cuenta cuenta : misCuentas) {
                    info.append(String.format("  • %s [%s] - Saldo: $%,.2f - %s\n",
                            cuenta.getNumeroCuenta(),
                            cuenta.getClass().getSimpleName(), // Dice "CuentaAhorros" o "Corriente"
                            cuenta.getSaldo(),
                            cuenta.getEstado()
                    ));
                }
            }
            // -------------------------
            
            info.append("\n═══════════════════════════════════════════════════════");
        }

        textArea.setText(info.toString());
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }
}
