// SimpleBackupPanel.java
// Panel para realizar copias de seguridad (dump SQL) de la base de datos MySQL


package View.Sistema;

import Dao.ConexionMySQL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BackUpPanel  extends JPanel {

    // Componentes del panel
    private JTextField txtDest;
    private JButton btnChoose;
    private JButton btnBackup;
    private JTextArea logArea;
    private final Runnable onClose;

    // Constructor
    public BackUpPanel(Runnable onClose) {
        this.onClose = onClose;
        initUI();
    }

    // Inicializa la interfaz gráfica
    private void initUI() {
        setLayout(new BorderLayout(8,8));
        setBorder(new EmptyBorder(10,10,10,10));
        setBackground(new Color(245,249,255));

        JLabel title = new JLabel("Copia de Seguridad - Dump SQL");
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
        center.add(new JLabel("Archivo destino (.sql):"), gbc);

        txtDest = new JTextField(System.getProperty("user.home") + File.separator + "backup.sql");
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        center.add(txtDest, gbc);

        btnChoose = new JButton("Elegir...");
        gbc.gridx = 3; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.0;
        center.add(btnChoose, gbc);

        btnBackup = new JButton("Copia de seguridad");
        btnBackup.setBackground(new Color(56,81,145));
        btnBackup.setForeground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.NONE;
        center.add(btnBackup, gbc);

        add(center, BorderLayout.CENTER);

        logArea = new JTextArea(12, 80);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // Listeners
        btnChoose.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar dump SQL");
            fc.setSelectedFile(new File(txtDest.getText()));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".sql")) {
                    f = new File(f.getAbsolutePath() + ".sql");
                }
                txtDest.setText(f.getAbsolutePath());
            }
        });
        // Realizar backup
        btnBackup.addActionListener(e -> {
            File dest = new File(txtDest.getText().trim());
            if (dest.getParentFile() != null && !dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) {
                    JOptionPane.showMessageDialog(this, "No se pudo crear la carpeta destino.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            btnBackup.setEnabled(false);
            log("Iniciando copia de seguridad en: " + dest.getAbsolutePath());
            new Thread(() -> {
                try {
                    realizarDump(dest);
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Copia de seguridad completada:\n" + dest.getAbsolutePath()));
                } catch (Exception ex) {
                    log("Error durante backup: " + ex.getMessage());
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error durante backup: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                } finally {
                    SwingUtilities.invokeLater(() -> btnBackup.setEnabled(true));
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

    // Realiza el dump: obtiene tablas, SHOW CREATE TABLE, y datos (INSERTs)
    private void realizarDump(File destino) throws Exception {
        try (Connection conn = ConexionMySQL.getConnection();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destino), "UTF-8"))) {

            DatabaseMetaData md = conn.getMetaData();
            String catalog = conn.getCatalog();

            log("Conectado a: " + catalog);
            writer.write("-- Dump generado por SimpleBackupPanel\n");
            writer.write("-- Base de datos: " + (catalog != null ? catalog : "") + "\n\n");
            // Desactivar checks temporales en el dump para restauración más sencilla
            writer.write("SET FOREIGN_KEY_CHECKS = 0;\n\n");

            // Obtener lista de tablas
            List<String> tablas = new ArrayList<>();
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SHOW TABLES")) {
                while (rs.next()) {
                    String t = rs.getString(1);
                    tablas.add(t);
                }
            }

            // Procesar cada tabla
            for (String tabla : tablas) {
                log("Procesando tabla: " + tabla);
                // Obtener CREATE TABLE
                try (PreparedStatement ps = conn.prepareStatement("SHOW CREATE TABLE `" + tabla + "`");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String create = rs.getString(2);
                        writer.write("-- ----------------------------\n");
                        writer.write("-- Estructura para tabla `" + tabla + "`\n");
                        writer.write("-- ----------------------------\n");
                        writer.write("DROP TABLE IF EXISTS `" + tabla + "`;\n");
                        writer.write(create + ";\n\n");
                    }
                }

                // Exportar datos (INSERTs)
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM `" + tabla + "`");
                     ResultSet rs = ps.executeQuery()) {
                    ResultSetMetaData rmd = rs.getMetaData();
                    int cols = rmd.getColumnCount();
                    int rowCount = 0;
                    StringBuilder sb = new StringBuilder();
                    while (rs.next()) {
                        if (rowCount % 100 == 0) {
                            // flush chunk (do nothing here, we accumulate)
                        }
                        sb.setLength(0);
                        sb.append("INSERT INTO `").append(tabla).append("` (");
                        for (int i = 1; i <= cols; i++) {
                            sb.append("`").append(rmd.getColumnName(i)).append("`");
                            if (i < cols) sb.append(", ");
                        }
                        sb.append(") VALUES (");
                        for (int i = 1; i <= cols; i++) {
                            Object val = rs.getObject(i);
                            sb.append(sqlForObject(val));
                            if (i < cols) sb.append(", ");
                        }
                        sb.append(");\n");
                        writer.write(sb.toString());
                        rowCount++;
                        if (rowCount % 500 == 0) {
                            writer.flush();
                            log("  -> " + rowCount + " filas volcadas en " + tabla);
                        }
                    }
                    if (rowCount > 0) writer.write("\n");
                    log("  filas exportadas: " + rowCount);
                }
            }

            // Reactivar checks
            writer.write("SET FOREIGN_KEY_CHECKS = 1;\n");
            writer.flush();
            log("Dump completado, archivo escrito.");
        }
    }

    //  Convierte un objeto Java a su representación SQL adecuada
    private String sqlForObject(Object val) {
        if (val == null) return "NULL";
        if (val instanceof Number || val instanceof Boolean) return val.toString();
        if (val instanceof byte[]) {
            return "0x" + bytesToHex((byte[]) val);
        }
        // Dates and timestamps: write as quoted string
        String s = val.toString();
        s = s.replace("'", "''");
        return "'" + s + "'";
    }

    // Convierte un arreglo de bytes a su representación hexadecimal
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}