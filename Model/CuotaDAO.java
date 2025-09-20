package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para cuotas. Estado persistido.
 */
public class CuotaDAO {

    public static boolean insertarBatch(List<Cuota> cuotas) {
        String sql = "INSERT INTO cuotas (id_credito, numero, monto, estado) VALUES (?,?,?,'pendiente')";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Cuota c : cuotas) {
                ps.setInt(1, c.getIdCredito());
                ps.setInt(2, c.getNumero());
                ps.setDouble(3, c.getMonto());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            System.out.println("Error insertarBatch cuotas: " + e.getMessage());
        }
        return false;
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
            System.out.println("Error listarPorCredito cuotas: " + e.getMessage());
        }
        return list;
    }

    public static boolean marcarPagada(int idCuota) {
        String sql = "UPDATE cuotas SET estado = 'pagada' WHERE id = ? AND estado <> 'pagada'";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCuota);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error marcarPagada cuota: " + e.getMessage());
        }
        return false;
    }

    public static boolean marcarMoraPorLoteCerrado(int loteCerrado) {
        // Marca 'mora' las cuotas pendientes cuyo lote_vencimiento = loteCerrado
        // lote_vencimiento = lote_origen + numero
        String sql = "UPDATE cuotas cu JOIN creditos cr ON cr.id = cu.id_credito " +
                     "SET cu.estado = 'mora' " +
                     "WHERE cu.estado = 'pendiente' AND (cr.lote_origen + cu.numero) = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loteCerrado);
            ps.executeUpdate(); // No importa cuántas filas; puede ser 0
            return true;
        } catch (SQLException e) {
            System.out.println("Error marcarMoraPorLoteCerrado: " + e.getMessage());
        }
        return false;
    }

    private static Cuota map(ResultSet rs) throws SQLException {
        return new Cuota(
            rs.getInt("id"),
            rs.getInt("id_credito"),
            rs.getInt("numero"),
            rs.getDouble("monto"),
            rs.getString("estado")
        );
    }
}