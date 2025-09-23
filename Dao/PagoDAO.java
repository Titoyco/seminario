package Dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;

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

    public static List<Map<String,Object>> listarPagosPorCliente(int idCliente) {
        List<Map<String,Object>> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.id_cuota, p.fecha_pago, p.monto_pagado, p.metodo_pago, p.observaciones, " +
                     "cu.numero AS nro_cuota, cr.id AS id_credito " +
                     "FROM pagos p " +
                     "JOIN cuotas cu ON cu.id = p.id_cuota " +
                     "JOIN creditos cr ON cr.id = cu.id_credito " +
                     "WHERE cr.id_cliente = ? " +
                     "ORDER BY p.fecha_pago DESC, p.id DESC";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,Object> row = new HashMap<>();
                    row.put("id_pago", rs.getInt("id"));
                    row.put("id_cuota", rs.getInt("id_cuota"));
                    row.put("fecha_pago", rs.getDate("fecha_pago").toLocalDate());
                    row.put("monto_pagado", rs.getDouble("monto_pagado"));
                    row.put("metodo_pago", rs.getString("metodo_pago"));
                    row.put("observaciones", rs.getString("observaciones"));
                    row.put("nro_cuota", rs.getInt("nro_cuota"));
                    row.put("id_credito", rs.getInt("id_credito"));
                    lista.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error listar pagos cliente: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Anula un pago:
     *  - Recupera id_cuota e id_credito
     *  - Borra el pago
     *  - Marca cuota 'pendiente'
     *  - Si el crédito estaba 'cancelado', lo regresa a 'vigente'
     */
    public static boolean anularPago(int idPago) {
        String select = "SELECT p.id_cuota, cu.id_credito, cr.estado AS estado_credito " +
                        "FROM pagos p " +
                        "JOIN cuotas cu ON cu.id = p.id_cuota " +
                        "JOIN creditos cr ON cr.id = cu.id_credito " +
                        "WHERE p.id = ? FOR UPDATE";
        String deletePago = "DELETE FROM pagos WHERE id = ?";
        String updateCuota = "UPDATE cuotas SET estado='pendiente' WHERE id=? AND estado='pagada'";
        String updateCreditoVigente = "UPDATE creditos SET estado='vigente' WHERE id=? AND estado='cancelado'";

        Connection conn = null;
        try {
            conn = ConexionMySQL.getConnection();
            conn.setAutoCommit(false);

            int idCuota;
            int idCredito;
            try (PreparedStatement psSel = conn.prepareStatement(select)) {
                psSel.setInt(1, idPago);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    idCuota = rs.getInt("id_cuota");
                    idCredito = rs.getInt("id_credito");
                }
            }

            try (PreparedStatement psDel = conn.prepareStatement(deletePago)) {
                psDel.setInt(1, idPago);
                psDel.executeUpdate();
            }

            try (PreparedStatement psCuota = conn.prepareStatement(updateCuota)) {
                psCuota.setInt(1, idCuota);
                psCuota.executeUpdate();
            }

            try (PreparedStatement psCred = conn.prepareStatement(updateCreditoVigente)) {
                psCred.setInt(1, idCredito);
                psCred.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error anular pago: " + e.getMessage());
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