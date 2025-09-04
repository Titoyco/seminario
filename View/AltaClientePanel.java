package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * Panel para el alta de clientes, con formato mejorado, comentarios explicativos
 * y ancho mínimo de los campos de entrada establecido en 300px.
 */
public class AltaClientePanel extends JPanel {
    // Campos de texto para el formulario (declarados globalmente para accederlos desde otros métodos)
    private JTextField nombreField;
    private JTextField docField;
    private JTextField dirField;
    private JTextField telField;
    private JTextField emailField;
    private JButton guardarBtn;

    public AltaClientePanel() {
        // Establece el color de fondo suave y moderno (igual que LoginPanel)
        setBackground(new Color(245, 249, 255));

        // Usa GridBagLayout para mejor control del diseño
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 18, 12, 18); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los campos ocupan el ancho disponible

        // Título del panel
        JLabel titleLabel = new JLabel("Alta de Cliente");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        //--------- Nombre ---------
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Fuente moderna y negrita
        nombreLabel.setForeground(new Color(56, 81, 145)); // Azul suave
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(nombreLabel, gbc);

        nombreField = new JTextField();
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nombreField.setBackground(Color.WHITE); // Fondo claro
        nombreField.setPreferredSize(new Dimension(300, 30)); // Establece ancho mínimo de 300px y alto de 30px
        gbc.gridx = 1; gbc.gridy = 1;
        add(nombreField, gbc);

        //--------- Documento ---------
        JLabel docLabel = new JLabel("Documento:");
        docLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        docLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 2;
        add(docLabel, gbc);

        docField = new JTextField();
        docField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        docField.setBackground(Color.WHITE);
        docField.setPreferredSize(new Dimension(300, 30)); // Ancho mínimo de 300px
        gbc.gridx = 1; gbc.gridy = 2;
        add(docField, gbc);

        //--------- Dirección ---------
        JLabel dirLabel = new JLabel("Dirección:");
        dirLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dirLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 3;
        add(dirLabel, gbc);

        dirField = new JTextField();
        dirField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dirField.setBackground(Color.WHITE);
        dirField.setPreferredSize(new Dimension(300, 30)); // Ancho mínimo de 300px
        gbc.gridx = 1; gbc.gridy = 3;
        add(dirField, gbc);

        //--------- Teléfono ---------
        JLabel telLabel = new JLabel("Teléfono:");
        telLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        telLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 4;
        add(telLabel, gbc);

        telField = new JTextField();
        telField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        telField.setBackground(Color.WHITE);
        telField.setPreferredSize(new Dimension(300, 30)); // Ancho mínimo de 300px
        gbc.gridx = 1; gbc.gridy = 4;
        add(telField, gbc);

        //--------- Email ---------
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        emailLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 5;
        add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField.setForeground(new Color(56, 81, 145));
        emailField.setBackground(Color.WHITE);
        emailField.setBorder(BorderFactory.createLineBorder(new Color(56, 81, 145)));
        emailField.setPreferredSize(new Dimension(300, 30)); // Ancho mínimo de 300px
        gbc.gridx = 1; gbc.gridy = 5;
        add(emailField, gbc);

        //--------- Botón Guardar ---------
        guardarBtn = new JButton("Guardar");
        guardarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        guardarBtn.setBackground(new Color(56, 81, 145));
        guardarBtn.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        add(guardarBtn, gbc);




        // Espacio extra al final para estética y empujar los campos hacia arriba
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.weighty = 1.0; // Empuja los campos hacia arriba si el panel crece
        add(Box.createVerticalGlue(), gbc);
    }

    // Métodos para obtener datos de los campos -----------------
    public String getNombre() { return nombreField.getText(); }
    public String getDocumento() { return docField.getText(); }
    public String getDireccion() { return dirField.getText(); }
    public String getTelefono() { return telField.getText(); }
    public String getEmail() { return emailField.getText(); }

    // Limpia todos los campos del formulario
    public void limpiarCampos() {
        nombreField.setText("");
        docField.setText("");
        dirField.setText("");
        telField.setText("");
        emailField.setText("");
    }

    // Permite agregar el listener al botón Guardar (usado por el controlador)
    public void setGuardarListener(ActionListener listener) {
        guardarBtn.addActionListener(listener);
    }
}