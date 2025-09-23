package View;

import Controller.ClienteController;
import Controller.PagoController;
import Model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Panel para anular un pago:
 *  - Seleccionar cliente
 *  - Listar pagos
 *  - Seleccionar un pago y anular
 */
public class AnularPagoPanel extends JPanel {

    private JComboBox<Cliente> clienteCombo;
    private JButton cargarBtn;
    private JButton anularBtn;
    private JTable tabla;
    private PagosTableModel modelo;

    public AnularPagoPanel() {
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

        anularBtn = new JButton("Anular Pago Seleccionado");
        top.add(anularBtn);

        add(top, BorderLayout.NORTH);

        modelo = new PagosTableModel();
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarBtn.addActionListener(e -> cargarPagos());
        anularBtn.addActionListener(e -> anularSeleccion());
    }

    private void cargarClientes(){
        clienteCombo.removeAllItems();
        List<Cliente> clientes = ClienteController.listarClientes();
        for (Cliente c : clientes) clienteCombo.addItem(c);
    }

    private void cargarPagos() {
        modelo.clear();
        Cliente c = (Cliente) clienteCombo.getSelectedItem();
        if (c == null) return;
        List<Map<String,Object>> pagos = PagoController.listarPagosCliente(c.getId());
        for (Map<String,Object> p : pagos) modelo.addPago(p);
    }

    private void anularSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pago.");
            return;
        }
        int idPago = (int) modelo.getValueAt(fila,0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Confirma anular el pago " + idPago + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok = PagoController.anularPago(idPago);
        JOptionPane.showMessageDialog(this, ok ? "Pago anulado." : "No se pudo anular.");
        if (ok) cargarPagos();
    }

    // Modelo interno
    static class PagosTableModel extends javax.swing.table.DefaultTableModel {
        public PagosTableModel() {
            super(new Object[]{"ID Pago","Fecha","Crédito","Cuota","Monto","Método","Obs"},0);
        }
        public boolean isCellEditable(int r,int c){return false;}
        public void addPago(Map<String,Object> p) {
            addRow(new Object[]{
                    p.get("id_pago"),
                    p.get("fecha_pago"),
                    p.get("id_credito"),
                    p.get("nro_cuota"),
                    p.get("monto_pagado"),
                    p.get("metodo_pago"),
                    p.get("observaciones")
            });
        }
        public void clear(){ setRowCount(0); }
    }
}