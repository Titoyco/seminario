package View;

import Controller.ClienteController;
import Controller.CreditoController;
import Model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class AltaCreditoPanel extends JPanel {

    private JComboBox<Cliente> clienteCombo;
    private JTextField montoField;
    private JTextField tasaField;
    private JTextField cuotasField;
    private JTextField fechaField;
    private JButton crearBtn;

    public AltaCreditoPanel() {
        setBackground(new Color(245, 249, 255));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,15,10,15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Alta de Crédito");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(56,81,145));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        add(title, gbc);
        gbc.gridwidth=1;

        addLabel("Cliente:", gbc, 0,1);
        clienteCombo = new JComboBox<>();
        cargarClientes();
        gbc.gridx=1; gbc.gridy=1;
        add(clienteCombo, gbc);

        addLabel("Monto:", gbc, 0,2);
        montoField = new JTextField();
        gbc.gridx=1; gbc.gridy=2; add(montoField, gbc);

        addLabel("Tasa % (o 0 para usar default):", gbc, 0,3);
        tasaField = new JTextField("0");
        gbc.gridx=1; gbc.gridy=3; add(tasaField, gbc);

        addLabel("Cantidad de cuotas:", gbc, 0,4);
        cuotasField = new JTextField();
        gbc.gridx=1; gbc.gridy=4; add(cuotasField, gbc);

        addLabel("Fecha otorgamiento (YYYY-MM-DD):", gbc, 0,5);
        fechaField = new JTextField(LocalDate.now().toString());
        gbc.gridx=1; gbc.gridy=5; add(fechaField, gbc);

        crearBtn = new JButton("Crear Crédito");
        crearBtn.setBackground(new Color(56,81,145));
        crearBtn.setForeground(Color.WHITE);
        crearBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx=0; gbc.gridy=6; gbc.gridwidth=2;
        add(crearBtn, gbc);
    }

    private void addLabel(String txt, GridBagConstraints gbc, int x, int y) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(new Color(56,81,145));
        gbc.gridx=x; gbc.gridy=y;
        add(l, gbc);
    }

    private void cargarClientes() {
        clienteCombo.removeAllItems();
        List<Cliente> clientes = ClienteController.listarClientes();
        for (Cliente c : clientes) clienteCombo.addItem(c);
    }

    public void setCrearListener(Runnable onSuccess) {
        crearBtn.addActionListener(e -> {
            Cliente c = (Cliente) clienteCombo.getSelectedItem();
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.");
                return;
            }
            double monto;
            double tasa;
            int cuotas;
            LocalDate fecha;
            try {
                monto = Double.parseDouble(montoField.getText().trim());
                tasa = Double.parseDouble(tasaField.getText().trim());
                cuotas = Integer.parseInt(cuotasField.getText().trim());
                fecha = LocalDate.parse(fechaField.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos numéricos o fecha inválidos.");
                return;
            }
            if (monto <= 0 || cuotas <= 0) {
                JOptionPane.showMessageDialog(this, "Monto y cuotas deben ser > 0.");
                return;
            }
            int idCredito = CreditoController.crearCredito(c.getId(), monto, tasa, cuotas, fecha);
            if (idCredito > 0) {
                JOptionPane.showMessageDialog(this, "Crédito creado ID = " + idCredito);
                limpiar();
                if (onSuccess != null) onSuccess.run();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear crédito.");
            }
        });
    }

    private void limpiar() {
        montoField.setText("");
        tasaField.setText("0");
        cuotasField.setText("");
        fechaField.setText(LocalDate.now().toString());
    }
}