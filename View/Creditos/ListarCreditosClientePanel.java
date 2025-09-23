package View.Creditos;

import Controller.CreditoController;
import Controller.ClienteController;
import Model.Cliente;
import Model.Credito;
import Model.Cuota;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel que permite seleccionar un cliente y ver sus créditos y las cuotas
 * del crédito seleccionado, mostrando estado y lote_origen reales.
 */
public class ListarCreditosClientePanel extends JPanel {

    private JComboBox<Cliente> clienteCombo;
    private JTable tablaCreditos;
    private JTable tablaCuotas;
    private DefaultTableModel modeloCreditos;
    private DefaultTableModel modeloCuotas;

    public ListarCreditosClientePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245,249,255));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(new Color(245,249,255));
        top.add(new JLabel("Cliente:"));
        clienteCombo = new JComboBox<>();
        cargarClientes();
        top.add(clienteCombo);

        JButton cargarBtn = new JButton("Cargar Créditos");
        top.add(cargarBtn);

        add(top, BorderLayout.NORTH);

        modeloCreditos = new DefaultTableModel(new Object[]{"ID","Monto","Tasa%","Cuotas","Fecha","Estado","Lote Origen"},0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        tablaCreditos = new JTable(modeloCreditos);

        modeloCuotas = new DefaultTableModel(new Object[]{"#","Monto","Estado","Lote Vencimiento"},0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        tablaCuotas = new JTable(modeloCuotas);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tablaCreditos),
                new JScrollPane(tablaCuotas));
        split.setDividerLocation(250);
        add(split, BorderLayout.CENTER);

        cargarBtn.addActionListener(e -> cargarCreditos());

        tablaCreditos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarCuotasSeleccion();
        });
    }

    private void cargarClientes() {
        clienteCombo.removeAllItems();
        for (Cliente c : ClienteController.listarClientes()) {
            clienteCombo.addItem(c);
        }
    }

    private void cargarCreditos() {
        modeloCreditos.setRowCount(0);
        Cliente c = (Cliente) clienteCombo.getSelectedItem();
        if (c == null) return;
        List<Credito> creditos = CreditoController.listarPorCliente(c.getId());
        for (Credito cr : creditos) {
            modeloCreditos.addRow(new Object[]{
                    cr.getId(),
                    cr.getMontoTotal(),
                    cr.getTasaInteres(),
                    cr.getCantidadCuotas(),
                    cr.getFechaOtorgamiento(),
                    cr.getEstado(),
                    cr.getLoteOrigen()
            });
        }
        modeloCuotas.setRowCount(0);
    }

    private void cargarCuotasSeleccion() {
        modeloCuotas.setRowCount(0);
        int fila = tablaCreditos.getSelectedRow();
        if (fila < 0) return;
        int idCredito = Integer.parseInt(tablaCreditos.getValueAt(fila, 0).toString());
        int loteOrigen = Integer.parseInt(tablaCreditos.getValueAt(fila, 6).toString());
        List<Cuota> cuotas = CreditoController.listarCuotasCredito(idCredito);
        for (Cuota cu : cuotas) {
            int loteVenc = loteOrigen + cu.getNumero();
            modeloCuotas.addRow(new Object[]{
                    cu.getNumero(),
                    cu.getMonto(),
                    cu.getEstado(),
                    loteVenc
            });
        }
    }
}