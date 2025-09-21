package View;

import javax.swing.*; // Importa las clases de Swing para crear la UI

import Dao.VariablesDAO;

import java.awt.*;    // Importa clases para layouts y colores

public class LoginPanel extends JPanel { // Panel de login de usuario
    private JPasswordField passwordField; // Campo para que el usuario ingrese la contraseña
    private OnLoginSuccessListener listener; // Listener para notificar si el login fue exitoso

    // Interfaz para comunicar el resultado del login
    public interface OnLoginSuccessListener {
        void onLoginSuccess(boolean success); // Método llamado al intentar ingresar
    }

    public LoginPanel(OnLoginSuccessListener listener) { // Constructor del panel
        this.listener = listener; // Guarda el listener recibido

        setBackground(new Color(245, 249, 255)); // Establece color de fondo suave

        setLayout(new GridBagLayout()); // Usa GridBagLayout para ubicar componentes
        GridBagConstraints gbc = new GridBagConstraints(); // Restricciones para el layout
        gbc.insets = new Insets(15, 15, 15, 15); // Espaciado externo entre componentes

        JLabel label = new JLabel("Ingrese la contraseña:"); // Etiqueta de instrucción
        label.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Fuente más grande y negrita
        label.setForeground(new Color(56, 81, 145)); // Color azul suave para la etiqueta

        passwordField = new JPasswordField(15); // Campo de contraseña con 15 columnas
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Fuente moderna y legible
        passwordField.setBackground(new Color(230, 235, 245)); // Fondo claro para el campo
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222), 1, true), // Borde azul claro
            BorderFactory.createEmptyBorder(8, 12, 8, 12) // Espaciado interno
        ));

        JButton loginBtn = new JButton("Ingresar"); // Botón para enviar el login
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Fuente moderna y negrita
        loginBtn.setBackground(new Color(56, 81, 145)); // Fondo azul
        loginBtn.setForeground(Color.WHITE); // Texto blanco
        loginBtn.setFocusPainted(false); // Quita el borde de foco
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Espaciado interno grande
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor tipo mano al pasar
        loginBtn.setToolTipText("Ingresar al sistema"); // Mensaje de ayuda

        // Si tienes un logo, lo muestra arriba (comenta esta línea si no tienes el archivo)
        // JLabel logo = new JLabel(new ImageIcon("img/logo.png"));

        // Ubica la etiqueta en la primera fila/columna
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; // Dos columnas de ancho
        // add(logo, gbc); // Descomenta si tienes logo

        gbc.gridy++; // Segunda fila: la etiqueta
        add(label, gbc);

        gbc.gridy++; // Tercera fila: el campo de contraseña
        add(passwordField, gbc);

        gbc.gridy++; // Cuarta fila: el botón de login
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