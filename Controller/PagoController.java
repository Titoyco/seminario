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
     * MANTIENE compatibilidad: paga y devuelve true/false.
     */
    public static boolean pagarCuota(int idCuota, LocalDate fecha, String metodo, String obs) {
        return pagarCuotaYDevolverId(idCuota, fecha, metodo, obs) != null;
    }

    /**
     * NUEVO: paga y devuelve el ID del pago generado.
     * - Inserta registro en pagos
     * - Marca cuota como pagada
     * - Si el crédito queda sin cuotas pendientes => lo marca cancelado
     * Retorna idPago si todo ok, o null si falla.
     */
    public static Integer pagarCuotaYDevolverId(int idCuota, LocalDate fecha, String metodo, String obs) {
        // Primero obtenemos la cuota para saber su monto y crédito
        Cuota cuota = CuotaDAO.buscarPorId(idCuota);
        if (cuota == null || !"pendiente".equalsIgnoreCase(cuota.getEstado())) {
            return null;
        }
        int idCredito = cuota.getIdCredito();
        int idPago = PagoDAO.registrarPagoCompleto(idCuota, cuota.getMonto(), metodo, obs, fecha);
        if (idPago <= 0) return null;

        // Verificar si aún quedan cuotas pendientes en el crédito
        boolean quedanPendientes = CuotaDAO.existenCuotasPendientes(idCredito);
        if (!quedanPendientes) {
            CreditoDAO.actualizarEstado(idCredito, "cancelado");
        }
        return idPago;
    }

    /**
     * Lista cuotas pendientes (con datos de crédito) para un cliente.
     * Retorna estructuras Map con: id_cuota, id_credito, numero, monto.
     */
    public static List<Map<String,Object>> listarCuotasPendientesCliente(int idCliente) {
        return CuotaDAO.listarPendientesPorCliente(idCliente);
    }

    /**
     * Devuelve lista de pagos (cada pago como Map con campos relevantes) de un cliente.
     */
    public static List<Map<String,Object>> listarPagosCliente(int idCliente) {
        return Dao.PagoDAO.listarPagosPorCliente(idCliente);
    }

    /**
     * Anula un pago:
     *  - Elimina el registro en pagos
     *  - Devuelve cuota a pendiente
     *  - Si el crédito estaba cancelado y ahora hay al menos una pendiente -> pasa a vigente
     */
    public static boolean anularPago(int idPago) {
        return Dao.PagoDAO.anularPago(idPago);
    }
}