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
 * Panel de pago de cuota:
 *  - Seleccionar cliente
 *  - Cargar cuotas pendientes de créditos vigentes
 *  - Seleccionar cuota pendiente (combo muestra: Crédito #X | Cuota #Y | $monto)
 *  - Elegir método de pago y (opcional) observaciones
 *  - Botón pagar (usa fecha hoy)
 *  - Emite recibo al pagar
 */
public class PagarCuotaPanel extends JPanel {

    private JComboBox<Cliente> clienteCombo;
    private JComboBox<Map<String,Object>> cuotaCombo;
    private JButton cargarCuotasBtn;

    // Nuevos campos
    private JComboBox<String> metodoCombo;
    private JTextField obsField;

    private JButton pagarBtn;

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

        // Método de pago (NUEVO)
        addLabel("Método de pago:", gbc, 0,4);
        metodoCombo = new JComboBox<>(new String[]{"efectivo","banco","transferencia"});
        metodoCombo.setSelectedIndex(0);
        gbc.gridx=1; gbc.gridy=4; gbc.gridwidth=2;
        add(metodoCombo, gbc);
        gbc.gridwidth=1;

        // Observaciones (NUEVO, opcional)
        addLabel("Observaciones:", gbc, 0,5);
        obsField = new JTextField();
        gbc.gridx=1; gbc.gridy=5; gbc.gridwidth=2;
        add(obsField, gbc);
        gbc.gridwidth=1;

        // Botón pagar
        pagarBtn = new JButton("Pagar");
        pagarBtn.setBackground(new Color(0,140,70));
        pagarBtn.setForeground(Color.WHITE);
        pagarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx=0; gbc.gridy=6; gbc.gridwidth=3;
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
        List<Map<String,Object>> cuotas = PagoController.listarCuotasPendientesCliente(c.getId());
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
        String metodo = (String) metodoCombo.getSelectedItem();
        String obs = obsField.getText() != null ? obsField.getText().trim() : "";

        Integer idPago = PagoController.pagarCuotaYDevolverId(idCuota, LocalDate.now(), metodo, obs);
        if (idPago == null) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el pago.");
            return;
        }

        // Mostrar recibo desde este panel con datos del pago recién generado
        Map<String, Object> datos = ReciboDAO.datosReciboPorPago(idPago);
        if (datos != null) {
            Window owner = SwingUtilities.getWindowAncestor(this);
            ReciboPagoDialog dlg = new ReciboPagoDialog(owner, datos, LocalDate.now());
            dlg.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Pago registrado (N° " + idPago + "), pero no se pudo obtener el recibo.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        // Limpiar observaciones y refrescar lista de cuotas
        obsField.setText("");
        cargarCuotasPendientes();
    }
}