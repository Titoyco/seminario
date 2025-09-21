package Controller;

import Model.Lote;
import Model.LoteDAO;

import java.time.LocalDate;

public class LoteController {

    public static Lote obtenerLoteActual() {
        return LoteDAO.getLoteActual();
    }

    /**
     * Cierra el lote actual y crea el siguiente automáticamente.
     * Usa la fecha de hoy tanto para el cierre como para la apertura del nuevo (puedes separar si querés).
     */
    public static boolean cerrarYCrearSiguiente(LocalDate fechaCierre, LocalDate fechaAperturaNuevo) {
        return LoteDAO.cerrarLoteActualYCrearNuevo(fechaCierre, fechaAperturaNuevo);
    }
}