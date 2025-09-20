package Controller;

import Model.LoteDAO;

import java.time.LocalDate;

/**
 * Controlador para operaciones sobre lotes.
 */
public class LoteController {

    public static boolean abrirNuevoLote(LocalDate fechaApertura) {
        return LoteDAO.abrirNuevoLote(fechaApertura);
    }

    public static boolean cerrarLote(int nroLote, LocalDate fechaCierre) {
        return LoteDAO.cerrarLote(nroLote, fechaCierre);
    }
}