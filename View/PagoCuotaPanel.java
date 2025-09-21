package View;

import Controller.CreditoController;
import Model.Cuota;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.awt.event.ActionListener;

/**
 * Panel sencillo para registrar pago de una cuota (full).
 * Requiere que antes se seleccione un crédito y se pase su id.
 * Versión inicial: usuario ingresa ID de cuota manualmente.
 */
public class PagoCuotaPanel extends JPanel {

    private JTextField idCuotaField;
    private JTextField fechaField;
    private JTextField metodoField;
    private JTextField observField;
    private JButton pagarBtn;

    public PagoCuotaPanel() {
        setBackground(new Color(245,249,255));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets=new Insets(10,10,10,10);
        gbc.fill=GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Registrar Pago de Cuota");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(56,81,145));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        add(title, gbc);

        gbc.gridwidth=1;
        addLabel("ID Cuota:", gbc, 0,1);
        idCuotaField = new JTextField();
        gbc.gridx=1; gbc.gridy=1; add(idCuotaField, gbc);

        addLabel("Fecha (YYYY-MM-DD):", gbc, 0,2);
        fechaField = new JTextField(LocalDate.now().toString());
        gbc.gridx=1; gbc.gridy=2; add(fechaField, gbc);

        addLabel("Método:", gbc, 0,3);
        metodoField = new JTextField("efectivo");
        gbc.gridx=1; gbc.gridy=3; add(metodoField, gbc);

        addLabel("Obs:", gbc, 0,4);
        observField = new JTextField();
        gbc.gridx=1; gbc.gridy=4; add(observField, gbc);

        pagarBtn = new JButton("Pagar");
        pagarBtn.setBackground(new Color(56,81,145));
        pagarBtn.setForeground(Color.WHITE);
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2;
        add(pagarBtn, gbc);
    }

    private void addLabel(String t, GridBagConstraints gbc,int x,int y){
        JLabel l=new JLabel(t);
        l.setForeground(new Color(56,81,145));
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx=x; gbc.gridy=y;
        add(l, gbc);
    }

    public void setPagarListener(ActionListener al) {
        pagarBtn.addActionListener(al);
    }

    public String getIdCuota() { return idCuotaField.getText().trim(); }
    public String getFecha() { return fechaField.getText().trim(); }
    public String getMetodo() { return metodoField.getText().trim(); }
    public String getObs() { return observField.getText().trim(); }
}