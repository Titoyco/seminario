// DeudaClientePanel.java
// Panel para mostrar la deuda de un cliente con un JComboBox para seleccionar cliente.

package View.Deudas;

import Controller.ClienteController;
import Dao.DeudaDAO;
import Dao.VariablesDAO;
import Model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.awt.print.*;

public class DeudaClientePanel extends JPanel {

    // Atributos
    private int idCliente;
    private JLabel lblCliente;
    private JLabel lblLoteActual;
    private JLabel lblDeudaTotal;
    private JLabel lblDeudaActual;
    private JLabel lblDeudaMora;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton refrescarBtn;
    private JButton cerrarBtn;
    private JButton imprimirBtn;
    private JComboBox<Cliente> comboClientes;
    private final Runnable onClose;

    // Constructor
    public DeudaClientePanel(final int idCliente, Runnable onClose) {
        this.idCliente = idCliente;
        this.onClose = onClose;
        setLayout(new BorderLayout());
        setBackground(new Color(245,249,255));

        // Encabezado
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(new Color(245,249,255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,12,6,12);
        gbc.anchor = GridBagConstraints.WEST;

        // selector de cliente
        JLabel lblSeleccionar = new JLabel("Seleccionar Cliente:");
        lblSeleccionar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        header.add(lblSeleccionar, gbc);

        comboClientes = new JComboBox<>();
        comboClientes.setPreferredSize(new Dimension(300, 28));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // espacio para botones en la misma fila si se quisiera
        header.add(comboClientes, gbc);
        gbc.gridwidth = 1; // reset

        //  datos del cliente y lote actual
        lblCliente = new JLabel("Cliente: -");
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        header.add(lblCliente, gbc);
        gbc.gridwidth = 1;

        Integer lote = VariablesDAO.getNroLote();
        lblLoteActual = new JLabel("Lote actual: " + (lote != null ? lote : "-"));
        lblLoteActual.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 2;
        gbc.gridy = 1;
        header.add(lblLoteActual, gbc);

        // resumen de deuda y botones
        lblDeudaTotal = etiqueta(header, gbc, 0,2, "Deuda TOTAL: $0.00");
        lblDeudaActual = etiqueta(header, gbc, 1,2, "Deuda ACTUAL: $0.00");
        lblDeudaMora = etiqueta(header, gbc, 2,2, "En MORA: $0.00");

        add(header, BorderLayout.NORTH);

        // Tabla detalle
        modelo = new DefaultTableModel(new Object[]{
                "Crédito","Cuota","Monto","Estado","Lote Origen","Lote Venc.","¿Futura?"
        },0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);




        comboClientes.addActionListener(e -> {
            Cliente seleccionado = (Cliente) comboClientes.getSelectedItem();
            if (seleccionado != null) {
                this.idCliente = seleccionado.getId();
                lblCliente.setText("Cliente: " + seleccionado.getNombre() + " (ID " + this.idCliente + ")");
                cargarDatos();
            } else {
                // Si no hay selección, limpiar vista
                lblCliente.setText("Cliente: -");
                modelo.setRowCount(0);
                lblDeudaTotal.setText("Deuda TOTAL: $0.00");
                lblDeudaActual.setText("Deuda ACTUAL: $0.00");
                lblDeudaMora.setText("En MORA: $0.00");
            }
        });

                // Botones inferiores
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 249, 255));
        refrescarBtn = new JButton("Refrescar");
        imprimirBtn = new JButton("Imprimir");
        cerrarBtn = new JButton("Cerrar");

        bottom.add(refrescarBtn);
        bottom.add(imprimirBtn);
        bottom.add(cerrarBtn);
        add(bottom, BorderLayout.SOUTH);

        // Listeners
        refrescarBtn.addActionListener(e -> Refrescar());
        imprimirBtn.addActionListener(e -> imprimirPanel());
        cerrarBtn.addActionListener(e -> { cerrar(); });

