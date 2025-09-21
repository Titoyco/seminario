package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.ConexionMySQL;
import Model.Cuota;

public class CuotaDAO {

    private static Cuota map(ResultSet rs) throws SQLException {
        return new Cuota(
            rs.getInt("id"),
            rs.getInt("id_credito"),
            rs.getInt("numero"),
            rs.getDouble("monto"),
            rs.getString("estado")
        );
    }

    public static List<Cuota> listarPorCredito(int idCredito) {
        List<Cuota> list = new ArrayList<>();
        String sql = "SELECT * FROM cuotas WHERE id_credito = ? ORDER BY numero ASC";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCredito);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error listar cuotas: " + e.getMessage());
        }
        return list;
    }

    public static Cuota buscarPorId(int idCuota) {
        String sql = "SELECT * FROM cuotas WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCuota);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error buscar cuota: " + e.getMessage());
        }
        return null;
    }

    public static boolean marcarPagada(int idCuota, Connection externalConn) throws SQLException {
        String sql = "UPDATE cuotas SET estado = 'pagada' WHERE id = ? AND estado <> 'pagada'";
        try (PreparedStatement ps = externalConn.prepareStatement(sql)) {
            ps.setInt(1, idCuota);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean marcarMoraPorLoteCerrado(int loteCerrado) {
        String sql = "UPDATE cuotas cu " +
                     "JOIN creditos cr ON cr.id = cu.id_credito " +
                     "SET cu.estado = 'mora' " +
                     "WHERE cu.estado = 'pendiente' AND (cr.lote_origen + cu.numero) = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loteCerrado);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error marcar mora lote: " + e.getMessage());
        }
        return false;
    }
}