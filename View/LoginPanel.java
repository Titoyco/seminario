package View;
import javax.swing.*;

import Model.VariablesDAO;

import java.awt.*;

public class LoginPanel extends JPanel {
    private JPasswordField passwordField;
    private OnLoginSuccessListener listener;

    public interface OnLoginSuccessListener {
        void onLoginSuccess(boolean success);
    }

    public LoginPanel(OnLoginSuccessListener listener) {
        this.listener = listener;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("Ingrese la contraseña:");
        passwordField = new JPasswordField(15);
        JButton loginBtn = new JButton("Ingresar");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(label, gbc);
        gbc.gridy++;
        add(passwordField, gbc);
        gbc.gridy++;
        add(loginBtn, gbc);

        loginBtn.addActionListener(e -> validarLogin());
    }

    private void validarLogin() {
        String passIngresada = new String(passwordField.getPassword());
        String passBD = VariablesDAO.getPassword();
        String masterBD = VariablesDAO.getMasterPassword();

        if (passIngresada.equals(passBD) || passIngresada.equals(masterBD)) {
            listener.onLoginSuccess(true);
        } else {
            listener.onLoginSuccess(false);
        }
    }
}