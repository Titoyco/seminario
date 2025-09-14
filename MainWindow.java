import javax.swing.*; // Importa clases de Swing para UI

import Controller.*; // Importa los controladores
import Model.*;
import View.*; // Importa todos los paneles de la vista
import java.util.List;
import java.awt.*; // Importa layouts y componentes de AWT

public class MainWindow extends JFrame { // Clase principal de la ventana
    
    private JPanel mainPanel; // Panel principal donde se muestran los diferentes paneles

    public MainWindow() { // Constructor de la ventana principal
        setTitle("Sistema de Préstamos Personales"); // Título de la ventana
        setSize(1200, 800); // Tamaño inicial de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra el programa al cerrar la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setMinimumSize(new Dimension(800, 600)); // Tamaño mínimo de la ventana

        mainPanel = new JPanel(new BorderLayout()); // Crea el panel principal con BorderLayout
        add(mainPanel); // Agrega el panel principal al frame

        mostrarLogin(); // Muestra el panel de login al iniciar
    }

    // Muestra el panel de login con el listener correspondiente
    private void mostrarLogin() {
        mainPanel.removeAll(); // Limpia el panel principal

        // Crea el panel de login y define su acción de éxito/error
        LoginPanel loginPanel = new LoginPanel(success -> {
            if (success) { // Si el login es exitoso
                mostrarMenus(); // Muestra los menús del sistema
            } else { // Si la contraseña es incorrecta
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE); // Mensaje de error
            }
        });

