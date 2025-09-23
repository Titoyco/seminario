package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Lista de cuotas pendientes de créditos vigentes para un cliente
    public static List<Map<String,Object>> listarPendientesPorCliente(int idCliente) {
        List<Map<String,Object>> lista = new ArrayList<>();
        String sql = "SELECT cu.id AS id_cuota, cu.numero, cu.monto, cr.id AS id_credito " +
                     "FROM cuotas cu JOIN creditos cr ON cr.id = cu.id_credito " +
                     "WHERE cr.id_cliente = ? AND cu.estado='pendiente' AND cr.estado='vigente' " +
                     "ORDER BY cr.id DESC, cu.numero ASC";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,Object> row = new HashMap<>();
                    row.put("id_cuota", rs.getInt("id_cuota"));
                    row.put("id_credito", rs.getInt("id_credito"));
                    row.put("numero", rs.getInt("numero"));
                    row.put("monto", rs.getDouble("monto"));
                    lista.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error listar pendientes cliente: " + e.getMessage());
        }
        return lista;
    }

    // Verifica si aún existen cuotas pendientes para un crédito
    public static boolean existenCuotasPendientes(int idCredito) {
        String sql = "SELECT 1 FROM cuotas WHERE id_credito = ? AND estado='pendiente' LIMIT 1";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCredito);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error existenCuotasPendientes: " + e.getMessage());
        }
        return false;
    }
}