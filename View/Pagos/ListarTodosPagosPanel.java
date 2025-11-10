// ListarTodosPagosPanel.java
// Panel para listar todos los pagos realizados

package View.Pagos;

import Dao.PagoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.awt.print.PrinterException;

public class ListarTodosPagosPanel extends JPanel {
    // Componentes del panel
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton cerrarBtn;
    private JButton imprimirBtn;
    private JLabel tituloLbl;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Runnable onClose;

    // Constructor del panel
    public ListarTodosPagosPanel(Runnable onClose) {
        this.onClose = onClose;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 249, 255));
        // Título
        tituloLbl = new JLabel("Listado de Todos los Pagos", JLabel.CENTER);
        tituloLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloLbl.setForeground(new Color(56,81,145));
        tituloLbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(tituloLbl, BorderLayout.NORTH);
        // Tabla de pagos
        modelo = new DefaultTableModel(new Object[]{
                "ID Pago", "Fecha", "Cliente", "Crédito", "Cuota", "Monto", "Método", "Observaciones"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        // Configuración de la tabla
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        // Panel inferior con botón de cerrar
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 249, 255));
        cerrarBtn = new JButton("Cerrar");
        imprimirBtn = new JButton("Imprimir");
        imprimirBtn.addActionListener(e -> imprimirTabla());
        bottom.add(cerrarBtn);
        bottom.add(imprimirBtn);
        add(bottom, BorderLayout.SOUTH);
        // Acción del botón cerrar
        cerrarBtn.addActionListener(e -> cerrar());
        // carga inicial
        cargar();
    }

    // Método para establecer los datos en la tabla
      public void setPagos(List<Map<String,Object>> pagos) {
        modelo.setRowCount(0);
        if (pagos == null || pagos.isEmpty()) return;

        for (Map<String,Object> p : pagos) {
            // Fecha
            Object fechaObj = p.get("fecha_pago");
            String fechaStr = "-";
            if (fechaObj instanceof LocalDate) fechaStr = fmt.format((LocalDate) fechaObj);
            else if (fechaObj != null) fechaStr = fechaObj.toString();

            // Monto formateado
            Object montoObj = p.get("monto_pagado");
            String montoStr = "0.00";
            if (montoObj instanceof Number) {
                montoStr = String.format("%.2f", ((Number) montoObj).doubleValue());
            } else if (montoObj != null) {
                try { montoStr = String.format("%.2f", Double.parseDouble(montoObj.toString())); } catch (Exception ignored) {}
            }

            modelo.addRow(new Object[]{
                    p.getOrDefault("id_pago", "-"),
                    fechaStr,
                    p.getOrDefault("cliente_nombre", "-"),
                    p.getOrDefault("id_credito", "-"),
                    p.getOrDefault("nro_cuota", "-"),
                    montoStr,
                    p.getOrDefault("metodo_pago", "-"),
                    p.getOrDefault("observaciones", "")
            });
        }
    }

    // Carga los datos desde el DAO y delega a setPagos
    private void cargar() {
        try {
            List<Map<String,Object>> pagos = PagoDAO.listarTodosOrdenadosDesc();
            setPagos(pagos);
        } catch (Exception ex) {
            modelo.setRowCount(0);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar pagos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Método para imprimir la tabla
     private void imprimirTabla() {
        MessageFormat header = new MessageFormat("Listado de Pagos - " + LocalDate.now().format(fmt));
        MessageFormat footer = new MessageFormat("Página {0}");
        try {
            boolean ok = tabla.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Impresión cancelada.", "Impresión", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al imprimir: " + ex.getMessage(),
                    "Error de impresión", JOptionPane.ERROR_MESSAGE);
        }
    }   


    // Método para cerrar el panel y volver a la ventana principal
    private void cerrar() {
         if (onClose != null) onClose.run();
    }
}