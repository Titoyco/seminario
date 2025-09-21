package Model;

import java.sql.*;
import java.time.LocalDate;

public class LoteDAO {

    private static Lote map(ResultSet rs) throws SQLException {
        return new Lote(
            rs.getInt("nro_lote"),
            rs.getDate("fecha_apertura").toLocalDate(),
            rs.getDate("fecha_cierre") != null ? rs.getDate("fecha_cierre").toLocalDate() : null
        );
    }

    // Obtiene el lote actual (el que coincide con variables.nro_lote)
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

    /**
     * Cierra el lote actual (fecha_cierre) y crea automáticamente el siguiente lote
     * marcando en mora las cuotas pendientes vencidas.
     * Proceso en una transacción:
     *   1) Obtener nro_lote actual (variables)
     *   2) UPDATE lotes set fecha_cierre
     *   3) Marcar cuotas mora (cr.lote_origen + numero = lote_cerrado)
     *   4) INSERT nuevo lote (nro_lote + 1, fecha_apertura = fechaAperturaNuevo)
     *   5) UPDATE variables.nro_lote
     */
    public static boolean cerrarLoteActualYCrearNuevo(LocalDate fechaCierre, LocalDate fechaAperturaNuevo) {
        String selectVar = "SELECT nro_lote FROM variables LIMIT 1";
        String cerrar = "UPDATE lotes SET fecha_cierre=? WHERE nro_lote=? AND fecha_cierre IS NULL";
        String mora = "UPDATE cuotas cu " +
                      "JOIN creditos cr ON cr.id = cu.id_credito " +
                      "SET cu.estado='mora' " +
                      "WHERE cu.estado='pendiente' AND (cr.lote_origen + cu.numero)=?";
        String insertNuevo = "INSERT INTO lotes (nro_lote, fecha_apertura) VALUES (?,?)";
        String updateVar = "UPDATE variables SET nro_lote=? WHERE id=1";

        try (Connection conn = ConexionMySQL.getConnection()) {
            conn.setAutoCommit(false);

            int loteActual;
            try (PreparedStatement psSel = conn.prepareStatement(selectVar);
                 ResultSet rs = psSel.executeQuery()) {
                if (!rs.next()) throw new SQLException("No se encontró fila en variables.");
                loteActual = rs.getInt(1);
            }

            // Cerrar lote actual
            try (PreparedStatement psCerrar = conn.prepareStatement(cerrar)) {
                psCerrar.setDate(1, Date.valueOf(fechaCierre));
                psCerrar.setInt(2, loteActual);
                int updated = psCerrar.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("El lote actual ya estaba cerrado o no existe.");
                }
            }

            // Marcar cuotas en mora
            try (PreparedStatement psMora = conn.prepareStatement(mora)) {
                psMora.setInt(1, loteActual);
                psMora.executeUpdate();
            }

            int nuevoLote = loteActual + 1;

            // Crear nuevo lote
            try (PreparedStatement psNuevo = conn.prepareStatement(insertNuevo)) {
                psNuevo.setInt(1, nuevoLote);
                psNuevo.setDate(2, Date.valueOf(fechaAperturaNuevo));
                psNuevo.executeUpdate();
            }

            // Actualizar variables
            try (PreparedStatement psUpd = conn.prepareStatement(updateVar)) {
                psUpd.setInt(1, nuevoLote);
                psUpd.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.out.println("Error cerrarLoteActualYCrearNuevo: " + e.getMessage());
        }
        return false;
    }
}