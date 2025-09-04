package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import Model.Cliente;
import Controller.ClienteController;

/**
 * Panel para dar de baja un cliente, permite seleccionar de una lista o ingresar el ID manualmente.
 */
public class BajaClientePanel extends JPanel {

    // Botón para eliminar cliente
    private JButton eliminarBtn;
    // Campo para ingresar el ID del cliente manualmente
    private JTextField idField;
    // ComboBox para seleccionar el cliente por nombre
    private JComboBox<Cliente> clientesCombo;

    // Constructor del panel de baja de cliente
    public BajaClientePanel() {
        // Fondo suave y moderno
        setBackground(new Color(245, 249, 255));

        // Usa GridBagLayout para diseño flexible
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 18, 12, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título del panel
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
        cargarClientes(); // Llena el combo con la lista de clientes existente
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
        gbc.gridx = 1; gbc.gridy = 2;
        add(idField, gbc);

        // Sincroniza el campo de ID cuando se selecciona un cliente del combo
        clientesCombo.addActionListener(e -> {
            Cliente seleccionado = (Cliente) clientesCombo.getSelectedItem();
            if (seleccionado != null) {
                idField.setText(String.valueOf(seleccionado.getId()));
            }
        });

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

    // Devuelve el ID ingresado en el campo de texto.
    public String getId() {
        return idField.getText().trim();
    }

    // Permite agregar el listener al botón Eliminar.
    public void setEliminarListener(ActionListener listener) {
        eliminarBtn.addActionListener(listener);
    }

    // Limpia los campos del panel (campo de ID y selección del combo).
    public void limpiarCampos() {
        idField.setText("");
        clientesCombo.setSelectedIndex(-1);
    }
}