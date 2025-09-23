package View.Pagos;

import Controller.ClienteController;
import Controller.PagoController;
import Dao.ReciboDAO;
import Model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Nuevo panel de pago de cuota:
 *  - Seleccionar cliente
 *  - Seleccionar cuota pendiente (combo muestra: Credito #X | Cuota #Y | $monto)
 *  - Botón pagar (usa fecha hoy)
 *  - Emite recibo al pagar
 */
public class PagarCuotaPanel extends JPanel {

    private JComboBox<Cliente> clienteCombo;
    private JComboBox<Map<String,Object>> cuotaCombo;
    private JButton cargarCuotasBtn;
    private JButton pagarBtn;
    private JLabel infoInteres; // opcional si se quiere mostrar algo

    public PagarCuotaPanel() {
        setBackground(new Color(245,249,255));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets=new Insets(10,15,10,15);
        gbc.fill=GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Pagar Cuota");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(56,81,145));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=3;
        add(title, gbc);
        gbc.gridwidth=1;

        // Cliente
        addLabel("Cliente:", gbc, 0,1);
        clienteCombo = new JComboBox<>();
        cargarClientes();
        gbc.gridx=1; gbc.gridy=1; gbc.gridwidth=2;
        add(clienteCombo, gbc);
        gbc.gridwidth=1;

        // Botón para cargar cuotas pendientes
        cargarCuotasBtn = new JButton("Cargar cuotas pendientes");
        cargarCuotasBtn.setBackground(new Color(56,81,145));
        cargarCuotasBtn.setForeground(Color.WHITE);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=3;
        add(cargarCuotasBtn, gbc);
        gbc.gridwidth=1;

        // Combo cuotas
        addLabel("Cuota:", gbc, 0,3);
        cuotaCombo = new JComboBox<>();
        cuotaCombo.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                if (value instanceof Map) {
                    Map<?,?> m = (Map<?,?>) value;
                    int idCredito = (int) m.get("id_credito");
                    int numero = (int) m.get("numero");
                    double monto = (double) m.get("monto");
                    setText("Crédito #" + idCredito + " | Cuota #" + numero + " | $" + String.format("%.2f", monto));
                } else {
                    setText("-");
                }
                return this;
            }
        });
        gbc.gridx=1; gbc.gridy=3; gbc.gridwidth=2;
        add(cuotaCombo, gbc);
        gbc.gridwidth=1;

        // Botón pagar
        pagarBtn = new JButton("Pagar");
        pagarBtn.setBackground(new Color(0,140,70));
        pagarBtn.setForeground(Color.WHITE);
        pagarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=3;
        add(pagarBtn, gbc);

        // Listeners
        cargarCuotasBtn.addActionListener(e -> cargarCuotasPendientes());
        pagarBtn.addActionListener(e -> pagarSeleccionada());
    }

    private void addLabel(String txt, GridBagConstraints gbc,int x,int y){
        JLabel l=new JLabel(txt);
        l.setForeground(new Color(56,81,145));
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx=x; gbc.gridy=y;
        add(l, gbc);
    }

    private void cargarClientes() {
        clienteCombo.removeAllItems();
        List<Cliente> clientes = ClienteController.listarClientes();
        for (Cliente c : clientes) clienteCombo.addItem(c);
    }

    private void cargarCuotasPendientes() {
        cuotaCombo.removeAllItems();
        Cliente c = (Cliente) clienteCombo.getSelectedItem();
        if (c == null) return;
        List<Map<String,Object>> cuotas = Controller.PagoController.listarCuotasPendientesCliente(c.getId());
        for (Map<String,Object> row : cuotas) cuotaCombo.addItem(row);
        if (cuotaCombo.getItemCount()==0) {
            JOptionPane.showMessageDialog(this, "No hay cuotas pendientes para este cliente.");
        }
    }

    private void pagarSeleccionada() {
        @SuppressWarnings("unchecked")
        Map<String,Object> sel = (Map<String,Object>) cuotaCombo.getSelectedItem();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una cuota.");
            return;
        }
        int idCuota = (int) sel.get("id_cuota");
        boolean ok = PagoController.pagarCuota(idCuota, LocalDate.now(), "efectivo", "");
        if (!ok) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el pago.");
            return;
        }

        // Mostrar recibo desde este panel
        Map<String, Object> datos = ReciboDAO.datosReciboPorCuota(idCuota);
        if (datos != null) {
            Window owner = SwingUtilities.getWindowAncestor(this);
            ReciboPagoDialog dlg = new ReciboPagoDialog(owner, datos, LocalDate.now());
            dlg.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Pago registrado, pero no se pudo obtener el recibo.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        // Refrescar lista de cuotas
        cargarCuotasPendientes();
    }
}