        // Cargar clientes y datos iniciales
        cargarClientes();
        // Si después de cargar clientes no hay ninguno, mantener vacíos; cargarDatos() se llama desde cargarClientes()
    }

    // Método helper para crear etiquetas del header
    private JLabel etiqueta(JPanel p, GridBagConstraints gbc, int x, int y, String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(new Color(56,81,145));
        gbc.gridx = x;
        gbc.gridy = y;
        p.add(l, gbc);
        return l;
    }

    // Seleccionar cliente por ID (si existe en el combo)
    public void seleccionarClientePorId(int clienteId) {
        if (clienteId <= 0 || comboClientes.getItemCount() == 0) return;
        for (int i = 0; i < comboClientes.getItemCount(); i++) {
            Cliente c = comboClientes.getItemAt(i);
            if (c != null && c.getId() == clienteId) {
                comboClientes.setSelectedIndex(i);
                // actualizar estado interno y etiqueta
                this.idCliente = clienteId;
                lblCliente.setText("Cliente: " + c.getNombre() + " (ID " + clienteId + ")");
                // cargar los datos para ese cliente
                cargarDatos();
                return;
            }
        }
        // si no se encuentra, no hacemos nada
    }

    // Cargar clientes en el combobox (versión robusta que evita disparos no deseados)
    private void cargarClientes() {
        try {
            java.util.List<Cliente> clientes = ClienteController.listarClientes();
            // Guardamos y removemos temporalmente los listeners para evitar disparos
            java.awt.event.ActionListener[] listeners = comboClientes.getActionListeners();
            for (java.awt.event.ActionListener l : listeners) {
                comboClientes.removeActionListener(l);
            }

            comboClientes.removeAllItems();

            if (clientes == null || clientes.isEmpty()) {
                // No hay clientes: limpiar y mostrar datos vacíos
                lblCliente.setText("Cliente: -");
                modelo.setRowCount(0);
                lblDeudaTotal.setText("Deuda TOTAL: $0.00");
                lblDeudaActual.setText("Deuda ACTUAL: $0.00");
                lblDeudaMora.setText("En MORA: $0.00");
                // Restaurar listeners aunque no haya items
                for (java.awt.event.ActionListener l : listeners) comboClientes.addActionListener(l);
                return;
            }

            // Agregar clientes al combo
            for (Cliente cliente : clientes) {
                comboClientes.addItem(cliente);
            }

            // Buscar índice del cliente solicitado (this.idCliente)
            int indexToSelect = 0; // por defecto el primero
            if (this.idCliente > 0) {
                for (int i = 0; i < comboClientes.getItemCount(); i++) {
                    Cliente it = comboClientes.getItemAt(i);
                    if (it != null && it.getId() == this.idCliente) {
                        indexToSelect = i;
                        break;
                    }
                }
            } else {
                // actualizar idCliente con el primer elemento
                Cliente primer = (Cliente) comboClientes.getItemAt(0);
                if (primer != null) this.idCliente = primer.getId();
            }

            // Selección y restauración de listeners en EDT para asegurar correcta aplicación
            final int selIndex = indexToSelect;
            final java.awt.event.ActionListener[] toRestore = listeners;
            SwingUtilities.invokeLater(() -> {
                // Seleccionamos el índice encontrado
                comboClientes.setSelectedIndex(selIndex);

                // Restauramos listeners
                for (java.awt.event.ActionListener l : toRestore) comboClientes.addActionListener(l);

                // Forzamos actualización de etiqueta y carga de datos con el id actualmente seleccionado
                Cliente clienteActual = (Cliente) comboClientes.getSelectedItem();
                if (clienteActual != null) {
                    this.idCliente = clienteActual.getId();
                    lblCliente.setText("Cliente: " + clienteActual.getNombre() + " (ID " + this.idCliente + ")");
                } else {
                    lblCliente.setText("Cliente: ID " + this.idCliente);
                }
                cargarDatos();
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar la lista de clientes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cargar datos de deuda
    private void cargarDatos() {
        if (this.idCliente <= 0) {
            // nothing to load
            modelo.setRowCount(0);
            lblDeudaTotal.setText("Deuda TOTAL: $0.00");
            lblDeudaActual.setText("Deuda ACTUAL: $0.00");
            lblDeudaMora.setText("En MORA: $0.00");
            return;
        }

        // Resumen
        Map<String,Double> resumen = DeudaDAO.resumenDeudaCliente(this.idCliente);
        if (resumen == null) {
            lblDeudaTotal.setText("Deuda TOTAL: $0.00");
            lblDeudaActual.setText("Deuda ACTUAL: $0.00");
            lblDeudaMora.setText("En MORA: $0.00");
        } else {
            lblDeudaTotal.setText(String.format("Deuda TOTAL: $%.2f", resumen.getOrDefault("total",0.0)));
            lblDeudaActual.setText(String.format("Deuda ACTUAL: $%.2f", resumen.getOrDefault("actual",0.0)));
            lblDeudaMora.setText(String.format("En MORA: $%.2f", resumen.getOrDefault("mora",0.0)));
        }

        // Detalle
        modelo.setRowCount(0);
        java.util.List<Map<String,Object>> detalle = DeudaDAO.detalleCuotasPendientes(this.idCliente);
        if (detalle != null) {
            Integer loteActual = VariablesDAO.getNroLote();
            for (Map<String,Object> m : detalle) {
                Object esFutura = m.get("es_futura");
                boolean futura = false;
                if (esFutura instanceof Boolean) futura = (Boolean) esFutura;
                else if (esFutura instanceof Number) futura = ((Number) esFutura).intValue() != 0;
                modelo.addRow(new Object[]{
                        m.get("id_credito"),
                        m.get("numero"),
                        String.format("%.2f", m.get("monto")),
                        m.get("estado"),
                        m.get("lote_origen"),
                        m.get("lote_venc"),
                        futura ? "Sí" : "No"
                });
            }
        }
    }

    // Imprime todo el panel (cabecera + tabla), escalando al ancho de la página y paginando verticalmente si hace falta.
    private void imprimirPanel() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName("Deuda por Cliente - " + lblCliente.getText());

        pj.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                Graphics2D g2 = (Graphics2D) graphics;
                // Mover origen a área imprimible
                g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                double pageWidth = pageFormat.getImageableWidth();
                double pageHeight = pageFormat.getImageableHeight();

                double panelWidth = DeudaClientePanel.this.getWidth();
                double panelHeight = DeudaClientePanel.this.getHeight();

                if (panelWidth == 0 || panelHeight == 0) return NO_SUCH_PAGE;

                // Escalar para ajustar el ancho de la página
                double scale = pageWidth / panelWidth;

                double scaledPanelHeight = panelHeight * scale;
                int totalPages = (int) Math.ceil(scaledPanelHeight / pageHeight);

                if (pageIndex >= totalPages) return NO_SUCH_PAGE;

                // desplazar verticalmente por página y aplicar escala
                g2.translate(0, -pageIndex * pageHeight);
                g2.scale(scale, scale);

                // Imprimir el componente (incluye cabecera y tabla)
                DeudaClientePanel.this.printAll(g2);
                return PAGE_EXISTS;
            }
        });

        boolean doPrint = pj.printDialog();
        if (doPrint) {
            try {
                pj.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void Refrescar() {
        cargarDatos();
    }

    private void cerrar() {
         if (onClose != null) onClose.run();
    }
}