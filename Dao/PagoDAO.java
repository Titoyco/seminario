// PagoDAO.java
// DAO para la tabla 'pagos'.

package Dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;

public class PagoDAO {

    // Registra un pago completo y marca la cuota como pagada
    public static int registrarPagoCompleto(int idCuota, double montoEsperado, String metodo, String obs, LocalDate fecha) {
        String insertPago = "INSERT INTO pagos (id_cuota, fecha_pago, monto_pagado, metodo_pago, observaciones) VALUES (?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConexionMySQL.getConnection();
            conn.setAutoCommit(false);

            // Si fecha es null, usamos hoy para evitar Date.valueOf(null)
            LocalDate efectiva = (fecha != null) ? fecha : LocalDate.now();

            int idPagoGenerado;
            // Insertar pago
            try (PreparedStatement ps = conn.prepareStatement(insertPago, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idCuota);
                ps.setDate(2, Date.valueOf(efectiva));
                ps.setDouble(3, montoEsperado);
                ps.setString(4, metodo);
                ps.setString(5, obs);
                ps.executeUpdate();
                // Obtener ID de pago generado
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("No se obtuvo ID de pago.");
                    idPagoGenerado = rs.getInt(1);
                }
            }
            // Marcar cuota como pagada
            CuotaDAO.marcarPagada(idCuota, conn);
            // Si todo bien, commit
            conn.commit();
            return idPagoGenerado;
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
        return -1;
    }

    // Lista todos los pagos de un cliente, con detalles de cuota y crédito
    public static List<Map<String,Object>> listarPagosPorCliente(int idCliente) {
        List<Map<String,Object>> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.id_cuota, p.fecha_pago, p.monto_pagado, p.metodo_pago, p.observaciones, " +
                     "cu.numero AS nro_cuota, cr.id AS id_credito " +
                     "FROM pagos p " +
                     "JOIN cuotas cu ON cu.id = p.id_cuota " +
                     "JOIN creditos cr ON cr.id = cu.id_credito " +
                     "WHERE cr.id_cliente = ? " +
                     "ORDER BY p.id DESC";
        // Ejecutar consulta
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
        // Retornar lista de pagos
        return lista;
    }

    // Anula un pago, elimina el registro y actualiza cuota y crédito si es necesario
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
        // Ejecutar transacción
        try {
            conn = ConexionMySQL.getConnection();
            conn.setAutoCommit(false);
            int idCuota;
            int idCredito;
            // Obtener datos del pago
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
            // Eliminar pago
            try (PreparedStatement psDel = conn.prepareStatement(deletePago)) {
                psDel.setInt(1, idPago);
                psDel.executeUpdate();
            }
            // Actualizar estado de cuota
            try (PreparedStatement psCuota = conn.prepareStatement(updateCuota)) {
                psCuota.setInt(1, idCuota);
                psCuota.executeUpdate();
            }
            // Actualizar estado de crédito si es necesario
            try (PreparedStatement psCred = conn.prepareStatement(updateCreditoVigente)) {
                psCred.setInt(1, idCredito);
                psCred.executeUpdate();
            }
            // Commit si todo bien
            conn.commit();
            // Retornar true
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

    public static List<Map<String,Object>> listarTodosOrdenadosDesc() {
        List<Map<String,Object>> lista = new ArrayList<>();
        String sql =
            "SELECT p.id AS id_pago, p.id_cuota, p.fecha_pago, p.monto_pagado, p.metodo_pago, p.observaciones, " +
            "       cu.numero AS nro_cuota, cr.id AS id_credito, cl.id AS cliente_id, cl.nombre AS cliente_nombre " +
            "FROM pagos p " +
            "JOIN cuotas cu ON cu.id = p.id_cuota " +
            "JOIN creditos cr ON cr.id = cu.id_credito " +
            "JOIN clientes cl ON cl.id = cr.id_cliente " +
            "ORDER BY p.id DESC";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String,Object> row = new HashMap<>();
                row.put("id_pago", rs.getInt("id_pago"));
                row.put("id_cuota", rs.getInt("id_cuota"));
                Date d = rs.getDate("fecha_pago");
                row.put("fecha_pago", d != null ? d.toLocalDate() : null);
                row.put("monto_pagado", rs.getDouble("monto_pagado"));
                row.put("metodo_pago", rs.getString("metodo_pago"));
                row.put("observaciones", rs.getString("observaciones"));
                row.put("nro_cuota", rs.getInt("nro_cuota"));
                row.put("id_credito", rs.getInt("id_credito"));
                row.put("cliente_id", rs.getInt("cliente_id"));
                row.put("cliente_nombre", rs.getString("cliente_nombre"));
                lista.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error listarTodosOrdenadosDesc: " + e.getMessage());
        }
        return lista;
    }
}