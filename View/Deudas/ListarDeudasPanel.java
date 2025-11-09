// ListarDeudasPanel.java
// Panel para mostrar el resumen de deuda por cliente: id_cliente, nombre, deuda_total, deuda_actual, deuda_mora

package View.Deudas;

import Dao.DeudaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ListarDeudasPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton refrescarBtn;
    private JLabel tituloLbl;

    public ListarDeudasPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245,249,255));

        tituloLbl = new JLabel("Listado de Deudas por Cliente", JLabel.CENTER);
        tituloLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloLbl.setForeground(new Color(56,81,145));
        tituloLbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(tituloLbl, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{
                "ID Cliente", "Nombre Cliente", "Deuda TOTAL", "Deuda ACTUAL", "En MORA"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Panel inferior con botón refrescar
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245,249,255));
        refrescarBtn = new JButton("Refrescar");
        bottom.add(refrescarBtn);
        add(bottom, BorderLayout.SOUTH);

        // Listeners
        refrescarBtn.addActionListener(e -> cargar());

        // Carga inicial
        cargar();
    }

    // Carga datos desde DAO
    private void cargar() {
        modelo.setRowCount(0);
        List<Map<String,Object>> rows = DeudaDAO.listarResumenDeudaPorClientes();
        if (rows == null || rows.isEmpty()) {
            // dejar tabla vacía
            return;
        }
        for (Map<String,Object> r : rows) {
            Integer id = r.get("id_cliente") != null ? (Integer) r.get("id_cliente") : null;
            String nombre = r.get("nombre_cliente") != null ? r.get("nombre_cliente").toString() : "-";
            double total = r.get("deuda_total") != null ? ((Number) r.get("deuda_total")).doubleValue() : 0.0;
            double actual = r.get("deuda_actual") != null ? ((Number) r.get("deuda_actual")).doubleValue() : 0.0;
            double mora  = r.get("deuda_mora") != null ? ((Number) r.get("deuda_mora")).doubleValue() : 0.0;

            modelo.addRow(new Object[]{
                    id != null ? id : "-",
                    nombre,
                    String.format("%.2f", total),
                    String.format("%.2f", actual),
                    String.format("%.2f", mora)
            });
        }
    }
}