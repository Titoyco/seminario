import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JPanel mainPanel;

    public MainWindow() {
        setTitle("Sistema de Préstamos Personales");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu clientesMenu = new JMenu("Clientes");
        JMenuItem altaClienteItem = new JMenuItem("Alta de Cliente");
        clientesMenu.add(altaClienteItem);
        menuBar.add(clientesMenu);
        setJMenuBar(menuBar);

        // Panel principal donde se mostrarán los distintos paneles
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JLabel("Bienvenido al sistema", JLabel.CENTER), BorderLayout.CENTER);
        add(mainPanel);

        // Acción para mostrar AltaClientePanel
        altaClienteItem.addActionListener(e -> mostrarAltaCliente());

        // Puedes agregar otras opciones de menú y paneles aquí
    }

    private void mostrarAltaCliente() {
        mainPanel.removeAll();
        mainPanel.add(new AltaClientePanel(), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
}