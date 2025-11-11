// RestaurarPanel.java
// Panel para restaurar (ejecutar) un archivo .sql en la base de datos

package View.Sistema;

import Dao.ConexionMySQL;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.sql.*;


public class RestaurarPanel extends JPanel {

    // Componentes del panel
    private JTextField txtSource;
    private JButton btnChoose;
    private JButton btnRestore;
    private JTextArea logArea;
    private final Runnable onClose;

    // Constructor
    public RestaurarPanel(Runnable onClose) {
        this.onClose = onClose;
        initUI();
    }

    // Inicializa la interfaz gráfica
    private void initUI() {
        setLayout(new BorderLayout(8,8));
        setBorder(new EmptyBorder(10,10,10,10));
        setBackground(new Color(245,249,255));

        JLabel title = new JLabel("Restaurar Base de Datos (ejecutar .sql)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(56,81,145));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        center.add(new JLabel("Archivo fuente (.sql):"), gbc);

        txtSource = new JTextField(System.getProperty("user.home") + File.separator + "backup.sql");
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        center.add(txtSource, gbc);

        btnChoose = new JButton("Elegir...");
        gbc.gridx = 3; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.0;
        center.add(btnChoose, gbc);

        btnRestore = new JButton("Restaurar");
        btnRestore.setBackground(new Color(200,60,60));
        btnRestore.setForeground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.NONE;
        center.add(btnRestore, gbc);

        add(center, BorderLayout.CENTER);

        logArea = new JTextArea(12,80);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        btnChoose.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Seleccionar archivo SQL");
            File f = new File(txtSource.getText());
            if (f.exists()) fc.setSelectedFile(f);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtSource.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });
        // Restaurar acción
        btnRestore.addActionListener(e -> {
            File src = new File(txtSource.getText().trim());
            if (!src.exists() || !src.isFile()) {
                JOptionPane.showMessageDialog(this, "Archivo no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int conf = JOptionPane.showConfirmDialog(this, "Se va a ejecutar el script SQL seleccionado. ¿Continuar?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf != JOptionPane.YES_OPTION) return;
            btnRestore.setEnabled(false);
            log("Iniciando restauración desde: " + src.getAbsolutePath());
            new Thread(() -> {
                try {
                    restaurarDesdeArchivo(src);
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Restauración finalizada."));
                } catch (Exception ex) {
                    log("Error durante restauración: " + ex.getMessage());
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                } finally {
                    SwingUtilities.invokeLater(() -> btnRestore.setEnabled(true));
                }
            }).start();
        });
    }

    // Método para agregar líneas al área de log
    private void log(String s) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(s + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    // Lee el archivo .sql y ejecuta las sentencias en la conexión JDBC.
    private void restaurarDesdeArchivo(File sqlFile) throws Exception {
        try (Connection conn = ConexionMySQL.getConnection()) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement();
                 BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile), "UTF-8"))) {

                // Desactivar checks de FK temporalmente
                log("Desactivando FOREIGN_KEY_CHECKS");
                st.execute("SET FOREIGN_KEY_CHECKS = 0");

                StringBuilder sb = new StringBuilder();
                String line;
                boolean inBlockComment = false;
                int executed = 0;
                while ((line = br.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) continue;

                    // Manejo simple de comentarios de bloque
                    if (trimmed.startsWith("/*")) {
                        inBlockComment = true;
                        if (trimmed.endsWith("*/")) inBlockComment = false;
                        continue;
                    }
                    if (inBlockComment) {
                        if (trimmed.endsWith("*/")) inBlockComment = false;
                        continue;
                    }
                    if (trimmed.startsWith("--") || trimmed.startsWith("#")) continue;

                    sb.append(line).append("\n");

                    // Si la línea contiene ';' terminador (puede haber varias en la línea)
                    while (sb.indexOf(";") >= 0) {
                        int idx = sb.indexOf(";");
                        String stmt = sb.substring(0, idx).trim();
                        // remover la parte ya procesada
                        sb.delete(0, idx + 1);
                        if (!stmt.isEmpty()) {
                            log("Ejecutando: " + (stmt.length() > 120 ? stmt.substring(0, 120) + "..." : stmt));
                            try {
                                st.execute(stmt);
                                executed++;
                                if (executed % 50 == 0) {
                                    conn.commit();
                                    log("Commit intermedio, sentencias ejecutadas: " + executed);
                                }
                            } catch (SQLException sqle) {
                                log("ERROR al ejecutar sentencia: " + sqle.getMessage());
                                // Decidir: continuar o abortar. Aquí abortamos (rollback y lanzar)
                                conn.rollback();
                                throw new SQLException("Error ejecutando script: " + sqle.getMessage(), sqle);
                            }
                        }
                    }
                }

                // Si quedó algo sin ; al final
                String remaining = sb.toString().trim();
                if (!remaining.isEmpty()) {
                    log("Ejecutando última sentencia...");
                    st.execute(remaining);
                    executed++;
                }

                // Reactivar checks de FK y commit final
                st.execute("SET FOREIGN_KEY_CHECKS = 1");
                conn.commit();
                log("Restauración completada. Sentencias ejecutadas: " + executed);
            } catch (Exception ex) {
                try { conn.rollback(); } catch (SQLException ignored) {}
                throw ex;
            } finally {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
            }
        }
    }
}