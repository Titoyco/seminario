package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BajaClientePanel extends JPanel {


    private JButton eliminarBtn;
    private JTextField idField;

    public BajaClientePanel() {
        // Establece el color de fondo suave y moderno (igual que LoginPanel)
        setBackground(new Color(245, 249, 255));

        // Usa GridBagLayout para mejor control del diseño
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 18, 12, 18); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los campos ocupan el ancho disponible

        JLabel titulo = new JLabel("Baja de Cliente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        JLabel idLabel = new JLabel("ID de Cliente:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        idLabel.setForeground(new Color(56, 81, 145));
        gbc.gridx = 0; gbc.gridy = 1;
        add(idLabel, gbc);

        idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        idField.setForeground(new Color(56, 81, 145));
        idField.setBackground(Color.WHITE);
        idField.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 1; gbc.gridy = 1;
        add(idField, gbc);

        eliminarBtn = new JButton("Eliminar");
        eliminarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        eliminarBtn.setBackground(new Color(56, 81, 145));
        eliminarBtn.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(eliminarBtn, gbc);


        // Espacio extra al final para estética y empujar los campos hacia arriba
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.weighty = 1.0; // Empuja los campos hacia arriba si el panel crece
        add(Box.createVerticalGlue(), gbc);

    }

    // Permite obtener el ID ingresado (usado por el controlador)
    public String getId() {
        return idField.getText().trim();
    }

    // Permite agregar el listener al botón Eliminar (usado por el controlador)
    public void setEliminarListener(ActionListener listener) {
        eliminarBtn.addActionListener(listener);
    }
    // Limpia el campo de ID
    public void limpiarCampos() {
        idField.setText("");
    }
}