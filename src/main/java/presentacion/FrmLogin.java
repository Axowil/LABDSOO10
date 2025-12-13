package presentacion;

import gestor.GestorBanco;
import gestor.GestorUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FrmLogin extends javax.swing.JFrame {
    
    private GestorBanco gestorBanco;
    private GestorUsuarios gestorUsuarios;
    
    private final Color COLOR_PRIMARY = new Color(41, 128, 185); 
    private final Color COLOR_SUCCESS = new Color(46, 204, 113); 
    private final Color COLOR_DANGER = new Color(231, 76, 60); 
    private final Color COLOR_DARK = new Color(44, 62, 80);
    private final Color COLOR_WHITE = Color.WHITE;
    
    public FrmLogin() {
        initComponents();
        this.gestorBanco = MainGUI.getGestorBanco();
        this.gestorUsuarios = gestorBanco.getGestorUsuarios();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Sistema bancario -- Inicio de Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(false); 
    }
    
    private void initComponents() {
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        panelPrincipal.setBackground(COLOR_WHITE);

        JPanel panelIzquierdo = crearPanelIzquierdo();
        panelIzquierdo.setPreferredSize(new Dimension(380, 600));
        panelPrincipal.add(panelIzquierdo, BorderLayout.WEST);

        JPanel panelDerecho = crearPanelFormulario();
        panelDerecho.setPreferredSize(new Dimension(470, 600));
        panelPrincipal.add(panelDerecho, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }
    
    private JPanel crearPanelIzquierdo() {
    JPanel panel = new JPanel();
    panel.setBackground(COLOR_PRIMARY);
    panel.setPreferredSize(new Dimension(350, 600));
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.insets = new Insets(10, 30, 10, 30);
    
    // Icono del banco
    JLabel lblIcono = new JLabel();
    try {
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("imagen_banco.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        lblIcono.setIcon(new ImageIcon(imagenEscalada));
    } catch (Exception e) {
        lblIcono.setText("🏛");
        lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        lblIcono.setForeground(COLOR_WHITE);
    }
    lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
    gbc.gridy = 0;
    gbc.insets = new Insets(60, 30, 20, 30);
    panel.add(lblIcono, gbc);
    
    // Título
    JLabel lblTitulo = new JLabel("NeoBank");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
    lblTitulo.setForeground(COLOR_WHITE);
    lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
    gbc.gridy = 1;
    gbc.insets = new Insets(10, 30, 5, 30);
    panel.add(lblTitulo, gbc);
    
    JLabel lblSubtitulo = new JLabel("Banca digital segura");
    lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
    lblSubtitulo.setForeground(new Color(236, 240, 241));
    lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
    gbc.gridy = 2;
    gbc.insets = new Insets(5, 30, 40, 30);
    panel.add(lblSubtitulo, gbc);
    
    // Descripción
    JTextArea txtDescripcion = new JTextArea();
    txtDescripcion.setText("Consulta saldos, realiza\n" +
                      "transferencias, gestiona\n" +
                      "cuentas y mucho más\n" +
                      "desde un solo lugar.");
    txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    txtDescripcion.setForeground(new Color(236, 240, 241));
    txtDescripcion.setBackground(COLOR_PRIMARY);
    txtDescripcion.setEditable(false);
    txtDescripcion.setFocusable(false);
    txtDescripcion.setLineWrap(true);
    txtDescripcion.setWrapStyleWord(true);
    txtDescripcion.setBorder(null);
    gbc.gridy = 3;
    gbc.insets = new Insets(10, 30, 60, 30);
    panel.add(txtDescripcion, gbc);
    
    return panel;
}

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_WHITE);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título del formulario
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_DARK);
        gbc.gridy = 0;
        gbc.insets = new Insets(40, 40, 10, 40);
        panel.add(lblTitulo, gbc);
        

        JLabel lblSubtitulo = new JLabel("Ingrese sus credenciales para continuar");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(127, 140, 141));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 40, 30, 40);
        panel.add(lblSubtitulo, gbc);
        
        // Campo Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(COLOR_DARK);
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 40, 5, 40);
        panel.add(lblUsuario, gbc);
        
        JTextField txtUsuario = crearCampoTextoEstilizado();
        txtUsuario.setToolTipText("Ingrese su nombre de usuario");
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(txtUsuario, gbc);
        
        // Campo Contraseña
        JLabel lblContraseña = new JLabel("Contraseña");
        lblContraseña.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblContraseña.setForeground(COLOR_DARK);
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 40, 5, 40);
        panel.add(lblContraseña, gbc);
        
        JPasswordField txtContraseña = crearCampoPasswordEstilizado();
        txtContraseña.setToolTipText("Ingrese su contraseña");
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(txtContraseña, gbc);
        
        // Campo Tipo Usuario
        JLabel lblTipo = new JLabel("Tipo de Usuario");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTipo.setForeground(COLOR_DARK);
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 40, 5, 40);
        panel.add(lblTipo, gbc);
        
        JComboBox<String> cmbTipoUsuario = crearComboEstilizado();
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 40, 25, 40);
        panel.add(cmbTipoUsuario, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setBackground(COLOR_WHITE);
        
        JButton btnIngresar = crearBotonEstilizado("Ingresar", COLOR_SUCCESS);
        JButton btnSalir = crearBotonEstilizado("Salir", COLOR_DANGER);
        
        panelBotones.add(btnIngresar);
        panelBotones.add(btnSalir);
        
        gbc.gridy = 8;
        gbc.insets = new Insets(10, 40, 40, 40);
        panel.add(panelBotones, gbc);
        
        // Eventos
        btnIngresar.addActionListener(e -> procesarIngreso(txtUsuario, txtContraseña, cmbTipoUsuario));
        btnSalir.addActionListener(e -> System.exit(0));
        
        // Enter para ingresar
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    procesarIngreso(txtUsuario, txtContraseña, cmbTipoUsuario);
                }
            }
        };
        txtUsuario.addKeyListener(enterListener);
        txtContraseña.addKeyListener(enterListener);
        
        return panel;
    }
    
    /**
     * Crea un campo de texto estilizado
     */
    private JTextField crearCampoTextoEstilizado() {
        JTextField campo = new JTextField(20);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Efecto focus
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARY, 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });
        
        return campo;
    }
    
    
    private JPasswordField crearCampoPasswordEstilizado() {
        JPasswordField campo = new JPasswordField(20);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARY, 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });
        
        return campo;
    }
    
    private JComboBox<String> crearComboEstilizado() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"CLIENTE", "EMPLEADO", "ADMINISTRADOR"});
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(300, 40));
        combo.setBackground(COLOR_WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return combo;
    }
    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(140, 45));
        boton.setBackground(colorFondo);
        boton.setForeground(COLOR_WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorFondo.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorFondo);
            }
        });
        
        return boton;
    }

    private void procesarIngreso(JTextField txtUsuario, JPasswordField txtContraseña, JComboBox<String> cmbTipo) {
        String usuario = txtUsuario.getText().trim();
        String contraseña = new String(txtContraseña.getPassword());
        String tipoSeleccionado = cmbTipo.getSelectedItem().toString();
        
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (gestorUsuarios.autenticarUsuario(usuario, contraseña)) {
                String tipoUsuarioActual = gestorUsuarios.getTipoUsuarioActual();
                
                if (tipoUsuarioActual.equals(tipoSeleccionado)) {
                    mostrarMensaje("¡Bienvenido " + usuario + "!", "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
                    abrirMenuPrincipal(tipoUsuarioActual);
                    dispose();
                } else {
                    mostrarMensaje(
                        "No tiene permisos para ingresar como " + tipoSeleccionado + "\n" +
                        "Su tipo de usuario es: " + tipoUsuarioActual, 
                        "Permisos Insuficientes", 
                        JOptionPane.ERROR_MESSAGE
                    );
                    gestorUsuarios.cerrarSesion();
                }
            } else {
                mostrarMensaje("Usuario o contraseña incorrectos", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            mostrarMensaje("Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
    private void abrirMenuPrincipal(String tipoUsuario) {
        FrmMenuPrincipal menu = new FrmMenuPrincipal(tipoUsuario);
        menu.setVisible(true);
    }
}