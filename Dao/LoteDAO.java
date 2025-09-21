package Dao;

import java.sql.*;
import java.time.LocalDate;

import Model.ConexionMySQL;
import Model.Lote;

public class LoteDAO {

    private static Lote map(ResultSet rs) throws SQLException {
        return new Lote(
            rs.getInt("nro_lote"),
            rs.getDate("fecha_apertura").toLocalDate(),
            rs.getDate("fecha_cierre") != null ? rs.getDate("fecha_cierre").toLocalDate() : null
        );
    }

    public static Lote getLoteActual() {
        String sql = "SELECT l.* FROM lotes l JOIN variables v ON l.nro_lote = v.nro_lote LIMIT 1";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.out.println("Error getLoteActual: " + e.getMessage());
        }
        return null;
    }

    public static boolean cerrarLoteActualYCrearNuevo(LocalDate fechaCierre, LocalDate fechaAperturaNuevo) {
        String selectVar = "SELECT nro_lote FROM variables LIMIT 1";
        String cerrar = "UPDATE lotes SET fecha_cierre=? WHERE nro_lote=? AND fecha_cierre IS NULL";
        String mora = "UPDATE cuotas cu JOIN creditos cr ON cr.id = cu.id_credito " +
                      "SET cu.estado='mora' " +
                      "WHERE cu.estado='pendiente' AND (cr.lote_origen + cu.numero)=?";
        String insertNuevo = "INSERT INTO lotes (nro_lote, fecha_apertura) VALUES (?,?)";
        String updateVar = "UPDATE variables SET nro_lote=? WHERE id=1";

        Connection conn = null;
        try {
            conn = ConexionMySQL.getConnection();
            conn.setAutoCommit(false);

            int loteActual;
            try (PreparedStatement psSel = conn.prepareStatement(selectVar);
                 ResultSet rs = psSel.executeQuery()) {
                if (!rs.next()) throw new SQLException("No se encontró fila en variables.");
                loteActual = rs.getInt(1);
            }

            try (PreparedStatement psCerrar = conn.prepareStatement(cerrar)) {
                psCerrar.setDate(1, Date.valueOf(fechaCierre));
                psCerrar.setInt(2, loteActual);
                int updated = psCerrar.executeUpdate();
                if (updated == 0) throw new SQLException("El lote actual ya estaba cerrado o no existe.");
            }

            try (PreparedStatement psMora = conn.prepareStatement(mora)) {
                psMora.setInt(1, loteActual);
                psMora.executeUpdate();
            }

            int nuevoLote = loteActual + 1;

            try (PreparedStatement psNuevo = conn.prepareStatement(insertNuevo)) {
                psNuevo.setInt(1, nuevoLote);
                psNuevo.setDate(2, Date.valueOf(fechaAperturaNuevo));
                psNuevo.executeUpdate();
            }

            try (PreparedStatement psUpd = conn.prepareStatement(updateVar)) {
                psUpd.setInt(1, nuevoLote);
                psUpd.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.out.println("Error cerrarLoteActualYCrearNuevo: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
            }
        }
        return false;
    }
}