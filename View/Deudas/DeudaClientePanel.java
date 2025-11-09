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
    private JComboBox<Cliente> comboClientes;

    // Constructor
    public DeudaClientePanel(final int idCliente, Runnable onClose) {
        this.idCliente = idCliente;
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

        refrescarBtn = new JButton("Refrescar");
        gbc.gridx = 3;
        gbc.gridy = 2;
        header.add(refrescarBtn, gbc);

        cerrarBtn = new JButton("Cerrar");
        gbc.gridx = 4;
        gbc.gridy = 2;
        header.add(cerrarBtn, gbc);

        add(header, BorderLayout.NORTH);

        // Tabla detalle
        modelo = new DefaultTableModel(new Object[]{
                "Crédito","Cuota","Monto","Estado","Lote Origen","Lote Venc.","¿Futura?"
        },0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Listeners
        refrescarBtn.addActionListener(e -> cargarDatos());
        cerrarBtn.addActionListener(e -> {
            if (onClose != null) onClose.run();
        });

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

    // Cargar clientes en el combobox
    private void cargarClientes() {
        try {
            List<Cliente> clientes = ClienteController.listarClientes();
            comboClientes.removeAllItems();

            if (clientes == null || clientes.isEmpty()) {
                // No hay clientes: limpiar y mostrar datos vacíos
                lblCliente.setText("Cliente: -");
                modelo.setRowCount(0);
                lblDeudaTotal.setText("Deuda TOTAL: $0.00");
                lblDeudaActual.setText("Deuda ACTUAL: $0.00");
                lblDeudaMora.setText("En MORA: $0.00");
                return;
            }

            // Agregar clientes al combo
            for (Cliente cliente : clientes) {
                comboClientes.addItem(cliente);
            }

            // Intentar seleccionar el cliente pasado por constructor si existe,
            // de lo contrario seleccionar el primero de la lista.
            boolean seleccionado = false;
            if (this.idCliente > 0) {
                for (int i = 0; i < comboClientes.getItemCount(); i++) {
                    Cliente it = comboClientes.getItemAt(i);
                    if (it != null && it.getId() == this.idCliente) {
                        comboClientes.setSelectedIndex(i);
                        seleccionado = true;
                        break;
                    }
                }
            }
            if (!seleccionado) {
                comboClientes.setSelectedIndex(0);
                Cliente primer = (Cliente) comboClientes.getSelectedItem();
                if (primer != null) this.idCliente = primer.getId();
            }

            // Actualizar etiqueta de cliente y cargar datos
            Cliente clienteActual = ClienteController.buscarClientePorId(this.idCliente);
            if (clienteActual != null) {
                lblCliente.setText("Cliente: " + clienteActual.getNombre() + " (ID " + this.idCliente + ")");
            } else {
                lblCliente.setText("Cliente: ID " + this.idCliente);
            }

            cargarDatos();

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
        List<Map<String,Object>> detalle = DeudaDAO.detalleCuotasPendientes(this.idCliente);
        if (detalle != null) {
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
}