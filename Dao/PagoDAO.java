package Dao;

import java.sql.*;
import java.time.LocalDate;

/**
 * DAO para registrar pagos. Marca la cuota como pagada (no contempla pagos parciales todavía).
 * Mejora futura: sumar pagos y marcar pagada cuando sum >= monto cuota.
 */
public class PagoDAO {

    public static boolean registrarPagoCompleto(int idCuota, double montoEsperado, String metodo, String obs, LocalDate fecha) {
        String insertPago = "INSERT INTO pagos (id_cuota, fecha_pago, monto_pagado, metodo_pago, observaciones) VALUES (?,?,?,?,?)";
        try (Connection conn = ConexionMySQL.getConnection()) {
            conn.setAutoCommit(false);

            // Insert pago
            try (PreparedStatement ps = conn.prepareStatement(insertPago)) {
                ps.setInt(1, idCuota);
                ps.setDate(2, Date.valueOf(fecha));
                ps.setDouble(3, montoEsperado);
                ps.setString(4, metodo);
                ps.setString(5, obs);
                ps.executeUpdate();
            }

            // Marcar cuota pagada
            CuotaDAO.marcarPagada(idCuota, conn);

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.out.println("Error registrar pago: " + e.getMessage());
        }
        return false;
    }
}