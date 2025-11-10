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
    private JButton refrescarBtn;
    private JButton imprimirBtn;
    private JLabel tituloLbl;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor del panel
    public ListarTodosPagosPanel() {
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
        try {
            List<Map<String,Object>> pagos = PagoDAO.listarTodosOrdenadosDesc();
            if (pagos == null || pagos.isEmpty()) return;

            for (Map<String,Object> p : pagos) {
                Object fechaObj = p.get("fecha_pago");
                String fechaStr;
                if (fechaObj instanceof LocalDate) fechaStr = fmt.format((LocalDate) fechaObj);
                else fechaStr = fechaObj != null ? fechaObj.toString() : "-";

                Object idPago = p.get("id_pago");
                Object clienteNombre = p.get("cliente_nombre");
                Object idCredito = p.get("id_credito");
                Object nroCuota = p.get("nro_cuota");
                Object monto = p.get("monto_pagado");
                Object metodo = p.get("metodo_pago");
                Object obs = p.get("observaciones");

                String montoStr = "0.00";
                if (monto instanceof Number) {
                    montoStr = String.format("%.2f", ((Number) monto).doubleValue());
                } else if (monto != null) {
                    try { montoStr = String.format("%.2f", Double.parseDouble(monto.toString())); } catch (Exception ignored) {}
                }

                modelo.addRow(new Object[]{
                        idPago != null ? idPago : "-",
                        fechaStr,
                        clienteNombre != null ? clienteNombre : "-",
                        idCredito != null ? idCredito : "-",
                        nroCuota != null ? nroCuota : "-",
                        montoStr,
                        metodo != null ? metodo : "-",
                        obs != null ? obs : ""
                });
            }
        } catch (Exception ex) {
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



}