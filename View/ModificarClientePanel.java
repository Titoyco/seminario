package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import Model.Cliente;
import Controller.ClienteController;

/**
 * Panel para modificar los datos de un cliente.
 * Si se pasa un id por parámetro, el combo muestra ese cliente como seleccionado.
 * Si no, muestra el primero de la lista.
 */
public class ModificarClientePanel extends JPanel {
    private JComboBox<Cliente> clientesCombo;
    private JTextField nombreField;
    private JTextField docField;
    private JTextField dirField;
    private JTextField telField;
    private JTextField emailField;
    private JButton guardarBtn;

    // Constructor SIN parámetros: selecciona el primer cliente por defecto
    public ModificarClientePanel() {
        this(-1); // -1 indica que no se pasa ID
    }

    // Constructor CON parámetro: selecciona el cliente indicado por ID
    public ModificarClientePanel(int clienteId) {
        setBackground(new Color(245, 249, 255));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 18, 12, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Modificar Cliente");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Combo para seleccionar cliente
        JLabel clienteLabel = new JLabel("Seleccionar Cliente:");
        clienteLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clienteLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(clienteLabel, gbc);

        clientesCombo = new JComboBox<>();
        cargarClientes(); // Llena el combo

        clientesCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1;
        add(clientesCombo, gbc);

        // Selecciona el cliente correspondiente
        if (clientesCombo.getItemCount() > 0) {
            if (clienteId > 0) {
                seleccionarClientePorId(clienteId);
            } else {
                clientesCombo.setSelectedIndex(0);
            }
        }

        // Cuando cambia el combo, carga los datos en los campos
        clientesCombo.addActionListener(e -> cargarDatosCliente());

        // Nombre
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nombreLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 2;
        add(nombreLabel, gbc);

        nombreField = new JTextField();
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nombreField.setBackground(Color.WHITE);
        nombreField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1; gbc.gridy = 2;
        add(nombreField, gbc);

        // Documento
        JLabel docLabel = new JLabel("Documento:");
        docLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        docLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 3;
        add(docLabel, gbc);

        docField = new JTextField();
        docField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        docField.setBackground(Color.WHITE);
        docField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1; gbc.gridy = 3;
        add(docField, gbc);

        // Dirección
        JLabel dirLabel = new JLabel("Dirección:");
        dirLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dirLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 4;
        add(dirLabel, gbc);

        dirField = new JTextField();
        dirField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dirField.setBackground(Color.WHITE);
        dirField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1; gbc.gridy = 4;
        add(dirField, gbc);

        // Teléfono
        JLabel telLabel = new JLabel("Teléfono:");
        telLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        telLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 5;
        add(telLabel, gbc);

        telField = new JTextField();
        telField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        telField.setBackground(Color.WHITE);
        telField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1; gbc.gridy = 5;
        add(telField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        emailLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 6;
        add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField.setForeground(new Color(56, 81, 145));
        emailField.setBackground(Color.WHITE);
        emailField.setBorder(BorderFactory.createLineBorder(new Color(56, 81, 145)));
        emailField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1; gbc.gridy = 6;
        add(emailField, gbc);

        // Botón Guardar
        guardarBtn = new JButton("Guardar Cambios");
        guardarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        guardarBtn.setBackground(new Color(56, 81, 145));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        add(guardarBtn, gbc);

        // Espaciador estético
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);

        // Carga los datos del cliente actualmente seleccionado
        cargarDatosCliente();
    }

    // Carga la lista de clientes en el JComboBox.
    private void cargarClientes() {
        clientesCombo.removeAllItems();
        for (Cliente c : ClienteController.listarClientes()) {
            clientesCombo.addItem(c);
        }
    }

    // Selecciona en el combo el cliente con el id dado (si existe)
    private void seleccionarClientePorId(int clienteId) {
        for (int i = 0; i < clientesCombo.getItemCount(); i++) {
            Cliente c = clientesCombo.getItemAt(i);
            if (c.getId() == clienteId) {
                clientesCombo.setSelectedIndex(i);
                return;
            }
        }
        // Si no encuentra, selecciona el primero
        if (clientesCombo.getItemCount() > 0) {
            clientesCombo.setSelectedIndex(0);
        }
    }

    // Carga los datos del cliente seleccionado en los campos de texto.
    private void cargarDatosCliente() {
        Cliente seleccionado = (Cliente) clientesCombo.getSelectedItem();
        if (seleccionado != null) {
            nombreField.setText(seleccionado.getNombre());
            docField.setText(seleccionado.getDocumento());
            dirField.setText(seleccionado.getDireccion());
            telField.setText(seleccionado.getTelefono());
            emailField.setText(seleccionado.getEmail());
        }
    }

    // Devuelve el cliente actualizado (incluye el ID del seleccionado)
    public Cliente getClienteActualizado() {
        Cliente seleccionado = (Cliente) clientesCombo.getSelectedItem();
        if (seleccionado == null) return null;
        return new Cliente(
            seleccionado.getId(),
            nombreField.getText(),
            docField.getText(),
            dirField.getText(),
            telField.getText(),
            emailField.getText()
        );
    }

    // Permite agregar el listener al botón Guardar
    public void setGuardarListener(ActionListener listener) {
        guardarBtn.addActionListener(listener);
    }

    // Limpia los campos y recarga la lista de clientes
    public void limpiarCampos() {
        cargarClientes();
        if (clientesCombo.getItemCount() > 0) {
            clientesCombo.setSelectedIndex(0);
            cargarDatosCliente();
        } else {
            nombreField.setText("");
            docField.setText("");
            dirField.setText("");
            telField.setText("");
            emailField.setText("");
        }
    }
}