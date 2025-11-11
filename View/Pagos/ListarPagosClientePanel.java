// ListarPagosClientePanel.java
// Panel para listar pagos por cliente

package View.Pagos;

import Controller.ClienteController;
import Controller.PagoController;
import Model.Cliente;
import java.text.MessageFormat;
import java.awt.print.PrinterException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ListarPagosClientePanel extends JPanel {
    // Componentes del panel
    private JComboBox<Cliente> clienteCombo;
    private JButton cargarBtn;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton imprimirBtn;
    private JButton cerrarBtn;
    private final Runnable onClose;

    // Constructor
    public ListarPagosClientePanel(Runnable onClose) {
        this.onClose = onClose;
        setLayout(new BorderLayout());
        setBackground(new Color(245,249,255));
        // Top panel
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(new Color(245,249,255));
        top.add(new JLabel("Cliente:"));
        clienteCombo = new JComboBox<>();
        cargarClientes();
        top.add(clienteCombo);
        cargarBtn = new JButton("Cargar Pagos");
        top.add(cargarBtn);
        add(top, BorderLayout.NORTH);
        // Tabla de pagos
        modelo = new DefaultTableModel(new Object[]{
                "ID Pago","Fecha","Crédito","Cuota","Monto","Método","Obs"
        },0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        // Listeners
        cargarBtn.addActionListener(e -> cargarPagos());
      
          // Botones inferiores
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 249, 255));
        imprimirBtn = new JButton("Imprimir");
        cerrarBtn = new JButton("Cerrar");

        bottom.add(imprimirBtn);
        bottom.add(cerrarBtn);
        add(bottom, BorderLayout.SOUTH);

        // Listeners

        imprimirBtn.addActionListener(e -> imprimirTabla());
        cerrarBtn.addActionListener(e -> { cerrar(); });

        
    }

    // Cargar clientes en el combo
    private void cargarClientes() {
        clienteCombo.removeAllItems();
        for (Cliente c : ClienteController.listarClientes()) {
            clienteCombo.addItem(c);
        }
    }



    // Cargar pagos del cliente seleccionado
    private void cargarPagos() {
        modelo.setRowCount(0);
        Cliente c = (Cliente) clienteCombo.getSelectedItem();
        if (c == null) return;
        List<Map<String,Object>> pagos = PagoController.listarPagosCliente(c.getId());
        for (Map<String,Object> p : pagos) {
            modelo.addRow(new Object[]{
                    p.get("id_pago"),
                    p.get("fecha_pago"),
                    p.get("id_credito"),
                    p.get("nro_cuota"),
                    p.get("monto_pagado"),
                    p.get("metodo_pago"),
                    p.get("observaciones")
            });
        }
    }

    // Obtener ID del pago seleccionado
    public Integer getPagoSeleccionadoId() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return null;
        Object val = modelo.getValueAt(fila,0);
        try {
            return Integer.parseInt(val.toString());
        } catch (Exception e){
            return null;
        }
    }

    public void cerrar() {
         if (onClose != null) onClose.run();
    }

    // Imprimir tabla (simulación)
    private void imprimirTabla() {
        try {
            boolean completo = tabla.print(JTable.PrintMode.FIT_WIDTH, new MessageFormat("Listado de Pagos"), null);
            if (completo) {
                JOptionPane.showMessageDialog(this, "Impresión completada.", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impresión cancelada.", "Imprimir", JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}