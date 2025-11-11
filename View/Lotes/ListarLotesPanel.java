// ListarLotesPanel.java
// Panel para listar lotes.

package View.Lotes;

import Dao.ConexionMySQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.text.MessageFormat;
import java.awt.print.PrinterException;


public class ListarLotesPanel extends JPanel {

    // Componentes del panel
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton imprimirBtn;
    private JButton refrescarBtn;
    private JButton cerrarBtn;
    private final Runnable onClose;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor
    public ListarLotesPanel(Runnable onClose) {
        this.onClose = onClose;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 249, 255));

        // Título
        JLabel titulo = new JLabel("Historial de Lotes", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(56, 81, 145));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(new Object[]{"Nro Lote", "Fecha Apertura", "Fecha Cierre", "Estado"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones inferiores
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 249, 255));
        refrescarBtn = new JButton("Refrescar");
        imprimirBtn = new JButton("Imprimir");
        cerrarBtn = new JButton("Cerrar");

        bottom.add(refrescarBtn);
        bottom.add(imprimirBtn);
        bottom.add(cerrarBtn);
        add(bottom, BorderLayout.SOUTH);

        // Listeners
        refrescarBtn.addActionListener(e -> cargar());
        imprimirBtn.addActionListener(e -> imprimirTabla());
        cerrarBtn.addActionListener(e -> { cerrar(); });
           

        // carga inicial
        cargar();
    }

    // Carga todos los lotes desde la BD
    private void cargar() {
        modelo.setRowCount(0);
        List<LoteRow> rows = new ArrayList<>();
        String sql = "SELECT nro_lote, fecha_apertura, fecha_cierre FROM lotes ORDER BY nro_lote DESC";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int nro = rs.getInt("nro_lote");
                java.sql.Date fa = rs.getDate("fecha_apertura");
                java.sql.Date fc = rs.getDate("fecha_cierre");
                LocalDate fechaA = fa != null ? fa.toLocalDate() : null;
                LocalDate fechaC = fc != null ? fc.toLocalDate() : null;
                String estado = (fechaC == null) ? "ABIERTO" : "CERRADO";
                rows.add(new LoteRow(nro, fechaA, fechaC, estado));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar lotes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (LoteRow r : rows) {
            modelo.addRow(new Object[]{
                    r.nro,
                    r.fechaA != null ? fmt.format(r.fechaA) : "-",
                    r.fechaC != null ? fmt.format(r.fechaC) : "-",
                    r.estado
            });
        }
    }

    // Imprime la tabla
    private void imprimirTabla() {
        MessageFormat header = new MessageFormat("Historial de Lotes");
        MessageFormat footer = new MessageFormat("Página {0}");
        try {
            boolean ok = tabla.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Impresión cancelada.", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage(), "Imprimir", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clase interna para mapear filas
    private static class LoteRow {
        final int nro;
        final LocalDate fechaA;
        final LocalDate fechaC;
        final String estado;
        LoteRow(int nro, LocalDate fechaA, LocalDate fechaC, String estado) {
            this.nro = nro;
            this.fechaA = fechaA;
            this.fechaC = fechaC;
            this.estado = estado;
        }
    }

    public void cerrar() {
        if (onClose != null) onClose.run();
    }
}