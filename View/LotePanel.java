// Panel que muestra información del lote actual y permite cerrarlo y crear uno nuevo.

package View;

import Controller.LoteController;
import Model.Lote;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class LotePanel extends JPanel {

    // Componentes del panel
    private JLabel lblNumero;
    private JLabel lblApertura;
    private JLabel lblEstado;
    private JButton cerrarBtn;
    private JButton refrescarBtn;

    // Constructor del panel
    public LotePanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245,249,255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,18,12,18);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Título
        JLabel titulo = new JLabel("Lote Actual");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(56,81,145));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        add(titulo, gbc);
        
        gbc.gridwidth=1;
        // Etiquetas de datos   
        lblNumero = etiquetaDato("Lote Actual N°:", gbc, 0,1);
        lblApertura = etiquetaDato("Abierto el:", gbc, 0,2);
        lblEstado = etiquetaDato("Estado:", gbc, 0,3);
        // Botones
        cerrarBtn = new JButton("Cerrar Lote y Crear Siguiente");
        cerrarBtn.setBackground(new Color(200,60,60));
        cerrarBtn.setForeground(Color.WHITE);
        cerrarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=2;
        add(cerrarBtn, gbc);
        // Botón refrescar
        refrescarBtn = new JButton("Refrescar");
        refrescarBtn.setBackground(new Color(56,81,145));
        refrescarBtn.setForeground(Color.WHITE);
        refrescarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2;
        add(refrescarBtn, gbc);
        // Eventos
        cerrarBtn.addActionListener(e -> cerrarLote());
        refrescarBtn.addActionListener(e -> cargarDatos());
        // Carga datos iniciales
        cargarDatos();
    }

    // Método auxiliar para crear etiquetas de datos
    private JLabel etiquetaDato(String titulo, GridBagConstraints gbc, int x, int y) {
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(56,81,145));
        gbc.gridx=x; gbc.gridy=y;
        add(lblTitulo, gbc);
        // Valor
        JLabel lblValor = new JLabel("-");
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblValor.setForeground(Color.DARK_GRAY);
        gbc.gridx=x+1; gbc.gridy=y;
        add(lblValor, gbc);
        return lblValor;
    }

    // Carga los datos del lote actual
    private void cargarDatos() {
        Lote lote = LoteController.obtenerLoteActual();
        if (lote == null) {
            lblNumero.setText("No definido");
            lblApertura.setText("-");
            lblEstado.setText("Sin datos");
            cerrarBtn.setEnabled(false);
            return;
        }
        lblNumero.setText(String.valueOf(lote.getNroLote()));
        lblApertura.setText(lote.getFechaApertura().toString());
        if (lote.getFechaCierre() == null) {
            lblEstado.setText("ABIERTO");
            cerrarBtn.setEnabled(true);
        } else {
            lblEstado.setText("CERRADO el " + lote.getFechaCierre());
            cerrarBtn.setEnabled(false);
        }
    }

    // Cierra el lote actual y crea el siguiente
    private void cerrarLote() {
        int opt = JOptionPane.showConfirmDialog(this,
                "¿Confirma cerrar el lote actual y crear el siguiente?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (opt != JOptionPane.YES_OPTION) return;
        // Realiza la operación
        boolean ok = LoteController.cerrarYCrearSiguiente(LocalDate.now(), LocalDate.now());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Lote cerrado y nuevo lote creado.");
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo cerrar el lote.");
        }
    }
}