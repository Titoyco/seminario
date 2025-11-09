package View.Creditos;

import Model.Credito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarTodosCreditosPanel extends JPanel {

    // Componentes del panel
    private JTable tabla;
    private DefaultTableModel modelo;

    public ListarTodosCreditosPanel() {
        setLayout(new BorderLayout());
        modelo = new DefaultTableModel(
                new Object[]{"ID","Cliente","Monto","Tasa%","Cuotas","Fecha","Estado","Lote Origen"},0){
            public boolean isCellEditable(int r,int c){ return false; }
        };
        tabla = new JTable(modelo);
        add(new JLabel("Listado de Todos los Créditos", JLabel.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        cargar();
    }

    private void cargar() {
        modelo.setRowCount(0);
        List<Credito> creditos = Dao.CreditoDAO.listarTodos();
        for (Credito cr : creditos) {
            modelo.addRow(new Object[]{
                    cr.getId(), cr.getIdCliente(), cr.getMontoTotal(), cr.getTasaInteres(),
                    cr.getCantidadCuotas(), cr.getFechaOtorgamiento(), cr.getEstado(), cr.getLoteOrigen()
            });
        }
    }
}