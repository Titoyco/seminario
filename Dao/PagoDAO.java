package Dao;

import java.sql.*;
import java.time.LocalDate;

public class PagoDAO {

    public static boolean registrarPagoCompleto(int idCuota, double montoEsperado, String metodo, String obs, LocalDate fecha) {
        String insertPago = "INSERT INTO pagos (id_cuota, fecha_pago, monto_pagado, metodo_pago, observaciones) VALUES (?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConexionMySQL.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(insertPago)) {
                ps.setInt(1, idCuota);
                ps.setDate(2, Date.valueOf(fecha));
                ps.setDouble(3, montoEsperado);
                ps.setString(4, metodo);
                ps.setString(5, obs);
                ps.executeUpdate();
            }

            CuotaDAO.marcarPagada(idCuota, conn);

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error registrar pago: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
        return false;
    }
}