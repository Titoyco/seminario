package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import Model.Cliente;
import Controller.ClienteController;

/**
 * Panel para dar de baja un cliente, permite seleccionar de una lista o ingresar el ID manualmente.
 * Si el constructor recibe un id, el combo muestra ese cliente como seleccionado.
 */
public class BajaClientePanel extends JPanel {

    private JButton eliminarBtn;
    private JTextField idField;
    private JComboBox<Cliente> clientesCombo;

    // Constructor para baja general (combo, primer cliente seleccionado)
    public BajaClientePanel() {
        this(-1); // Llama al constructor principal sin selección específica
    }

    // Constructor para baja de un cliente específico (combo, muestra cliente con el id pasado)
    public BajaClientePanel(int clienteId) {
        setBackground(new Color(245, 249, 255));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 18, 12, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("Baja de Cliente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        //----- ComboBox para seleccionar cliente -----
        gbc.gridwidth = 1;
        JLabel comboLabel = new JLabel("Seleccionar Cliente:");
        comboLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        comboLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 1;
        add(comboLabel, gbc);

        clientesCombo = new JComboBox<>();
        cargarClientes(); // Carga la lista de clientes en el combo
        clientesCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1;
        add(clientesCombo, gbc);

        //----- Campo de ID de Cliente -----
        JLabel idLabel = new JLabel("ID de Cliente:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        idLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 2;
        add(idLabel, gbc);

        idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        idField.setForeground(new Color(56, 81, 145));
        idField.setBackground(Color.WHITE);
        idField.setPreferredSize(new Dimension(300, 30));
        idField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 2;
        add(idField, gbc);

        // Sincroniza el campo de ID cuando se selecciona un cliente del combo
        clientesCombo.addActionListener(e -> {
            Cliente seleccionado = (Cliente) clientesCombo.getSelectedItem();
            if (seleccionado != null) {
                idField.setText(String.valueOf(seleccionado.getId()));
            } else {
                idField.setText("");
            }
        });

        // Selección inicial del combo
        if (clientesCombo.getItemCount() > 0) {
            if (clienteId > 0) {
                seleccionarClientePorId(clienteId);
            } else {
                clientesCombo.setSelectedIndex(0);
            }
            // Esto dispara el listener y setea el campo idField
        }

        //----- Botón Eliminar -----
        eliminarBtn = new JButton("Eliminar");
        eliminarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        eliminarBtn.setBackground(new Color(56, 81, 145));
        eliminarBtn.setForeground(Color.WHITE);
        eliminarBtn.setFocusPainted(false);
        eliminarBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        eliminarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eliminarBtn.setToolTipText("Eliminar cliente seleccionado");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(eliminarBtn, gbc);

        // Espacio extra al final para estética y empujar los campos hacia arriba
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    // Carga la lista de clientes en el JComboBox.
    private void cargarClientes() {
        List<Cliente> clientes = ClienteController.listarClientes();
        clientesCombo.removeAllItems();
        for (Cliente c : clientes) {
            clientesCombo.addItem(c);
        }
    }

    // Devuelve el ID mostrado en el campo de texto.
    public String getId() {
        return idField.getText().trim();
    }

    // Permite agregar el listener al botón Eliminar.
    public void setEliminarListener(ActionListener listener) {
        eliminarBtn.addActionListener(listener);
    }

    // Limpia los campos del panel (campo de ID y combo).
    public void limpiarCampos() {
        if (clientesCombo.getItemCount() > 0) {
            clientesCombo.setSelectedIndex(0);
        }
        idField.setText("");
    }

    /**
     * Selecciona en el combo el cliente con el id dado (si existe).
     * También actualiza el campo de ID.
     */
    public void seleccionarClientePorId(int clienteId) {
        for (int i = 0; i < clientesCombo.getItemCount(); i++) {
            Cliente c = clientesCombo.getItemAt(i);
            if (c.getId() == clienteId) {
                clientesCombo.setSelectedIndex(i);
                idField.setText(String.valueOf(clienteId));
                return;
            }
        }
        // Si no lo encuentra, selecciona el primero
        if (clientesCombo.getItemCount() > 0) {
            clientesCombo.setSelectedIndex(0);
        }
    }
}