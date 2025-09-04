package View;
import javax.swing.*;
import java.awt.*;
import Model.ClienteDAO;

public class BajaClientePanel extends JPanel {
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

        eliminarBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                boolean ok = ClienteDAO.eliminarPorId(id);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
                    idField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente no encontrado o error en la eliminación.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un ID válido.");
            }
        });
    }
}