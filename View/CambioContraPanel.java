package View;
import javax.swing.*;

import Model.VariablesDAO;

import java.awt.*;

public class CambioContraPanel extends JPanel {
    public CambioContraPanel() {
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Nueva contraseña:"));
        JPasswordField nuevaPassField = new JPasswordField();
        add(nuevaPassField);

        add(new JLabel("Confirmar contraseña:"));
        JPasswordField confirmarPassField = new JPasswordField();
        add(confirmarPassField);

        JButton guardarBtn = new JButton("Guardar");
        add(guardarBtn);

        // Espacio vacío
        add(new JLabel());

        guardarBtn.addActionListener(e -> {
            String nuevaPass = new String(nuevaPassField.getPassword());
            String confirmarPass = new String(confirmarPassField.getPassword());

            if (nuevaPass.isEmpty() || confirmarPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No puede dejar campos vacíos");
                return;
            }
            if (!nuevaPass.equals(confirmarPass)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
                return;
            }
            boolean ok = VariablesDAO.updatePassword(nuevaPass);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Contraseña actualizada correctamente");
                nuevaPassField.setText("");
                confirmarPassField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la contraseña");
            }
        });
    }
}