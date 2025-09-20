package Controller;

import Model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lógica de negocio para creación de créditos y generación de cuotas.
 * Versión inicial: cuotas iguales (ajuste redondeo en la última).
 * TODO futuro: incorporar interés en cálculo real (francés o simple).
 */
public class CreditoController {

    public static int crearCredito(int idCliente, double monto, double tasaInteres, int cantidadCuotas, LocalDate fechaOtorgamiento) {
        Integer loteActual = VariablesDAO.getNroLote();
        if (loteActual == null) {
            System.out.println("No se pudo obtener nro_lote actual.");
            return -1;
        }

        Credito credito = new Credito(idCliente, monto, tasaInteres, cantidadCuotas, fechaOtorgamiento);
        int idGenerado = CreditoDAO.insertar(credito, loteActual);
        if (idGenerado <= 0) return -1;

        // Generar cuotas iguales (sin interés aplicado en monto de cuota por ahora)
        List<Cuota> cuotas = generarCuotasIguales(idGenerado, monto, cantidadCuotas);

        boolean okCuotas = CuotaDAO.insertarBatch(cuotas);
        if (!okCuotas) {
            System.out.println("Advertencia: cuotas no insertadas. (Sin rollback por MyISAM)");
        }

        VariablesDAO.incrementarNroCredito(); // Opcional si querés usar este contador
        return idGenerado;
    }

    private static List<Cuota> generarCuotasIguales(int idCredito, double montoTotal, int cant) {
        List<Cuota> list = new ArrayList<>();
        double base = Math.floor((montoTotal / cant) * 100.0) / 100.0;
        double acumulado = 0;
        for (int i = 1; i <= cant; i++) {
            double montoCuota;
            if (i < cant) {
                montoCuota = base;
                acumulado += base;
            } else {
                montoCuota = Math.round((montoTotal - acumulado) * 100.0) / 100.0; // Ajuste final
            }
            list.add(new Cuota(idCredito, i, montoCuota));
        }
        return list;
    }
}