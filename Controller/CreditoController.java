package Controller;

import Model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Dao.CreditoDAO;
import Dao.CuotaDAO;
import Dao.VariablesDAO;

/**
 * Lógica de negocio para creación de créditos.
 * Cuotas simples (monto / cantidad) con ajuste de redondeo en la última.
 * Interés: todavía no aplicado al importe de cuotas (pendiente definir fórmula).
 */
public class CreditoController {

    public static int crearCredito(int idCliente, double monto, double tasaInteres, int cantCuotas, LocalDate fecha) {
        Integer loteActual = VariablesDAO.getNroLote();
        if (loteActual == null) {
            System.out.println("No se pudo obtener lote actual.");
            return -1;
        }
        // Si la tasaInteres ingresada es 0, podríamos usar variables.interes_mensual
        if (tasaInteres <= 0) {
            Double tm = VariablesDAO.getInteresMensual();
            if (tm != null) {
                // Si interes_mensual está guardado como decimal (0.05 = 5%), convierte a porcentaje:
                tasaInteres = tm * 100.0;
            }
        }

        Credito credito = new Credito(idCliente, monto, tasaInteres, cantCuotas, fecha);
        List<Cuota> cuotas = generarCuotasIguales(monto, cantCuotas);

        int idGenerado = CreditoDAO.crearCreditoConCuotas(credito, cuotas, loteActual);
        if (idGenerado > 0) {
            VariablesDAO.incrementarNroCredito();
        }
        return idGenerado;
    }

    private static List<Cuota> generarCuotasIguales(double montoTotal, int cant) {
        List<Cuota> list = new ArrayList<>();
        double base = Math.floor((montoTotal / cant) * 100.0) / 100.0;
        double acumulado = 0;
        for (int i = 1; i <= cant; i++) {
            double cuotaVal;
            if (i < cant) {
                cuotaVal = base;
                acumulado += base;
            } else {
                cuotaVal = Math.round((montoTotal - acumulado) * 100.0) / 100.0;
            }
            list.add(new Cuota(0, i, cuotaVal)); // id_credito se asigna en la transacción
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