package View.Clientes;

import Controller.ClienteController;
import Dao.DeudaDAO;
import Dao.VariablesDAO;
import Model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DeudaClientePanel extends JPanel {

    private int idCliente;
    private JLabel lblCliente;
    private JLabel lblLoteActual;
    private JLabel lblDeudaTotal;
    private JLabel lblDeudaActual;
    private JLabel lblDeudaMora;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton refrescarBtn;
    private JButton cerrarBtn;

    public DeudaClientePanel(int idCliente, Runnable onClose) {
        this.idCliente = idCliente;
        setLayout(new BorderLayout());
        setBackground(new Color(245,249,255));

        // Encabezado
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(new Color(245,249,255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,12,6,12);
        gbc.anchor = GridBagConstraints.WEST;

        Cliente c = ClienteController.buscarClientePorId(idCliente);
        String nombre = (c != null ? c.getNombre() + " (ID " + idCliente + ")" : "ID " + idCliente);

        lblCliente = new JLabel("Cliente: " + nombre);
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx=0; gbc.gridy=0; header.add(lblCliente, gbc);

        Integer lote = VariablesDAO.getNroLote();
        lblLoteActual = new JLabel("Lote actual: " + (lote != null ? lote : "-"));
        lblLoteActual.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx=1; header.add(lblLoteActual, gbc);

        lblDeudaTotal = etiqueta(header, gbc, 0,1, "Deuda TOTAL: $0.00");
        lblDeudaActual = etiqueta(header, gbc, 1,1, "Deuda ACTUAL: $0.00");
        lblDeudaMora = etiqueta(header, gbc, 2,1, "En MORA: $0.00");

        refrescarBtn = new JButton("Refrescar");
        gbc.gridx=3; gbc.gridy=1; header.add(refrescarBtn, gbc);

        cerrarBtn = new JButton("Cerrar");
        gbc.gridx=4; header.add(cerrarBtn, gbc);

        add(header, BorderLayout.NORTH);

        // Tabla detalle
        modelo = new DefaultTableModel(new Object[]{
                "Crédito","Cuota","Monto","Estado","Lote Origen","Lote Venc.","¿Futura?"
        },0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Listeners
        refrescarBtn.addActionListener(e -> cargarDatos());
        cerrarBtn.addActionListener(e -> {
            if (onClose != null) onClose.run();
        });

        cargarDatos();
    }

    private JLabel etiqueta(JPanel p, GridBagConstraints gbc, int x, int y, String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(new Color(56,81,145));
        gbc.gridx=x; gbc.gridy=y;
        p.add(l, gbc);
        return l;
    }

    private void cargarDatos() {
        // Resumen
        Map<String,Double> resumen = DeudaDAO.resumenDeudaCliente(idCliente);
        lblDeudaTotal.setText(String.format("Deuda TOTAL: $%.2f", resumen.getOrDefault("total",0.0)));
        lblDeudaActual.setText(String.format("Deuda ACTUAL: $%.2f", resumen.getOrDefault("actual",0.0)));
        lblDeudaMora.setText(String.format("En MORA: $%.2f", resumen.getOrDefault("mora",0.0)));

        // Detalle
        modelo.setRowCount(0);
        List<Map<String,Object>> detalle = DeudaDAO.detalleCuotasPendientes(idCliente);
        for (Map<String,Object> m : detalle) {
            modelo.addRow(new Object[]{
                    m.get("id_credito"),
                    m.get("numero"),
                    String.format("%.2f", m.get("monto")),
                    m.get("estado"),
                    m.get("lote_origen"),
                    m.get("lote_venc"),
                    ((boolean)m.get("es_futura")) ? "Sí" : "No"
            });
        }
    }
}