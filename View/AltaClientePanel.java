package View;
import javax.swing.*;
import java.awt.*;

public class AltaClientePanel extends JPanel {
    public AltaClientePanel() {
        setLayout(new GridLayout(0, 2, 50, 50));

        add(new JLabel("Nombre:"));
        JTextField nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Documento:"));
        JTextField docField = new JTextField();
        add(docField);

        add(new JLabel("Dirección:"));
        JTextField dirField = new JTextField();
        add(dirField);

        add(new JLabel("Teléfono:"));
        JTextField telField = new JTextField();
        add(telField);

        add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        add(emailField);

        JButton guardarBtn = new JButton("Guardar");
        add(guardarBtn);

        // Espacio vacío para completar el grid
        add(new JLabel());

        guardarBtn.addActionListener(e -> {
            String nombre = nombreField.getText();
            String documento = docField.getText();
            String direccion = dirField.getText();
            String telefono = telField.getText();
            String email = emailField.getText();
            // Aquí iría la lógica para guardar el cliente en la base de datos
            JOptionPane.showMessageDialog(this,
                "Cliente guardado:\nNombre: " + nombre + "\nDocumento: " + documento);
        });
    }
}