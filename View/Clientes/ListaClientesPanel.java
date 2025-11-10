// ListaClientesPanel.java
// Panel para mostrar la lista de clientes

package View.Clientes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Model.Cliente;
import java.text.MessageFormat;
import java.awt.print.PrinterException;

public class ListaClientesPanel extends JPanel {
    
    // Componentes del panel
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton imprimirBtn;
    
    // Constructor
    public ListaClientesPanel() {
        setLayout(new BorderLayout());
        // Columnas
        String[] columnas = {"ID", "Nombre", "Documento", "Dirección", "Teléfono", "Email"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        // Añadir componentes
        add(new JLabel("Listado de Clientes", JLabel.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        // Panel inferior con botón de refrescar
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 249, 255));
        imprimirBtn = new JButton("Imprimir");
        imprimirBtn.addActionListener(e -> imprimirTabla());
        bottom.add(imprimirBtn);
        add(bottom, BorderLayout.SOUTH);

    }

    // Método para cargar datos en la tabla
    public void setClientes(List<Cliente> clientes) {
        modelo.setRowCount(0); // Limpiar
        for (Cliente c : clientes) {
            modelo.addRow(new Object[]{
                c.getId(), c.getNombre(), c.getDocumento(), c.getDireccion(), c.getTelefono(), c.getEmail()
            });
        }
    }
    // Método para imprimir la tabla
    private void imprimirTabla() {
        MessageFormat header = new MessageFormat("Listado de Clientes - " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        MessageFormat footer = new MessageFormat("Página {0}");
        try {
            tabla.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
}