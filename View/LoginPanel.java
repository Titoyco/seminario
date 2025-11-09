// Panel de login de usuario

package View;

import javax.swing.*; // Importa las clases de Swing para crear la UI
import Dao.VariablesDAO;
import java.awt.*;    // Importa clases para layouts y colores

public class LoginPanel extends JPanel { // Panel de login de usuario

    // Componentes del panel
    private JPasswordField passwordField; // Campo para que el usuario ingrese la contraseña
    private OnLoginSuccessListener listener; // Listener para notificar si el login fue exitoso

    // Interfaz para comunicar el resultado del login
    public interface OnLoginSuccessListener {
        void onLoginSuccess(boolean success); // Método llamado al intentar ingresar
    }

    // Constructor del panel de login
    public LoginPanel(OnLoginSuccessListener listener) { 
        this.listener = listener; 
        setBackground(new Color(245, 249, 255)); // Establece color de fondo suave
        setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(15, 15, 15, 15);
        JLabel label = new JLabel("Ingrese la contraseña:"); 
        label.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        label.setForeground(new Color(56, 81, 145)); 
        passwordField = new JPasswordField(15); 
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16)); 
        passwordField.setBackground(new Color(230, 235, 245)); 
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 1, true), 
            BorderFactory.createEmptyBorder(8, 12, 8, 12) 
        ));

        JButton loginBtn = new JButton("Ingresar"); // Botón para enviar el login
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        loginBtn.setBackground(new Color(56, 81, 145)); 
        loginBtn.setForeground(Color.WHITE); 
        loginBtn.setFocusPainted(false); 
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        loginBtn.setToolTipText("Ingresar al sistema"); // Mensaje de ayuda

        // Logo
        JLabel logo = new JLabel(new ImageIcon("img/logo.png"));

        // Ubica la etiqueta en la primera fila/columna
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; 
        add(logo, gbc); 
        gbc.gridy++; 
        add(label, gbc);
        gbc.gridy++; 
        add(passwordField, gbc);
        gbc.gridy++; 
        add(loginBtn, gbc);

        // Agrega el evento al botón para validar el login
        loginBtn.addActionListener(e -> validarLogin());
        passwordField.addActionListener(e -> validarLogin()); // También valida al presionar Enter
    }

    // Método privado para validar la contraseña ingresada
    private void validarLogin() {
        String passIngresada = new String(passwordField.getPassword()); // Obtiene la contraseña ingresada
        String passBD = VariablesDAO.getPassword(); // Obtiene la contraseña almacenada en la base de datos
        String masterBD = VariablesDAO.getMasterPassword(); // Obtiene la contraseña maestra
        // Si la contraseña coincide con alguna de las almacenadas...
        if (passIngresada.equals(passBD) || passIngresada.equals(masterBD)) {
            listener.onLoginSuccess(true); // Notifica éxito
        } else {
            listener.onLoginSuccess(false); // Notifica error
        }
    }
}