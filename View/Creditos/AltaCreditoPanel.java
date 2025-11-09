// AltaCreditoPanel.java
// Panel para crear un crédito solicitando cliente, importe y cuotas

package View.Creditos;

import Controller.ClienteController;
import Controller.CreditoController;
import Dao.ReciboDAO;
import Dao.VariablesDAO;
import Dao.CreditoDAO;
import Model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AltaCreditoPanel extends JPanel {

    // Componentes del panel
    private JComboBox<Cliente> clienteCombo;
    private JTextField montoField;
    private JTextField cuotasField;
    private JLabel interesLabel;
    private JButton crearBtn;
    private Double interesMensualDecimal;    // Ej: 0.05
    private Double interesMensualPorcentaje; // Ej: 5.00

    // Constructor
    public AltaCreditoPanel() {
        setBackground(new Color(245, 249, 255));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,15,10,15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel title = new JLabel("Alta de Crédito");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(56,81,145));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        add(title, gbc);
        gbc.gridwidth=1;
        // Cliente
        addLabel("Cliente:", gbc, 0,1);
        clienteCombo = new JComboBox<>();
        cargarClientes();
        gbc.gridx=1; gbc.gridy=1;
        add(clienteCombo, gbc);
        // Monto
        addLabel("Importe (capital):", gbc, 0,2);
        montoField = new JTextField();
        gbc.gridx=1; gbc.gridy=2; add(montoField, gbc);
        // Cuotas
        addLabel("Cantidad de cuotas:", gbc, 0,3);
        cuotasField = new JTextField();
        gbc.gridx=1; gbc.gridy=3; add(cuotasField, gbc);
        // Interés vigente
        addLabel("Interés mensual vigente:", gbc, 0,4);
        interesLabel = new JLabel("-");
        interesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        interesLabel.setForeground(new Color(56,81,145));
        gbc.gridx=1; gbc.gridy=4;
        add(interesLabel, gbc);
        // Botón Crear
        crearBtn = new JButton("Crear Crédito");
        crearBtn.setBackground(new Color(56,81,145));
        crearBtn.setForeground(Color.WHITE);
        crearBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2;
        add(crearBtn, gbc);
        // Cargar interés desde variables
        cargarInteres();
    }

    // Método auxiliar para agregar etiquetas
    private void addLabel(String txt, GridBagConstraints gbc, int x, int y) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(new Color(56,81,145));
        gbc.gridx=x; gbc.gridy=y;
        add(l, gbc);
    }

    // Carga clientes en el combo
    private void cargarClientes() {
        clienteCombo.removeAllItems();
        List<Cliente> clientes = ClienteController.listarClientes();
        for (Cliente c : clientes) clienteCombo.addItem(c);
    }

    // Carga el interés mensual desde la tabla de variables
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

    // Asigna el listener para el botón Crear
    public void setCrearListener(Runnable onSuccess) {
        crearBtn.addActionListener(e -> crearCredito(onSuccess));
    }

    // Crea un nuevo crédito
    private void crearCredito(Runnable onSuccess) {
        Cliente c = (Cliente) clienteCombo.getSelectedItem();
        // debe seleccionarse un cliente
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }
        double monto;
        int cuotas;
        // Validar monto y cuotas
        try {
            monto = Double.parseDouble(montoField.getText().trim());
            cuotas = Integer.parseInt(cuotasField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto o cuotas inválidos.");
            return;
        }
        // Validar valores positivos
        if (monto <= 0 || cuotas <= 0) {
            JOptionPane.showMessageDialog(this, "Monto y cuotas deben ser > 0.");
            return;
        }
        // Validación opcional de rango de cuotas (ajusta si definís otro límite)
        if (cuotas > 12) {
            JOptionPane.showMessageDialog(this, "Cantidad de cuotas excede el límite permitido (máx. 12).");
            return;
        }
        // El controlador ignora la tasa recibida y toma la de variables
        int idCredito = CreditoController.crearCredito(
                c.getId(),
                monto,
                0.0,          // ignorado internamente
                cuotas,
                LocalDate.now()
        );
        // Mostrar resultado
        if (idCredito > 0) {
            JOptionPane.showMessageDialog(this,
                    "Crédito creado (ID=" + idCredito + ").\n" +
                    "Interés mensual aplicado: " + String.format("%.2f%%", interesMensualPorcentaje));
            // Mostrar comprobante de alta de crédito
            try {
                Map<String, Object> cab = ReciboDAO.datosComprobanteCredito(idCredito);
                List<Map<String, Object>> cuotasDet = CreditoDAO.cuotasDeCredito(idCredito);
                if (cab != null && cuotasDet != null && !cuotasDet.isEmpty()) {
                    Window owner = SwingUtilities.getWindowAncestor(this);
                    ComprobanteCreditoDialog dlg = new ComprobanteCreditoDialog(owner, cab, cuotasDet);
                    dlg.setVisible(true);
                } else {
                    System.out.println("No se pudo armar el comprobante (datos incompletos).");
                }
            } catch (Exception ex) {
                System.out.println("Error mostrando comprobante: " + ex.getMessage());
            }
            // Limpiar formulario
            limpiar();
            if (onSuccess != null) onSuccess.run();
        } else {
            JOptionPane.showMessageDialog(this, "Error al crear crédito.");
        }
    }

    // Limpia los campos del formulario
    private void limpiar() {
        montoField.setText("");
        cuotasField.setText("");
    }
}