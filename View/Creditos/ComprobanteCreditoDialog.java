package View.Creditos;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ComprobanteCreditoDialog extends JDialog {

    private final JTextArea textArea;

    public ComprobanteCreditoDialog(Window owner,
                                    Map<String,Object> cabecera,
                                    List<Map<String,Object>> cuotas) {
        super(owner, "Comprobante de Crédito", ModalityType.APPLICATION_MODAL);
        setSize(650, 620);
        setLocationRelativeTo(owner);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        textArea.setText(construirTexto(cabecera, cuotas));

        JScrollPane scroll = new JScrollPane(textArea);

        JButton imprimirBtn = new JButton("Imprimir");
        imprimirBtn.addActionListener(e -> imprimir());
        JButton cerrarBtn = new JButton("Cerrar");
        cerrarBtn.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(imprimirBtn);
        botones.add(cerrarBtn);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }

    private String construirTexto(Map<String,Object> c, List<Map<String,Object>> cuotas) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("===============================================\n");
        sb.append("        COMPROBANTE DE ALTA DE CRÉDITO\n");
        sb.append("===============================================\n");
        sb.append("Crédito ID: ").append(c.get("credito_id")).append("\n");
        sb.append("Fecha: ").append(fmt.format((LocalDate)c.get("fecha_otorgado"))).append("\n\n");

        sb.append("Cliente:\n");
        sb.append("  Nombre: ").append(c.get("cliente_nombre")).append("\n");
        sb.append("  Documento: ").append(c.get("cliente_dni")).append("\n\n");

        double capital = (double) c.get("capital");
        double tasa = (double) c.get("tasa");
        int cuotasCant = (int) c.get("cantidad_cuotas");
        double totalConInteres = (double) c.get("total_con_interes");

        sb.append("Datos del crédito:\n");
        sb.append(String.format("  Capital: $%.2f\n", capital));
        sb.append(String.format("  Tasa mensual aplicada: %.2f%%\n", tasa));
        sb.append("  Cantidad de cuotas: ").append(cuotasCant).append("\n");
        sb.append(String.format("  Total con interés: $%.2f\n\n", totalConInteres));

        sb.append("Detalle de cuotas:\n");
        sb.append(String.format("%-8s %-12s\n","Cuota","Monto"));
        sb.append("--------------------------\n");
        double suma = 0;
        for (Map<String,Object> q : cuotas) {
            int nro = (int) q.get("numero");
            double monto = (double) q.get("monto");
            suma += monto;
            sb.append(String.format("%-8d $%-11.2f\n", nro, monto));
        }
        sb.append("--------------------------\n");
        sb.append(String.format("Suma cuotas: $%.2f\n", suma));
        sb.append("\nFirma: __________________________\n");
        return sb.toString();
    }

    private void imprimir() {
        try {
            if (!textArea.print()) {
                JOptionPane.showMessageDialog(this, "Impresión cancelada.");
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}