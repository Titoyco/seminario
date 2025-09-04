package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel de cambio de contraseña con formato moderno y comentarios explicativos.
 */
public class PassPanel extends JPanel { // Panel para cambiar la contraseña
    // Campo para la nueva contraseña
    private JPasswordField nuevaPassField;
    // Campo para la confirmación de la nueva contraseña
    private JPasswordField confirmarPassField;
    // Botón para guardar la nueva contraseña
    private JButton guardarBtn;

    public PassPanel( Runnable onSuccess) {
        // Establece el color de fondo igual que el LoginPanel
        setBackground(new Color(245, 249, 255));

        // Usa GridBagLayout para ubicar los componentes con mayor control
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los campos llenan el ancho disponible

        // Título del panel
        JLabel titleLabel = new JLabel("Cambio de Contraseña");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Etiqueta para el campo de nueva contraseña
        JLabel nuevaPassLabel = new JLabel("Nueva contraseña:");
        nuevaPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Fuente moderna y negrita
        nuevaPassLabel.setForeground(new Color(56, 81, 145)); // Color azul suave
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(nuevaPassLabel, gbc);

        // Campo de entrada para la nueva contraseña
        nuevaPassField = new JPasswordField(15);
        nuevaPassField.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Fuente moderna
        nuevaPassField.setBackground(new Color(230, 235, 245)); // Fondo claro
        nuevaPassField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 1, true), // Borde azul claro
            BorderFactory.createEmptyBorder(8, 12, 8, 12) // Espaciado interno
        ));
        gbc.gridx = 1; gbc.gridy = 1;
        add(nuevaPassField, gbc);

        // Etiqueta para el campo de confirmación de contraseña
        JLabel confirmarPassLabel = new JLabel("Confirmar contraseña:");
        confirmarPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmarPassLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 2;
        add(confirmarPassLabel, gbc);

        // Campo de entrada para confirmar la nueva contraseña
        confirmarPassField = new JPasswordField(15);
        confirmarPassField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        confirmarPassField.setBackground(new Color(230, 235, 245));
        confirmarPassField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 1; gbc.gridy = 2;
        add(confirmarPassField, gbc);

        // Botón guardar con formato moderno
        guardarBtn = new JButton("Guardar");
        guardarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        guardarBtn.setBackground(new Color(56, 81, 145));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setFocusPainted(false); // Quita borde de foco
        guardarBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Espaciado interno
        guardarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor tipo mano
        guardarBtn.setToolTipText("Guardar nueva contraseña"); // Ayuda al usuario
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(guardarBtn, gbc);

        // Agrega espacio extra al final para empujar los campos hacia arriba si el panel crece
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    // Devuelve el texto ingresado en el campo de nueva contraseña
    public String getNuevaPassword() {
        return new String(nuevaPassField.getPassword());
    }

    // Devuelve el texto ingresado en el campo de confirmación de contraseña
    public String getConfirmarPassword() {
        return new String(confirmarPassField.getPassword());
    }

    // Limpia los campos de contraseña del panel
    public void limpiarCampos() {
        nuevaPassField.setText("");
        confirmarPassField.setText("");
    }

    // Permite al controlador agregar el listener al botón guardar
    public void setGuardarListener(ActionListener listener) {
        guardarBtn.addActionListener(listener);
    }
}