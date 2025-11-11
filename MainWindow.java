// MainWindow.java
// Clase principal de la aplicación que maneja la ventana principal y la navegación entre paneles.


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
import View.Creditos.AltaCreditoPanel;
import View.Creditos.ListarCreditosClientePanel;
import View.Deudas.DeudaClientePanel;
import View.Pagos.AnularPagoPanel;
import View.Pagos.ListarPagosClientePanel;
import View.Pagos.PagarCuotaPanel;
import View.Creditos.AnularCreditoPanel;
import View.Lotes.ListarLotesPanel;



public class MainWindow extends JFrame {

    private JPanel mainPanel;

    // Constructor
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

    // Método para mostrar el panel de login
    private void mostrarLogin() {
        mainPanel.removeAll();
        LoginPanel loginPanel = new LoginPanel(success -> {
            if (success) {
                mostrarMenus(); // Mostrar menús principales si el login es exitoso
            } else {
                // Si el login falla, mostrar un mensaje de error
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Agregar el panel de login al panel principal
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Método para mostrar los menús principales
private void mostrarMenus() {
    JMenuBar menuBar = new JMenuBar();

    // helper para aplicar estilo tipo "botón" a cualquier componente de menú (JButton, JMenu, etc.)
    java.util.function.Consumer<javax.swing.JComponent> styleMenuComponent = comp -> {
        comp.setFocusable(false);
        comp.setForeground(new Color(56, 81, 145));
    };

    // ----- inicio (botón) -----
    JButton inicioBtn = new JButton("Inicio");
    styleMenuComponent.accept(inicioBtn);
    inicioBtn.setForeground(Color.WHITE);
    inicioBtn.setBackground(new Color(56, 81, 145));
    inicioBtn.addActionListener(e -> mostrarMenus());
    menuBar.add(inicioBtn);
    menuBar.add(Box.createHorizontalStrut(8));

    // ---- Menú Clientes ----
    JMenu clientesMenu = new JMenu("Clientes");
    styleMenuComponent.accept(clientesMenu);
    // Submenu Clientes.AltaClientePanel
    JMenuItem altaClienteItem = new JMenuItem("Alta de Cliente");
    altaClienteItem.addActionListener(e -> mostrarAltaCliente());
    clientesMenu.add(altaClienteItem);
    // Submenu Clientes.ModificarClientePanel
    JMenuItem modificarClienteItem = new JMenuItem("Modificar Cliente");
    modificarClienteItem.addActionListener(e -> mostrarModificarClientePanel());
    clientesMenu.add(modificarClienteItem);
    // Submenu Clientes.BuscarClientesPanel
    JMenuItem buscarClienteItem = new JMenuItem("Buscar Cliente");
    buscarClienteItem.addActionListener(e -> mostrarBuscarClientesPanel());
    clientesMenu.add(buscarClienteItem);
    // Submenu Clientes.BajaClientePanel
    JMenuItem bajaClienteItem = new JMenuItem("Baja de Cliente");
    bajaClienteItem.addActionListener(e -> mostrarBajaClientePanel());
    clientesMenu.add(bajaClienteItem);
    // Submenu Clientes.ListaClientesPanel
    JMenuItem listarClientesItem = new JMenuItem("Listar Clientes");
    listarClientesItem.addActionListener(e -> mostrarListaClientes());
    clientesMenu.add(listarClientesItem);
    menuBar.add(clientesMenu);

    // ---- Menú Créditos  ----
    JMenu creditosMenu = new JMenu("Créditos");
    styleMenuComponent.accept(creditosMenu);
    JMenuItem altaCreditoItem = new JMenuItem("Alta de Crédito");
    altaCreditoItem.addActionListener(e -> mostrarAltaCredito());
    creditosMenu.add(altaCreditoItem);
    JMenuItem listarCreditosClienteItem = new JMenuItem("Créditos por Cliente");
    listarCreditosClienteItem.addActionListener(e -> mostrarListarCreditosCliente());
    creditosMenu.add(listarCreditosClienteItem);
    JMenuItem listarTodosCreditosItem = new JMenuItem("Listar Todos los Créditos");
    listarTodosCreditosItem.addActionListener(e -> mostrarListarTodosCreditos());
    creditosMenu.add(listarTodosCreditosItem);
    JMenuItem anularCreditoItem = new JMenuItem("Anular Crédito");
    anularCreditoItem.addActionListener(e -> mostrarAnularCredito());
    creditosMenu.add(anularCreditoItem);
    menuBar.add(creditosMenu);

    // ---- Menú Pagos ----
    JMenu pagosMenu = new JMenu("Pagos");
    styleMenuComponent.accept(pagosMenu);
    JMenuItem pagarCuotaItem = new JMenuItem("Pagar Cuota");
    pagarCuotaItem.addActionListener(e -> mostrarPagarCuota());
    pagosMenu.add(pagarCuotaItem);
    JMenuItem listarPagosItem = new JMenuItem("Listar Pagos por Cliente");
    listarPagosItem.addActionListener(e -> mostrarListarPagosCliente());
    pagosMenu.add(listarPagosItem);
    JMenuItem anularPagoItem = new JMenuItem("Anular Pago");
    anularPagoItem.addActionListener(e -> mostrarAnularPago());
    pagosMenu.add(anularPagoItem);
    JMenuItem listarTodosPagosItem = new JMenuItem("Listar Todos los Pagos");
    listarTodosPagosItem.addActionListener(e -> mostrarListarTodosPagos());
    pagosMenu.add(listarTodosPagosItem);
    menuBar.add(pagosMenu);

    // ---- Menú Deudas ----
    JMenu deudasMenu = new JMenu("Deudas");
    styleMenuComponent.accept(deudasMenu);
    JMenuItem deudaClienteItem = new JMenuItem("Deuda por Cliente");
    deudaClienteItem.addActionListener(e -> mostrarDeudaClientePanel());
    deudasMenu.add(deudaClienteItem);
    JMenuItem listarDeudasItem = new JMenuItem("Listar Todas las Deudas");
    listarDeudasItem.addActionListener(e -> mostrarListarDeudas());
    deudasMenu.add(listarDeudasItem);
    menuBar.add(deudasMenu);

    // ---- Menú Lote ----
    JMenu loteMenu = new JMenu("Lote");
    styleMenuComponent.accept(loteMenu);
    JMenuItem loteActualItem = new JMenuItem("Lote Actual");
    loteActualItem.addActionListener(e -> mostrarLotePanel());
    loteMenu.add(loteActualItem);
    JMenuItem listarLotesItem = new JMenuItem("Listar Lotes");
    listarLotesItem.addActionListener(e -> mostrarListarLotesPanel());
    loteMenu.add(listarLotesItem);
    menuBar.add(loteMenu);

    // ---- Menú Sistema ----
    JMenu sistemaMenu = new JMenu("Sistema");
    styleMenuComponent.accept(sistemaMenu);
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

    setJMenuBar(menuBar); // Establecer la barra de menús en la ventana principal

    // Mostrar panel de bienvenida
    mainPanel.removeAll(); // Limpiar el panel principal
    JPanel bienvenidaPanel = new JPanel(new BorderLayout());
    bienvenidaPanel.setBackground(new Color(245, 249, 255));

    // Logo (centrado)
    JLabel logo = new JLabel(new ImageIcon("img/logo.png"));
    logo.setHorizontalAlignment(JLabel.CENTER);

    // Texto de bienvenida (más arriba)
    JLabel bienvenida = new JLabel("Bienvenido al sistema", JLabel.CENTER);
    bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 44));
    bienvenida.setForeground(new Color(56, 81, 145));
    bienvenida.setBorder(BorderFactory.createEmptyBorder(100, 20, 10, 20));

    // Añadir la etiqueta en la parte superior y el logo en el centro
    bienvenidaPanel.add(bienvenida, BorderLayout.NORTH);
    bienvenidaPanel.add(logo, BorderLayout.CENTER);

    mainPanel.add(bienvenidaPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();

    }

    // Método para listar los lotes
    private void mostrarListarLotesPanel() {
        mainPanel.removeAll();
        ListarLotesPanel panel = new ListarLotesPanel( () -> mostrarMenus() );
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar ListarPagosClientePanel
    private void mostrarListarPagosCliente() {
        mainPanel.removeAll();
        ListarPagosClientePanel panel = new ListarPagosClientePanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar AnularCreditoPanel
    private void mostrarAnularCredito() {
        mainPanel.removeAll();
        AnularCreditoPanel panel = new AnularCreditoPanel( () -> mostrarMenus() );
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar ListarDeudasPanel
    private void mostrarListarDeudas() {
        mainPanel.removeAll();
        View.Deudas.ListarDeudasPanel panel = new View.Deudas.ListarDeudasPanel( () -> mostrarMenus() );
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar ListarTodosPagosPanel
    private void mostrarListarTodosPagos() {
        mainPanel.removeAll();
        View.Pagos.ListarTodosPagosPanel panel = new View.Pagos.ListarTodosPagosPanel( () -> mostrarMenus() );
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar AnularPagoPanel
    private void mostrarAnularPago() {
        mainPanel.removeAll();
        AnularPagoPanel panel = new AnularPagoPanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar PagarCuotaPanel
    private void mostrarPagarCuota() {
        mainPanel.removeAll();
        PagarCuotaPanel panel = new PagarCuotaPanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }
    // Mostrar DeudaClientePanel
    private void mostrarDeudaClientePanel() {
        mainPanel.removeAll();
        DeudaClientePanel panel = new DeudaClientePanel(0, () -> mostrarMenus());
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar LotePanel
    private void mostrarLotePanel() {
        mainPanel.removeAll();
        View.Lotes.LotePanel panel = new View.Lotes.LotePanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Mostrar alta de cliente
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

    // Mostrar BajaClientePanel
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

    // Mostrar ModificarClientePanel
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

 //  Mostrar BuscarClientesPanel
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

    // Si el usuario pulsa "Ver Deuda" abrimos DeudaClientePanel pasando el ID seleccionado.
    panel.setVerDeudaListener(e -> {
        Integer id = panel.getClienteSeleccionadoId();
        if (id != null) {
            mainPanel.removeAll();
            // Pasamos el id al constructor para que cargarClientes() lo tenga desde el inicio
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

    /*/ Mostrar DeudaClientePanel
    private void mostrarDeudaCliente(int idCliente) {
        mainPanel.removeAll();
        DeudaClientePanel panel = new DeudaClientePanel(idCliente, () -> mostrarBuscarClientesPanel());
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    } /*/

    //  Mostrar ModificarClientePanel con ID específico
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

    //  Mostrar BajaClientePanel con ID específico
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

    // Mostrar ListaClientesPanel
    private void mostrarListaClientes() {
        mainPanel.removeAll();
        ListaClientesPanel panel = new ListaClientesPanel( () -> mostrarMenus() );
        List<Cliente> clientes = ClienteController.listarClientes();
        panel.setClientes(clientes);
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar CambioContraPanel
    private void mostrarCambioContraPanel() {
        mainPanel.removeAll();
        CambioPassPanel panel = new CambioPassPanel(() -> mostrarMenus());
        new PassController(panel, () -> mostrarMenus());
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar AltaCreditoPanel
    private void mostrarAltaCredito() {
        mainPanel.removeAll();
        AltaCreditoPanel panel = new AltaCreditoPanel();
        panel.setCrearListener(() -> {
            // callback vacío
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar ListarCreditosClientePanel
    private void mostrarListarCreditosCliente() {
        mainPanel.removeAll();
        ListarCreditosClientePanel panel = new ListarCreditosClientePanel();
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }

    // Mostrar ListarTodosCreditosPanel
    private void mostrarListarTodosCreditos() {
        mainPanel.removeAll();
        View.Creditos.ListarTodosCreditosPanel panel = new View.Creditos.ListarTodosCreditosPanel( () -> mostrarMenus() );
        mainPanel.add(panel, BorderLayout.CENTER);
        refrescar();
    }   

    // Refrescar el panel principal
    private void refrescar() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}