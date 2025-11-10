// ListarTodosCreditosPanel.java
// Panel para listar todos los créditos otorgados


package View.Creditos;

import Model.Cliente;
import Model.Credito;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Controller.ClienteController;
import Dao.CreditoDAO;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.MessageFormat;
import java.awt.print.PrinterException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class ListarTodosCreditosPanel extends JPanel {

    // Componentes del panel
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton cerrarBtn;
    private JButton imprimirBtn;
    private JLabel tituloLbl;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Runnable onClose;

    // Constructor del panel
    public ListarTodosCreditosPanel(Runnable onClose) {
        this.onClose = onClose;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 249, 255));

        // Título
        tituloLbl = new JLabel("Listado de Todos los Créditos", JLabel.CENTER);
        tituloLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloLbl.setForeground(new Color(56,81,145));
        tituloLbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(tituloLbl, BorderLayout.NORTH);

        // Modelo y tabla
        modelo = new DefaultTableModel(
                new Object[]{"ID_crédito","Cliente","Monto $","Tasa %","Cuotas","Fecha","Estado","Lote Origen"},0){
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 249, 255));
        cerrarBtn = new JButton("Cerrar");
        imprimirBtn = new JButton("Imprimir");
        bottom.add(cerrarBtn);
        bottom.add(imprimirBtn);
        add(bottom, BorderLayout.SOUTH);

        // Listeners
        cerrarBtn.addActionListener(e -> cerrar());
        imprimirBtn.addActionListener(e -> imprimirTabla());

        // carga inicial
        cargar();
    }

    // Método para establecer los datos en la tabla
    public void setCreditos(List<Credito> creditos) {
        modelo.setRowCount(0);
        if (creditos == null || creditos.isEmpty()) return;

        // Crear mapa idCliente -> nombre para evitar múltiples consultas
        Map<Integer, String> nombrePorId = new HashMap<>();
        List<Cliente> clientes = ClienteController.listarClientes();
        if (clientes != null) {
            for (Cliente cl : clientes) nombrePorId.put(cl.getId(), cl.getNombre());
        }
        // Llenar la tabla
        for (Credito cr : creditos) {
            String nombreCliente = nombrePorId.getOrDefault(cr.getIdCliente(), "ID " + cr.getIdCliente());
            modelo.addRow(new Object[]{
                    cr.getId(),
                    nombreCliente,
                    String.format("%.2f", cr.getMontoTotal()),
                    String.format("%.2f", cr.getTasaInteres()),
                    cr.getCantidadCuotas(),
                    cr.getFechaOtorgamiento() != null ? fmt.format(cr.getFechaOtorgamiento()) : "-",
                    cr.getEstado(),
                    cr.getLoteOrigen()
            });
        }
    }

    // Método para cargar los datos en la tabla desde el DAO
    private void cargar() {
        try {
            List<Credito> creditos = CreditoDAO.listarTodos();
            setCreditos(creditos);
        } catch (Exception e) {
            modelo.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Error al cargar los créditos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para imprimir la tabla
    private void imprimirTabla() {
        MessageFormat header = new MessageFormat("Listado de Créditos - " + LocalDate.now().format(fmt));
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