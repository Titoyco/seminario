package View.Pagos;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Diálogo de Recibo de Pago imprimible.
 */
public class ReciboPagoDialog extends JDialog {

    private final JTextArea textArea;

    public ReciboPagoDialog(Window owner, Map<String, Object> datos, LocalDate fechaCabecera) {
        super(owner, "Recibo de Pago", ModalityType.APPLICATION_MODAL);
        setSize(600, 600);
        setLocationRelativeTo(owner);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        textArea.setText(construirTexto(datos, fechaCabecera));

        JScrollPane scroll = new JScrollPane(textArea);

        JButton imprimirBtn = new JButton("Imprimir");
        imprimirBtn.addActionListener(e -> imprimir());

        JButton cerrarBtn = new JButton("Cerrar");
        cerrarBtn.addActionListener(e -> dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(imprimirBtn);
        bottom.add(cerrarBtn);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);
    }

    private String construirTexto(Map<String, Object> d, LocalDate fechaCabecera) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String clienteNombre = safeStr(d.get("cliente_nombre"));
        String clienteDni = safeStr(d.get("cliente_dni"));
        int creditoId = safeInt(d.get("credito_id"));
        LocalDate creditoFecha = (LocalDate) d.get("credito_fecha");
        double creditoMonto = safeDouble(d.get("credito_monto"));
        int creditoCantidad = safeInt(d.get("credito_cantidad"));
        int cuotaNumero = safeInt(d.get("cuota_numero"));
        double cuotaMonto = safeDouble(d.get("cuota_monto"));

        StringBuilder sb = new StringBuilder();
        sb.append("==============================================\n");
        sb.append("               RECIBO DE PAGO\n");
        sb.append("==============================================\n");
        sb.append("Fecha: ").append(fechaCabecera != null ? fmt.format(fechaCabecera) : fmt.format(LocalDate.now())).append("\n\n");

        sb.append("Datos del Cliente\n");
        sb.append("  Nombre y Apellido: ").append(clienteNombre).append("\n");
        sb.append("  DNI: ").append(clienteDni).append("\n\n");

        sb.append("Datos del Crédito\n");
        sb.append("  Número: ").append(creditoId).append("\n");
        sb.append("  Fecha Otorgamiento: ").append(creditoFecha != null ? fmt.format(creditoFecha) : "-").append("\n");
        sb.append("  Importe (capital): $").append(String.format("%.2f", creditoMonto)).append("\n");
        sb.append("  Cantidad de cuotas: ").append(creditoCantidad).append("\n\n");

        sb.append("Detalle del Pago\n");
        sb.append("  En el día de la fecha se realiza el pago de \n  la cuota ")
          .append(cuotaNumero)
          .append("  de $ ")
          .append(String.format("%.2f", cuotaMonto))
          .append("\n\n");

        sb.append("----------------------------------------------\n \n \n");
        sb.append("Firma del cajero: ____________________________\n");

        return sb.toString();
    }

    private String safeStr(Object o) { return o == null ? "-" : o.toString(); }
    private int safeInt(Object o) {
        try { return o == null ? 0 : Integer.parseInt(o.toString()); } catch (Exception e) { return 0; }
    }
    private double safeDouble(Object o) {
        try { return o == null ? 0.0 : Double.parseDouble(o.toString()); } catch (Exception e) { return 0.0; }
    }

    private void imprimir() {
        try {
            boolean ok = textArea.print();
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Impresión cancelada.");
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "No se pudo imprimir: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}