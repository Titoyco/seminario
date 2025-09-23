package Controller;

import Dao.CreditoDAO;
import Dao.CuotaDAO;
import Dao.PagoDAO;
import Model.Cuota;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controlador para operaciones de pagos que combinan lógica
 * (actualizar estado de crédito cuando corresponde, etc.)
 */
public class PagoController {

    /**
     * Paga una cuota completa:
     *  - Inserta registro en pagos
     *  - Marca cuota como pagada
     *  - Si el crédito queda sin cuotas pendientes => lo marca cancelado
     * Retorna true si todo ok.
     */
    public static boolean pagarCuota(int idCuota, LocalDate fecha, String metodo, String obs) {
        // Primero obtenemos la cuota para saber su monto y crédito
        Cuota cuota = CuotaDAO.buscarPorId(idCuota);
        if (cuota == null || !"pendiente".equalsIgnoreCase(cuota.getEstado())) {
            return false;
        }
        int idCredito = cuota.getIdCredito();
        boolean ok = PagoDAO.registrarPagoCompleto(idCuota, cuota.getMonto(), metodo, obs, fecha);
        if (!ok) return false;

        // Verificar si aún quedan cuotas pendientes en el crédito
        boolean quedanPendientes = CuotaDAO.existenCuotasPendientes(idCredito);
        if (!quedanPendientes) {
            CreditoDAO.actualizarEstado(idCredito, "cancelado");
        }
        return true;
    }

    /**
     * Lista cuotas pendientes (con datos de crédito) para un cliente.
     * Retorna estructuras Map con: idCuota, idCredito, numero, monto.
     */
    public static List<Map<String,Object>> listarCuotasPendientesCliente(int idCliente) {
        return CuotaDAO.listarPendientesPorCliente(idCliente);
    }

    /**
     * Devuelve lista de pagos (cada pago como Map con campos relevantes) de un cliente.
     */
    public static List<Map<String,Object>> listarPagosCliente(int idCliente) {
        return PagoDAO.listarPagosPorCliente(idCliente);
    }

    /**
     * Anula un pago:
     *  - Elimina el registro en pagos
     *  - Devuelve cuota a pendiente
     *  - Si el crédito estaba cancelado y ahora hay al menos una pendiente -> pasa a vigente
     */
    public static boolean anularPago(int idPago) {
        return PagoDAO.anularPago(idPago);
    }
}