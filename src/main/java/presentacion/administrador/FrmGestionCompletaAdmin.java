package presentacion.administrador;

import gestor.GestorBanco;
import modelo.cuentas.CuentaAhorros;
import modelo.cuentas.CuentaCorriente;
import presentacion.MainGUI;

import javax.swing.*;
import java.awt.*;

public class FrmGestionCompletaAdmin extends JFrame {

    private final GestorBanco gb = MainGUI.getGestorBanco();

    public FrmGestionCompletaAdmin() {
        super("Gestión Completa - Administrador");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        p.setBackground(new Color(236, 240, 241));

        JLabel tit = new JLabel(" Gestión Completa del Sistema");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 26));
        tit.setForeground(new Color(44, 62, 80));
        tit.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel cards = new JPanel(new GridLayout(2, 3, 20, 20));
        cards.setOpaque(false);

        cards.add(crearCard(" Total usuarios", String.valueOf(gb.getGestorUsuarios().getUsuarios().size()), new Color(52, 152, 219)));
        cards.add(crearCard(" Total cuentas", String.valueOf(gb.getCuentas().size()), new Color(46, 204, 113)));
        cards.add(crearCard(" Dinero en sistema", String.format("$%,.2f", gb.getCuentas().stream().mapToDouble(c -> c.getSaldo()).sum()), new Color(241, 196, 15)));
        cards.add(crearCard(" Transacciones", String.valueOf(gb.getTransacciones().size()), new Color(155, 89, 182)));
        cards.add(crearCard(" Ahorros", String.valueOf(gb.getCuentas().stream().filter(c -> c instanceof CuentaAhorros).count()), new Color(26, 188, 156)));
        cards.add(crearCard(" Corrientes", String.valueOf(gb.getCuentas().stream().filter(c -> c instanceof CuentaCorriente).count()), new Color(230, 126, 34)));

        p.add(tit, BorderLayout.NORTH);
        p.add(cards, BorderLayout.CENTER);

        add(p);
    }

    private JPanel crearCard(String titulo, String valor, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(titulo, JLabel.CENTER);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel v = new JLabel(valor, JLabel.CENTER);
        v.setForeground(Color.WHITE);
        v.setFont(new Font("Segoe UI", Font.BOLD, 32));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(180, 120));
        return card;
    }
}