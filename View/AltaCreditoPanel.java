package View;

import Controller.ClienteController;
import Controller.CreditoController;
import Dao.VariablesDAO;
import Model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel para crear un crédito solicitando:
 *  - Cliente
 *  - Importe (capital)
 *  - Cantidad de cuotas
 *
 * La fecha = hoy.
 * El interés mensual se obtiene de variables.interes_mensual (decimal) y se muestra solo informativo.
 */
public class AltaCreditoPanel extends JPanel {

    private JComboBox<Cliente> clienteCombo;
    private JTextField montoField;
    private JTextField cuotasField;
    private JLabel interesLabel;
    private JButton crearBtn;

    private Double interesMensualDecimal;    // Ej: 0.05
    private Double interesMensualPorcentaje; // Ej: 5.00

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

        addLabel("Importe (capital):", gbc, 0,2);
        montoField = new JTextField();
        gbc.gridx=1; gbc.gridy=2; add(montoField, gbc);

        addLabel("Cantidad de cuotas:", gbc, 0,3);
        cuotasField = new JTextField();
        gbc.gridx=1; gbc.gridy=3; add(cuotasField, gbc);

        addLabel("Interés mensual vigente:", gbc, 0,4);
        interesLabel = new JLabel("-");
        interesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        interesLabel.setForeground(new Color(56,81,145));
        gbc.gridx=1; gbc.gridy=4;
        add(interesLabel, gbc);

        crearBtn = new JButton("Crear Crédito");
        crearBtn.setBackground(new Color(56,81,145));
        crearBtn.setForeground(Color.WHITE);
        crearBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2;
        add(crearBtn, gbc);

        cargarInteres();
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

    private void cargarInteres() {
        interesMensualDecimal = VariablesDAO.getInteresMensual(); // 0.05
        if (interesMensualDecimal != null) {
            interesMensualPorcentaje = interesMensualDecimal * 100.0;
            interesLabel.setText(String.format("%.2f %%", interesMensualPorcentaje));
        } else {
            interesLabel.setText("No definido");
            interesMensualDecimal = 0.0;
            interesMensualPorcentaje = 0.0;
        }
    }

    /**
     * Permite al exterior agregar un callback post-éxito (opcional).
     */
    public void setCrearListener(Runnable onSuccess) {
        crearBtn.addActionListener(e -> crearCredito(onSuccess));
    }

    private void crearCredito(Runnable onSuccess) {
        Cliente c = (Cliente) clienteCombo.getSelectedItem();
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }
        double monto;
        int cuotas;
        try {
            monto = Double.parseDouble(montoField.getText().trim());
            cuotas = Integer.parseInt(cuotasField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Monto o cuotas inválidos.");
            return;
        }
        if (monto <= 0 || cuotas <= 0) {
            JOptionPane.showMessageDialog(this, "Monto y cuotas deben ser > 0.");
            return;
        }

        // Pasamos una tasa dummy (0) porque el controlador siempre toma la de variables
        int idCredito = CreditoController.crearCredito(
                c.getId(),
                monto,
                0.0,          // ignorado internamente
                cuotas,
                LocalDate.now()
        );

        if (idCredito > 0) {
            JOptionPane.showMessageDialog(this,
                    "Crédito creado (ID=" + idCredito + ").\n" +
                    "Interés mensual aplicado: " + String.format("%.2f%%", interesMensualPorcentaje));
            limpiar();
            if (onSuccess != null) onSuccess.run();
        } else {
            JOptionPane.showMessageDialog(this, "Error al crear crédito.");
        }
    }

    private void limpiar() {
        montoField.setText("");
        cuotasField.setText("");
    }
}