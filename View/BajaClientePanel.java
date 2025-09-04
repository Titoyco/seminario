package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BajaClientePanel extends JPanel {


    private JButton eliminarBtn;
    private JTextField idField;

    public BajaClientePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel titulo = new JLabel("Baja de Cliente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        JLabel idLabel = new JLabel("ID de Cliente:");
        gbc.gridx = 0; gbc.gridy = 1;
        add(idLabel, gbc);

        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1; gbc.gridy = 1;
        add(idField, gbc);

        JButton eliminarBtn = new JButton("Eliminar");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(eliminarBtn, gbc);



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