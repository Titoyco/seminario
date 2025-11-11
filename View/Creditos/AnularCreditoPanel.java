// AnularCreditoPanel.java
// Panel para anular créditos manualmente

package View.Creditos;

import Controller.ClienteController;
import Dao.CreditoDAO;
import Model.Cliente;
import Model.Credito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnularCreditoPanel extends JPanel {

    // Componentes de UI
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton anularBtn;
    private JButton refrescarBtn;
    private JButton cerrarBtn;
    private final Runnable onClose;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor
    public AnularCreditoPanel(Runnable onClose) {
        this.onClose = onClose;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 249, 255));

        // Título
        JLabel titulo = new JLabel("Anular Crédito", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(56,81,145));
        titulo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(new Object[]{
                "ID", "Cliente", "Capital", "Tasa%", "Cuotas", "Fecha", "Estado", "Lote Origen"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones inferiores
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 249, 255));
        anularBtn = new JButton("Anular Crédito");
        refrescarBtn = new JButton("Refrescar");
        cerrarBtn = new JButton("Cerrar");
        bottom.add(refrescarBtn);
        bottom.add(anularBtn);
        bottom.add(cerrarBtn);
        add(bottom, BorderLayout.SOUTH);

        // Listeners
        refrescarBtn.addActionListener(e -> cargar());
        anularBtn.addActionListener(e -> anularSeleccionado());
        cerrarBtn.addActionListener(e -> {
            if (onClose != null) onClose.run();
        });

        // carga inicial
        cargar();
    }

    // Carga créditos y los muestra en la tabla
    private void cargar() {
        modelo.setRowCount(0);
        List<Credito> creditos = CreditoDAO.listarTodos();
        if (creditos == null || creditos.isEmpty()) return;

        // Cache de nombres de cliente para evitar muchas consultas
        Map<Integer, String> nombrePorId = new HashMap<>();
        try {
            List<Cliente> clientes = ClienteController.listarClientes();
            if (clientes != null) {
                for (Cliente c : clientes) nombrePorId.put(c.getId(), c.getNombre());
            }
        } catch (Exception ex) {
            // si falla la carga de clientes, seguimos con IDs
            System.out.println("Error cargando clientes para map: " + ex.getMessage());
        }

        for (Credito cr : creditos) {
            String nombre = nombrePorId.getOrDefault(cr.getIdCliente(), "ID " + cr.getIdCliente());
            String fecha = cr.getFechaOtorgamiento() != null ? fmt.format(cr.getFechaOtorgamiento()) : "-";
            modelo.addRow(new Object[]{
                    cr.getId(),
                    nombre,
                    String.format("%.2f", cr.getMontoTotal()),
                    String.format("%.2f", cr.getTasaInteres()),
                    cr.getCantidadCuotas(),
                    fecha,
                    cr.getEstado(),
                    cr.getLoteOrigen()
            });
        }
    }

    // Anula el crédito seleccionado (cambia estado a 'cancelado')
    private void anularSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un crédito para anular.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object idObj = modelo.getValueAt(fila, 0);
        int idCredito;
        try {
            idCredito = Integer.parseInt(idObj.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ID de crédito inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String estadoActual = modelo.getValueAt(fila, 6).toString();
        if ("cancelado".equalsIgnoreCase(estadoActual)) {
            JOptionPane.showMessageDialog(this, "El crédito ya está cancelado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Confirma anular (marcar como 'cancelado') el crédito N° " + idCredito + "?",
                "Confirmar anulación",
                JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        boolean ok = CreditoDAO.actualizarEstado(idCredito, "cancelado");
        if (ok) {
            JOptionPane.showMessageDialog(this, "Crédito anulado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargar();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo anular el crédito. Revise la consola.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}