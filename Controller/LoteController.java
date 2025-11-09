// Controlador para gestionar las operaciones relacionadas con los lotes.

package Controller;

import Model.Lote;

import java.time.LocalDate;

import Dao.LoteDAO;

public class LoteController {

    public static Lote obtenerLoteActual() {
        return LoteDAO.getLoteActual();
    }

    // Cierra el lote actual y crea el siguiente automáticamente.
    // Usa la fecha de hoy tanto para el cierre como para la apertura del nuevo.
    
    public static boolean cerrarYCrearSiguiente(LocalDate fechaCierre, LocalDate fechaAperturaNuevo) {
        return LoteDAO.cerrarLoteActualYCrearNuevo(fechaCierre, fechaAperturaNuevo);
    }
}