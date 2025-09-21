package Controller;

import Model.Credito;
import Model.Cuota;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Dao.CreditoDAO;
import Dao.CuotaDAO;
import Dao.VariablesDAO;

/**
 * Lógica de negocio para creación de créditos.
 *
 * Ahora las cuotas incorporan el interés mensual de forma lineal según la fórmula:
 *   total_con_interes = capital * (1 + n * i)
 *   cuota = total_con_interes / n   (ajustando centavos en la última)
 *
 * Donde:
 *   capital = monto ingresado
 *   n = cantidad de cuotas
 *   i = interesMensualDecimal (por ejemplo 0.05 si en variables.interes_mensual = 0.0500)
 *
 * NOTA: En la tabla 'creditos.monto' se sigue guardando el capital original.
 * Las cuotas incluyen capital + interés total distribuido.
 */
public class CreditoController {

    /**
     * @param tasaInteres (IGNORADO si es > 0: siempre se usa variables.interes_mensual)
     */
    public static int crearCredito(int idCliente, double monto, double tasaInteres, int cantCuotas, LocalDate fecha) {
        Integer loteActual = VariablesDAO.getNroLote();
        if (loteActual == null) {
            System.out.println("No se pudo obtener lote actual.");
            return -1;
        }

        // Obtener interés mensual decimal desde variables
        Double interesMensualDecimal = VariablesDAO.getInteresMensual(); // ej: 0.05
        if (interesMensualDecimal == null) {
            System.out.println("Interés mensual no definido en variables.");
            interesMensualDecimal = 0.0; // fallback
        }

        // Guardamos en el crédito la tasa como PORCENTAJE (ej: 5.00)
        double tasaPorcentaje = interesMensualDecimal * 100.0;

        // Generar cuotas con interés incorporado
        List<Cuota> cuotas = generarCuotasConInteres(monto, cantCuotas, interesMensualDecimal);

        Credito credito = new Credito(idCliente, monto, tasaPorcentaje, cantCuotas, fecha);

        int idGenerado = CreditoDAO.crearCreditoConCuotas(credito, cuotas, loteActual);
        if (idGenerado > 0) {
            VariablesDAO.incrementarNroCredito();
        }
        return idGenerado;
    }

    /**
     * Genera la lista de cuotas aplicando la fórmula lineal definida.
     * total = monto * (1 + n * i)
     * Se prorratea en n cuotas; se redondea cada cuota a 2 decimales
     * usando floor en las primeras (para no pasarnos) y la última ajusta la diferencia.
     */
    private static List<Cuota> generarCuotasConInteres(double montoCapital, int cantCuotas, double interesMensualDecimal) {
        List<Cuota> list = new ArrayList<>();
        if (cantCuotas <= 0) return list;

        double totalConInteres = montoCapital * (1.0 + cantCuotas * interesMensualDecimal);

        // Base con floor a 2 decimales
        double base = Math.floor((totalConInteres / cantCuotas) * 100.0) / 100.0;
        double acumulado = 0;

        for (int i = 1; i <= cantCuotas; i++) {
            double cuotaVal;
            if (i < cantCuotas) {
                cuotaVal = base;
                acumulado += base;
            } else {
                // Última cuota: ajusta el redondeo
                cuotaVal = Math.round((totalConInteres - acumulado) * 100.0) / 100.0;
            }
            list.add(new Cuota(0, i, cuotaVal)); // id_credito se setea en la inserción DAO
        }
        return list;
    }

    public static List<Credito> listarPorCliente(int idCliente) {
        return CreditoDAO.listarPorCliente(idCliente);
    }

    public static List<Cuota> listarCuotasCredito(int idCredito) {
        return CuotaDAO.listarPorCredito(idCredito);
    }
}