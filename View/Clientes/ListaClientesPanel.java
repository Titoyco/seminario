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
    private JButton cerrarBtn;
    private Runnable onClose;
    
    // Constructor
    public ListaClientesPanel( Runnable onClose ) {
        this.onClose = onClose;
        setLayout(new BorderLayout());
        // Columnas
        String[] columnas = {"ID", "Nombre", "Documento", "Dirección", "Teléfono", "Email"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        // Añadir componentes
        add(new JLabel("Listado de Clientes", JLabel.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);



        // Botones inferiores
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        imprimirBtn = new JButton("Imprimir");
        cerrarBtn = new JButton("Cerrar");
        bottom.add(imprimirBtn);
        bottom.add(cerrarBtn);
        add(bottom, BorderLayout.SOUTH);
        // Listeners
        imprimirBtn.addActionListener(e -> imprimirTabla());
        cerrarBtn.addActionListener(e -> cerrar());

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

    // Método para cerrar el panel y volver a la ventana principal
    private void cerrar() {
        if (onClose != null) onClose.run();
    }   
    
}