        mainPanel.add(loginPanel, BorderLayout.CENTER); // Agrega el panel de login al centro del panel principal
        mainPanel.revalidate(); // Revalida para actualizar la UI
        mainPanel.repaint(); // Repinta el panel
    }

    // Muestra los menús principales del sistema
    private void mostrarMenus() {
        JMenuBar menuBar = new JMenuBar(); // Barra de menú superior

        // Menú de clientes y su opción de alta/baja
        JMenu clientesMenu = new JMenu("Clientes"); // Crea menú "Clientes"
        JMenuItem altaClienteItem = new JMenuItem("Alta de Cliente"); // Opción para alta de cliente
        altaClienteItem.addActionListener(e -> mostrarAltaCliente()); // Muestra el panel de alta de cliente
        clientesMenu.add(altaClienteItem); // Añade la opción al menú
        
        JMenuItem modificarClienteItem = new JMenuItem("Modificar Cliente"); // Opción para modificar cliente
        modificarClienteItem.addActionListener(e -> mostrarModificarClientePanel()); // Muestra el panel de modificar cliente
        clientesMenu.add(modificarClienteItem); // Añade la opción al menú

        JMenuItem buscarClienteItem = new JMenuItem("Buscar Cliente"); // Opción para buscar cliente
        buscarClienteItem.addActionListener(e -> mostrarBuscarClientesPanel()); // Muestra el panel de buscar cliente
        clientesMenu.add(buscarClienteItem); // Añade la opción al menú

        JMenuItem bajaClienteItem = new JMenuItem("Baja de Cliente"); // Opción para baja de cliente
        bajaClienteItem.addActionListener(e -> mostrarBajaClientePanel()); // Muestra el panel de baja de cliente
        clientesMenu.add(bajaClienteItem); // Añade la opción al menú
        JMenuItem listarClientesItem = new JMenuItem("Listar Clientes");
        listarClientesItem.addActionListener(e -> mostrarListaClientes()); // Acción para listar clientes
        clientesMenu.add(listarClientesItem);
        menuBar.add(clientesMenu); // Añade el menú a la barra


        menuBar.add(clientesMenu); // Añade el menú a la barra

        // Menú de créditos
        JMenu creditosMenu = new JMenu("Créditos"); // Crea menú "Créditos"
        menuBar.add(creditosMenu); // Añade el menú a la barra

        // Menú de reportes
        JMenu reportesMenu = new JMenu("Reportes"); // Crea menú "Reportes"
        menuBar.add(reportesMenu); // Añade el menú a la barra

        // Menú de sistema y su opción de cambio de contraseña
        JMenu sistemaMenu = new JMenu("Sistema"); // Crea menú "Sistema"
        JMenuItem cambioContraItem = new JMenuItem("Cambio de contraseña"); // Opción de cambio de contraseña
        cambioContraItem.addActionListener(e -> mostrarCambioContraPanel()); // Muestra el panel de cambio de contraseña
        sistemaMenu.add(cambioContraItem); // Añade la opción al menú
        // Cerrar sesión
        JMenuItem cerrarSesionItem = new JMenuItem("Cerrar sesión");
        cerrarSesionItem.addActionListener(e -> {
            setJMenuBar(null);   // Quita la barra de menú
            mostrarLogin();      // Vuelve al login
        });
        sistemaMenu.add(cerrarSesionItem);
        menuBar.add(sistemaMenu); // Añade el menú a la barra

        setJMenuBar(menuBar); // Establece la barra de menú en la ventana

        mainPanel.removeAll(); // Limpia el panel principal

        // Crea panel de bienvenida con el mismo color que LoginPanel
        JPanel bienvenidaPanel = new JPanel(new BorderLayout());
        bienvenidaPanel.setBackground(new Color(245, 249, 255)); // Color igual al LoginPanel

        // Mensaje de bienvenida en el centro del panel principal
        JLabel bienvenida = new JLabel("Bienvenido al sistema", JLabel.CENTER); // Etiqueta de bienvenida centrada
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Fuente moderna y grande
        bienvenida.setForeground(new Color(56, 81, 145)); // Color azul suave
        bienvenida.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Espaciado interno
        bienvenidaPanel.add(bienvenida, BorderLayout.CENTER); // Añade la etiqueta al centro

        mainPanel.add(bienvenidaPanel, BorderLayout.CENTER); // Añade el panel de bienvenida al centro

        mainPanel.revalidate(); // Revalida la UI
        mainPanel.repaint(); // Repinta el panel
    }



    // Muestra el panel de alta de cliente usando el controlador
    private void mostrarAltaCliente() {
        mainPanel.removeAll(); // Limpia el panel principal
        AltaClientePanel panel = new AltaClientePanel(); // Crea el panel de alta de cliente

        // Agrega el listener para el botón guardar
        panel.setGuardarListener(e -> {
            String nombre = panel.getNombre();
            String documento = panel.getDocumento();
            String direccion = panel.getDireccion();
            String telefono = panel.getTelefono();
            String email = panel.getEmail();

            boolean ok = Controller.ClienteController.altaCliente(nombre, documento, direccion, telefono, email);

            if (ok) {
                JOptionPane.showMessageDialog(panel, "Cliente guardado correctamente.");
                panel.limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(panel, "Error al guardar cliente.");
            }
        });

        mainPanel.add(panel, BorderLayout.CENTER); // Agrega el panel al centro
        mainPanel.revalidate(); // Revalida la UI
        mainPanel.repaint(); // Repinta el panel
    }

    private void mostrarBajaClientePanel() {
        mainPanel.removeAll(); // Limpia el panel principal
        BajaClientePanel panel = new BajaClientePanel(); // Crea el panel de baja de cliente
        panel.cargarClientes(); // Carga los clientes en el combo
        panel.setEliminarListener(e -> {
            String idStr = panel.getId();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Debe ingresar un ID de cliente.");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID inválido.");
                return;
            }
            boolean ok = Controller.ClienteController.bajaCliente(id);
            if (ok) {
                JOptionPane.showMessageDialog(panel, "Cliente eliminado correctamente.");
                panel.limpiarCampos();
                panel.cargarClientes();
            } else {
                JOptionPane.showMessageDialog(panel, "Error al eliminar cliente.");
            }
        });
        mainPanel.add(panel, BorderLayout.CENTER); // Agrega el panel al centro
        mainPanel.revalidate(); // Revalida la UI
        mainPanel.repaint(); // Repinta el panel
    }
    // Muestra el panel para dar de baja a un cliente existente

    private void mostrarBajaClientePanel(int clienteId) {
        mainPanel.removeAll();
        BajaClientePanel panel = new BajaClientePanel();
        panel.seleccionarClientePorId(clienteId);
        panel.setEliminarListener(e -> {
            String idStr = panel.getId();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Debe ingresar un ID de cliente.");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID inválido.");
                return;
            }
            boolean ok = Controller.ClienteController.bajaCliente(id);
            if (ok) {
                JOptionPane.showMessageDialog(panel, "Cliente eliminado correctamente.");
                panel.limpiarCampos();
                panel.cargarClientes();
            } else {
                JOptionPane.showMessageDialog(panel, "Error al eliminar cliente.");
            }
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Muestra el panel para modificar datos de un cliente existente
    private void mostrarModificarClientePanel() {
        mainPanel.removeAll(); // Limpia el panel principal
        View.ModificarClientePanel panel = new View.ModificarClientePanel();
        LogicaModificar(panel);
    }

    private void mostrarModificarClientePanel(int clienteId) {
        mainPanel.removeAll();
        // Crea un panel de modificación con datos del cliente (deberás crearlo si no existe)
        ModificarClientePanel panel = new ModificarClientePanel(clienteId);
        LogicaModificar(panel);
    }

    private void LogicaModificar(ModificarClientePanel panel) {
        // Listener para el botón guardar
        panel.setGuardarListener(e -> {
            Model.Cliente clienteActualizado = panel.getClienteActualizado();
            if (clienteActualizado == null) {
                JOptionPane.showMessageDialog(panel, "Debe seleccionar un cliente.");
                return;
            }
            boolean ok = Controller.ClienteController.modificarCliente(clienteActualizado);
            if (ok) {
                JOptionPane.showMessageDialog(panel, "Cliente modificado correctamente.");
            } else {
                JOptionPane.showMessageDialog(panel, "Error al modificar cliente.");
            }
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Muestra el panel para buscar clientes por nombre o documento
    private void mostrarBuscarClientesPanel() {
        mainPanel.removeAll(); // Limpia el panel principal

        View.BuscarClientesPanel panel = new View.BuscarClientesPanel();

        // Listener para Modificar
        panel.setModificarListener(e -> {
            Integer id = panel.getClienteSeleccionadoId();
            if (id != null) {
                mostrarModificarClientePanel(id);
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione un cliente para modificar.");
            }
        });

        // Listener para Eliminar
        panel.setEliminarListener(e -> {
            Integer id = panel.getClienteSeleccionadoId();
            if (id != null) {
                mostrarBajaClientePanel(id);
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione un cliente para eliminar.");
            }
        });

        // (Puedes sumar el resto de listeners aquí...)

        mainPanel.add(panel, BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }


    // Muestra el panel de cambio de contraseña
    private void mostrarCambioContraPanel() {
        mainPanel.removeAll(); // Limpia el panel principal
        PassPanel panel = new PassPanel(() -> mostrarMenus()); // Crea el panel de cambio de contraseña
        new PassController(panel, () -> mostrarMenus()); // Conecta el controlador (esto agrega el listener)
        mainPanel.add(panel, BorderLayout.CENTER); // Agrega el panel al centro
        mainPanel.revalidate(); // Revalida la UI
        mainPanel.repaint(); // Repinta el panel
    }

    private void mostrarListaClientes() {
        mainPanel.removeAll();
        ListaClientesPanel panel = new ListaClientesPanel();
        // Aquí debes cargar los clientes:
        List<Cliente> clientes = ClienteController.listarClientes();
        panel.setClientes(clientes);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }   

    public static void main(String[] args) { // Método principal para ejecutar la aplicación
        SwingUtilities.invokeLater(() -> { // Ejecuta en el hilo de Swing
            new MainWindow().setVisible(true); // Crea y muestra la ventana principal
        });
    }
}