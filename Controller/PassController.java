package Controller;

import View.CambioContraPanel;
import Model.VariablesDAO;

import javax.swing.*;

public class PassController {

    private CambioContraPanel panel;

    public PassController(CambioContraPanel panel, Runnable onSuccess) {
        this.panel = panel;

        // Agrega el listener al botón guardar del panel
        panel.setGuardarListener(e -> guardarPassword(onSuccess));
    }

    // Lógica de validación y actualización de contraseña
    private void guardarPassword(Runnable onSuccess) {
        String nuevaPass = panel.getNuevaPassword();
        String confirmarPass = panel.getConfirmarPassword();

        if (nuevaPass.isEmpty() || confirmarPass.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "No puede dejar campos vacíos");
            return;
        }
        if (!nuevaPass.equals(confirmarPass)) {
            JOptionPane.showMessageDialog(panel, "Las contraseñas no coinciden");
            return;
        }
        boolean ok = VariablesDAO.updatePassword(nuevaPass);
        if (ok) {
            JOptionPane.showMessageDialog(panel, "Contraseña actualizada correctamente");
            if (onSuccess != null) onSuccess.run(); // Vuelve al panel de bienvenida
            panel.limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(panel, "Error al actualizar la contraseña");
        }
    }
}