import javax.swing.*;

import View.AltaClientePanel;
import View.CambioContraPanel;
import View.LoginPanel;

import java.awt.*;

public class MainWindow extends JFrame {
    private JPanel mainPanel;

    public MainWindow() {
        setTitle("Sistema de Préstamos Personales");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        

        mostrarLogin();
    }

    private void mostrarLogin() {
        mainPanel.removeAll();
        LoginPanel loginPanel = new LoginPanel(success -> {
            if (success) {
                mostrarMenus();
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void mostrarMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu clientesMenu = new JMenu("Clientes");
        JMenuItem altaClienteItem = new JMenuItem("Alta de Cliente");
        clientesMenu.add(altaClienteItem);
        menuBar.add(clientesMenu);

        JMenu creditosMenu = new JMenu("Créditos");
        menuBar.add(creditosMenu);

        JMenu reportesMenu = new JMenu("Reportes");
        menuBar.add(reportesMenu);

        JMenu sistemaMenu = new JMenu("Sistema");
        JMenuItem cambioContraItem = new JMenuItem("Cambio de contraseña");
        sistemaMenu.add(cambioContraItem);
        menuBar.add(sistemaMenu);

        // Acción para mostrar el panel de cambio de contraseña
        cambioContraItem.addActionListener(e -> mostrarCambioContraPanel());

        setJMenuBar(menuBar);

        mainPanel.removeAll();
        mainPanel.add(new JLabel("Bienvenido al sistema", JLabel.CENTER), BorderLayout.CENTER);

        altaClienteItem.addActionListener(e -> mostrarAltaCliente());
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void mostrarAltaCliente() {
        mainPanel.removeAll();
        mainPanel.add(new AltaClientePanel(), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void mostrarCambioContraPanel() {
    mainPanel.removeAll();
    mainPanel.add(new CambioContraPanel(), BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
}