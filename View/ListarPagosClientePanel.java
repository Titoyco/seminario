package View;

import Controller.ClienteController;
import Controller.PagoController;
import Model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Panel para listar pagos por cliente.
 */
public class ListarPagosClientePanel extends JPanel {

    private JComboBox<Cliente> clienteCombo;
    private JButton cargarBtn;
    private JTable tabla;
    private DefaultTableModel modelo;

    public ListarPagosClientePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245,249,255));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(new Color(245,249,255));
        top.add(new JLabel("Cliente:"));
        clienteCombo = new JComboBox<>();
        cargarClientes();
        top.add(clienteCombo);
        cargarBtn = new JButton("Cargar Pagos");
        top.add(cargarBtn);

        add(top, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{
                "ID Pago","Fecha","Crédito","Cuota","Monto","Método","Obs"
        },0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarBtn.addActionListener(e -> cargarPagos());
    }

    private void cargarClientes() {
        clienteCombo.removeAllItems();
        for (Cliente c : ClienteController.listarClientes()) {
            clienteCombo.addItem(c);
        }
    }

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
}