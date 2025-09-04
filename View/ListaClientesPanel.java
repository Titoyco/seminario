package View;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Model.Cliente;

public class ListaClientesPanel extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;

    public ListaClientesPanel() {
        setLayout(new BorderLayout());

        // Columnas
        String[] columnas = {"ID", "Nombre", "Documento", "Dirección", "Teléfono", "Email"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);

        add(new JLabel("Listado de Clientes", JLabel.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
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
}