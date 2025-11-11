// BuscarClientesPanel.java
// Panel para buscar clientes por nombre o documento y mostrar resultados en una tabla

package View.Clientes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import Model.Cliente;
import Controller.ClienteController;

// Panel para buscar clientes y mostrar resultados
public class BuscarClientesPanel extends JPanel {

    // Componentes del panel
    private JTextField nombreField;
    private JTextField documentoField;
    private JButton buscarBtn;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JButton modificarBtn;
    private JButton eliminarBtn;
    private JButton verCreditosBtn;
    private JButton verPagosBtn;
    private JButton verDeudaBtn;

    // Constructor del panel
    public BuscarClientesPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 249, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

          // Título
        JLabel titleLabel = new JLabel("Buscar Clientes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Línea explicativa debajo del título
        JLabel subtitle = new JLabel("Escribe el nombre o el DNI del cliente y presiona buscar, luego selecciona un cliente de la lista y presiona el botón de acción que desees");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitle.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        add(subtitle, gbc);

        // Campo Nombre
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nombreLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        add(nombreLabel, gbc);
        nombreField = new JTextField();
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        nombreField.setPreferredSize(new Dimension(200, 28));
        gbc.gridx = 1; gbc.gridy = 2;
        add(nombreField, gbc);

        // Campo Documento
        JLabel documentoLabel = new JLabel("Documento:");
        documentoLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        documentoLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 3;
        add(documentoLabel, gbc);
        documentoField = new JTextField();
        documentoField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        documentoField.setPreferredSize(new Dimension(200, 28));
        gbc.gridx = 1; gbc.gridy = 3;
        add(documentoField, gbc);

        // Botón Buscar
        buscarBtn = new JButton("Buscar");
        buscarBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        buscarBtn.setBackground(new Color(56, 81, 145));
        buscarBtn.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(buscarBtn, gbc);

        // Tabla de resultados
        String[] columnas = {"ID", "Nombre", "Documento", "Dirección", "Teléfono", "Email"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            // Las celdas no son editables
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setFillsViewportHeight(true);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        add(new JScrollPane(tablaResultados), gbc);

        // Panel de botones de acción
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        modificarBtn = new JButton("Modificar");
        eliminarBtn = new JButton("Eliminar");
        verCreditosBtn = new JButton("Ver Creditos");
        verPagosBtn = new JButton("Ver Pagos");
        verDeudaBtn = new JButton("Ver Deuda");

        botonesPanel.add(modificarBtn);
        botonesPanel.add(eliminarBtn);
        botonesPanel.add(verCreditosBtn);
        botonesPanel.add(verPagosBtn);
        botonesPanel.add(verDeudaBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(botonesPanel, gbc);
        // ActionListener para el botón buscar
        buscarBtn.addActionListener(e -> buscarClientes());
        nombreField.addActionListener(e -> buscarClientes());
        documentoField.addActionListener(e -> buscarClientes());

    }

    // Busca clientes usando el controlador y actualiza la tabla
    private void buscarClientes() {
        String nombre = nombreField.getText().trim().toLowerCase();
        String documento = documentoField.getText().trim().toLowerCase();
        // Obtener lista de clientes desde el controlador
        List<Cliente> clientes = ClienteController.listarClientes();
        modeloTabla.setRowCount(0);
        // Filtrar y agregar a la tabla
        for (Cliente c : clientes) {
            boolean coincideNombre = nombre.isEmpty() || c.getNombre().toLowerCase().contains(nombre);
            boolean coincideDoc = documento.isEmpty() || c.getDocumento().toLowerCase().contains(documento);
            if (coincideNombre && coincideDoc) {
                modeloTabla.addRow(new Object[]{
                        c.getId(), c.getNombre(), c.getDocumento(), c.getDireccion(), c.getTelefono(), c.getEmail()
                });
            }
        }
    }

    // Obtiene el ID del cliente seleccionado en la tabla
    public Integer getClienteSeleccionadoId() {
        int fila = tablaResultados.getSelectedRow();
        if (fila >= 0) {
            Object val = modeloTabla.getValueAt(fila, 0);
            if (val instanceof Integer) return (Integer) val;
            try {
                return Integer.parseInt(val.toString());
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    // Permite agregar listeners a los botones de acción
    public void setModificarListener(ActionListener l) { modificarBtn.addActionListener(l); }
    public void setEliminarListener(ActionListener l) { eliminarBtn.addActionListener(l); }
    public void setVerCreditosListener(ActionListener l) { verCreditosBtn.addActionListener(l); }
    public void setVerPagosListener(ActionListener l) { verPagosBtn.addActionListener(l); }
    public void setVerDeudaListener(ActionListener l) { verDeudaBtn.addActionListener(l); }

    // Permite agregar un listener extra al botón Buscar si se desea
    public void setBuscarListener(ActionListener listener) {
        buscarBtn.addActionListener(listener);
    }
}