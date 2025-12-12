package presentacion;
import gestor.GestorBanco;
import javax.swing.SwingUtilities;

public class MainGUI {
    private static GestorBanco gestorBanco;
    public static GestorBanco getGestorBanco() {
        if (gestorBanco == null) {
            gestorBanco = new GestorBanco();
            System.out.println("GestorBanco inicializado correctamente");
        }
        return gestorBanco;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName()
                    );
                } catch (Exception e) {
                    System.err.println("No se pudo aplicar el tema del sistema: " + e.getMessage());
                }
                FrmLogin login = new FrmLogin();
                login.setVisible(true);
                login.setLocationRelativeTo(null);
                System.out.println("Aplicación GUI iniciada correctamente");
            }
        });
    }
}