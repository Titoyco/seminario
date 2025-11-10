// ListarTodosCreditosPanel.java
// Panel para listar todos los créditos otorgados

package View.Creditos;

import Model.Cliente;
import Model.Credito;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Controller.ClienteController;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.MessageFormat;
import java.awt.print.PrinterException;

public class ListarTodosCreditosPanel extends JPanel {

    // Componentes del panel
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton refrescarBtn;
    private JButton imprimirBtn;

    // Constructor del panel
    public ListarTodosCreditosPanel() {
        setLayout(new BorderLayout());
        modelo = new DefaultTableModel(
                new Object[]{"ID_crédito","Cliente","Monto $","Tasa %","Cuotas","Fecha","Estado","Lote Origen"},0){
            public boolean isCellEditable(int r,int c){ return false; }
        };
        tabla = new JTable(modelo);
        add(new JLabel("Listado de Todos los Créditos", JLabel.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
             // Panel inferior con botón de refrescar
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 249, 255));
        refrescarBtn = new JButton("Refrescar");
        imprimirBtn = new JButton("Imprimir");
        imprimirBtn.addActionListener(e -> imprimirTabla());
        bottom.add(refrescarBtn);
        bottom.add(imprimirBtn);
        add(bottom, BorderLayout.SOUTH);
        // Acción del botón refrescar
        refrescarBtn.addActionListener(e -> cargar());
        // carga inicial
        cargar();
    }

    // Método para cargar los datos en la tabla
    private void cargar() {
        modelo.setRowCount(0);
        // Cargar todos los clientes y crear un mapa id -> nombre para evitar múltiples consultas
        List<Cliente> clientes = ClienteController.listarClientes();
        Map<Integer, String> nombrePorId = new HashMap<>();
        if (clientes != null) {
            for (Cliente c : clientes) {
                nombrePorId.put(c.getId(), c.getNombre());
            }
        }
        // Cargar todos los créditos
        List<Credito> creditos = Dao.CreditoDAO.listarTodos();
        // Agregar cada crédito a la tabla con el nombre del cliente correspondiente
        for (Credito cr : creditos) {
            String nombreCliente = nombrePorId.getOrDefault(cr.getIdCliente(), "ID " + cr.getIdCliente());
            modelo.addRow(new Object[]{
                    cr.getId(),
                    nombreCliente,
                    cr.getMontoTotal(),
                    cr.getTasaInteres(),
                    cr.getCantidadCuotas(),
                    cr.getFechaOtorgamiento(),
                    cr.getEstado(),
                    cr.getLoteOrigen()
            });
        }
    }
    // Método para imprimir la tabla
    private void imprimirTabla() {
        MessageFormat header = new MessageFormat("Listado de Créditos - " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        MessageFormat footer = new MessageFormat("Página {0}");
        try {
            tabla.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}