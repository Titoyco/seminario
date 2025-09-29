import javax.swing.*;
import java.awt.*;
import java.util.List;

import Controller.*;
import Model.*;
import View.*;
import View.Clientes.AltaClientePanel;
import View.Clientes.BajaClientePanel;
import View.Clientes.BuscarClientesPanel;
import View.Clientes.ListaClientesPanel;
import View.Clientes.ModificarClientePanel;
import View.Clientes.DeudaClientePanel;
import View.Creditos.AltaCreditoPanel;
import View.Creditos.ListarCreditosClientePanel;
import View.Pagos.AnularPagoPanel;
import View.Pagos.ListarPagosClientePanel;
import View.Pagos.PagarCuotaPanel;


public class MainWindow extends JFrame {

    private JPanel mainPanel;

    public MainWindow() {
        setTitle("Sistema de Créditos Personales - SIGLO 21");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

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

        // ---- Menú Clientes ----
        JMenu clientesMenu = new JMenu("Clientes");
        JMenuItem altaClienteItem = new JMenuItem("Alta de Cliente");
        altaClienteItem.addActionListener(e -> mostrarAltaCliente());
        clientesMenu.add(altaClienteItem);

        JMenuItem modificarClienteItem = new JMenuItem("Modificar Cliente");
        modificarClienteItem.addActionListener(e -> mostrarModificarClientePanel());
        clientesMenu.add(modificarClienteItem);

        JMenuItem buscarClienteItem = new JMenuItem("Buscar Cliente");
        buscarClienteItem.addActionListener(e -> mostrarBuscarClientesPanel());
        clientesMenu.add(buscarClienteItem);

        JMenuItem bajaClienteItem = new JMenuItem("Baja de Cliente");
        bajaClienteItem.addActionListener(e -> mostrarBajaClientePanel());
        clientesMenu.add(bajaClienteItem);

        JMenuItem listarClientesItem = new JMenuItem("Listar Clientes");
        listarClientesItem.addActionListener(e -> mostrarListaClientes());
        clientesMenu.add(listarClientesItem);

        menuBar.add(clientesMenu);

        // ---- Menú Créditos (sin Pagar Cuota) ----
        JMenu creditosMenu = new JMenu("Créditos");
        JMenuItem altaCreditoItem = new JMenuItem("Alta de Crédito");
        altaCreditoItem.addActionListener(e -> mostrarAltaCredito());
        creditosMenu.add(altaCreditoItem);

        JMenuItem listarCreditosClienteItem = new JMenuItem("Créditos por Cliente");
        listarCreditosClienteItem.addActionListener(e -> mostrarListarCreditosCliente());
        creditosMenu.add(listarCreditosClienteItem);

        JMenuItem listarTodosCreditosItem = new JMenuItem("Listar Todos los Créditos");
        listarTodosCreditosItem.addActionListener(e -> mostrarListarTodosCreditos());
        creditosMenu.add(listarTodosCreditosItem);

        menuBar.add(creditosMenu);

        // ---- Menú Pagos ----
        JMenu pagosMenu = new JMenu("Pagos");
        JMenuItem pagarCuotaItem = new JMenuItem("Pagar Cuota");
        pagarCuotaItem.addActionListener(e -> mostrarPagarCuota());
        pagosMenu.add(pagarCuotaItem);

        JMenuItem listarPagosItem = new JMenuItem("Listar Pagos por Cliente");
        listarPagosItem.addActionListener(e -> mostrarListarPagosCliente());
        pagosMenu.add(listarPagosItem);

        JMenuItem anularPagoItem = new JMenuItem("Anular Pago");
        anularPagoItem.addActionListener(e -> mostrarAnularPago());
        pagosMenu.add(anularPagoItem);

        menuBar.add(pagosMenu);

        // ---- Menú Lote ----
        JMenu loteMenu = new JMenu("Lote");
        JMenuItem loteActualItem = new JMenuItem("Lote Actual");
        loteActualItem.addActionListener(e -> mostrarLotePanel());
        loteMenu.add(loteActualItem);
        menuBar.add(loteMenu);

        // ---- Menú Sistema ----
        JMenu sistemaMenu = new JMenu("Sistema");
        JMenuItem cambioContraItem = new JMenuItem("Cambio de contraseña");
        cambioContraItem.addActionListener(e -> mostrarCambioContraPanel());
        sistemaMenu.add(cambioContraItem);

        JMenuItem cerrarSesionItem = new JMenuItem("Cerrar sesión");
        cerrarSesionItem.addActionListener(e -> {
            setJMenuBar(null);
            mostrarLogin();
        });
        sistemaMenu.add(cerrarSesionItem);

        menuBar.add(sistemaMenu);

        setJMenuBar(menuBar);

        mainPanel.removeAll();
        JPanel bienvenidaPanel = new JPanel(new BorderLayout());
        bienvenidaPanel.setBackground(new Color(245, 249, 255));
        JLabel bienvenida = new JLabel("Bienvenido al sistema", JLabel.CENTER);
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bienvenida.setForeground(new Color(56, 81, 145));
        bienvenida.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        bienvenidaPanel.add(bienvenida, BorderLayout.CENTER);
        mainPanel.add(bienvenidaPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Pagos
    private void mostrarListarPagosCliente() {
        mainPanel.removeAll();
        ListarPagosClientePanel panel = new ListarPagosClientePanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }
    private void mostrarAnularPago() {
        mainPanel.removeAll();
        AnularPagoPanel panel = new AnularPagoPanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }
    private void mostrarPagarCuota() {
        mainPanel.removeAll();
        PagarCuotaPanel panel = new PagarCuotaPanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Lote
    private void mostrarLotePanel() {
        mainPanel.removeAll();
        View.LotePanel panel = new View.LotePanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // ---------- CLIENTES ----------
    private void mostrarAltaCliente() {
        mainPanel.removeAll();
        AltaClientePanel panel = new AltaClientePanel();
        panel.setGuardarListener(e -> {
            boolean ok = ClienteController.altaCliente(
                    panel.getNombre(),
                    panel.getDocumento(),
                    panel.getDireccion(),
                    panel.getTelefono(),
                    panel.getEmail()
            );
            if (ok) {
                JOptionPane.showMessageDialog(panel, "Cliente guardado correctamente.");
                panel.limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(panel, "Error al guardar cliente.");
            }
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarBajaClientePanel() {
        mainPanel.removeAll();
        BajaClientePanel panel = new BajaClientePanel();
        panel.cargarClientes();
        panel.setEliminarListener(e -> {
            String idStr = panel.getId();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Seleccione un cliente.");
                return;
            }
            try {
                int id = Integer.parseInt(idStr);
                boolean ok = ClienteController.bajaCliente(id);
                if (ok) {
                    JOptionPane.showMessageDialog(panel, "Cliente eliminado.");
                    panel.cargarClientes();
                } else {
                    JOptionPane.showMessageDialog(panel, "No se pudo eliminar.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID inválido.");
            }
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarModificarClientePanel() {
        mainPanel.removeAll();
        ModificarClientePanel panel = new ModificarClientePanel();
        panel.setGuardarListener(e -> {
            Cliente actualizado = panel.getClienteActualizado();
            if (actualizado == null) {
                JOptionPane.showMessageDialog(panel, "Seleccione un cliente.");
                return;
            }
            boolean ok = ClienteController.modificarCliente(actualizado);
            JOptionPane.showMessageDialog(panel, ok ? "Cliente modificado." : "Error al modificar.");
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarBuscarClientesPanel() {
        mainPanel.removeAll();
        BuscarClientesPanel panel = new BuscarClientesPanel();

        panel.setModificarListener(e -> {
            Integer id = panel.getClienteSeleccionadoId();
            if (id != null) mostrarModificarClientePanel(id);
            else JOptionPane.showMessageDialog(panel, "Seleccione un cliente.");
        });

        panel.setEliminarListener(e -> {
            Integer id = panel.getClienteSeleccionadoId();
            if (id != null) mostrarBajaClientePanel(id);
            else JOptionPane.showMessageDialog(panel, "Seleccione un cliente.");
        });
/* 
        panel.setVerCreditosListener(e -> {
            Integer id = panel.getClienteSeleccionadoId();
            if (id != null) {
                mainPanel.removeAll();
                ListarCreditosClientePanel creditosPanel = new ListarCreditosClientePanel(id);
                mainPanel.add(creditosPanel, BorderLayout.CENTER);
                refrescar();
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione un cliente.");
            }
        });

        panel.setVerPagosListener(e -> {
            Integer id = panel.getClienteSeleccionadoId();
            if (id != null) {
                mainPanel.removeAll();
                ListarPagosClientePanel pagosPanel = new ListarPagosClientePanel(id);
                mainPanel.add(pagosPanel, BorderLayout.CENTER);
                refrescar();
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione un cliente.");
            }
        });
*/
        panel.setVerDeudaListener(e -> {
            Integer id = panel.getClienteSeleccionadoId();
            if (id != null) {
                mainPanel.removeAll();
                DeudaClientePanel deudaPanel = new DeudaClientePanel(id, () -> mostrarBuscarClientesPanel());
                mainPanel.add(deudaPanel, BorderLayout.CENTER);
                refrescar();
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione un cliente.");
            }
        });

        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarDeudaCliente(int idCliente) {
        mainPanel.removeAll();
        DeudaClientePanel panel = new DeudaClientePanel(idCliente, () -> mostrarBuscarClientesPanel());
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarModificarClientePanel(int clienteId) {
        mainPanel.removeAll();
        ModificarClientePanel panel = new ModificarClientePanel(clienteId);
        panel.setGuardarListener(e -> {
            Cliente actualizado = panel.getClienteActualizado();
            if (actualizado == null) {
                JOptionPane.showMessageDialog(panel, "Seleccione un cliente.");
                return;
            }
            boolean ok = ClienteController.modificarCliente(actualizado);
            JOptionPane.showMessageDialog(panel, ok ? "Cliente modificado." : "Error al modificar.");
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarBajaClientePanel(int clienteId) {
        mainPanel.removeAll();
        BajaClientePanel panel = new BajaClientePanel(clienteId);
        panel.setEliminarListener(e -> {
            String idStr = panel.getId();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Seleccione un cliente.");
                return;
            }
            try {
                int id = Integer.parseInt(idStr);
                boolean ok = ClienteController.bajaCliente(id);
                JOptionPane.showMessageDialog(panel, ok ? "Cliente eliminado." : "No se pudo eliminar.");
                panel.cargarClientes();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID inválido.");
            }
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarListaClientes() {
        mainPanel.removeAll();
        ListaClientesPanel panel = new ListaClientesPanel();
        List<Cliente> clientes = ClienteController.listarClientes();
        panel.setClientes(clientes);
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // ---------- SISTEMA ----------
    private void mostrarCambioContraPanel() {
        mainPanel.removeAll();
        CambioPassPanel panel = new CambioPassPanel(() -> mostrarMenus());
        new PassController(panel, () -> mostrarMenus());
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // ---------- CRÉDITOS ----------
    private void mostrarAltaCredito() {
        mainPanel.removeAll();
        AltaCreditoPanel panel = new AltaCreditoPanel();
        panel.setCrearListener(() -> {
            // callback vacío
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarListarCreditosCliente() {
        mainPanel.removeAll();
        ListarCreditosClientePanel panel = new ListarCreditosClientePanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarListarTodosCreditos() {
        mainPanel.removeAll();
        View.Creditos.ListarTodosCreditosPanel panel = new View.Creditos.ListarTodosCreditosPanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }   

    // Utilidad
    private void refrescar() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}