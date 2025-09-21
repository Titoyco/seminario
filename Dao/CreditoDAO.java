package Dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Model.Credito;
import Model.Cuota;

/**
 * DAO para la tabla 'creditos'.
 * Usa transacciones InnoDB para crear crédito + cuotas atómicamente.
 */
public class CreditoDAO {

    private static Credito map(ResultSet rs) throws SQLException {
        return new Credito(
            rs.getInt("id"),
            rs.getInt("id_cliente"),
            rs.getDouble("monto"),
            rs.getDouble("tasa_interes"),
            rs.getInt("cantidad_cuotas"),
            rs.getDate("fecha_otorgado").toLocalDate(),
            rs.getString("estado"),
            rs.getInt("lote_origen"),
            null
        );
    }

    public static Credito buscarPorId(int id) {
        String sql = "SELECT * FROM creditos WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error buscar crédito: " + e.getMessage());
        }
        return null;
    }

    public static List<Credito> listarPorCliente(int idCliente) {
        List<Credito> list = new ArrayList<>();
        String sql = "SELECT * FROM creditos WHERE id_cliente = ? ORDER BY id DESC";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error listar créditos cliente: " + e.getMessage());
        }
        return list;
    }

    public static List<Credito> listarTodos() {
        List<Credito> list = new ArrayList<>();
        String sql = "SELECT * FROM creditos ORDER BY id DESC";
        try (Connection conn = ConexionMySQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Error listar créditos: " + e.getMessage());
        }
        return list;
    }

    public static boolean actualizarEstado(int idCredito, String nuevoEstado) {
        String sql = "UPDATE creditos SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCredito);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizar estado crédito: " + e.getMessage());
        }
        return false;
    }

    public static int crearCreditoConCuotas(Credito credito, List<Cuota> cuotas, int loteOrigen) {
        String sqlCredito = "INSERT INTO creditos (id_cliente, monto, fecha_otorgado, tasa_interes, cantidad_cuotas, estado, lote_origen) " +
                            "VALUES (?,?,?,?,?,'vigente',?)";
        String sqlCuota = "INSERT INTO cuotas (id_credito, numero, monto, estado) VALUES (?,?,?,'pendiente')";

        try (Connection conn = ConexionMySQL.getConnection()) {
            conn.setAutoCommit(false);

            int idCreditoGenerado;
            try (PreparedStatement ps = conn.prepareStatement(sqlCredito, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, credito.getIdCliente());
                ps.setDouble(2, credito.getMontoTotal());
                ps.setDate(3, Date.valueOf(credito.getFechaOtorgamiento()));
                ps.setDouble(4, credito.getTasaInteres());
                ps.setInt(5, credito.getCantidadCuotas());
                ps.setInt(6, loteOrigen);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("No se obtuvo ID de crédito.");
                    idCreditoGenerado = rs.getInt(1);
                }
            }

            try (PreparedStatement psCuota = conn.prepareStatement(sqlCuota)) {
                for (Cuota c : cuotas) {
                    psCuota.setInt(1, idCreditoGenerado);
                    psCuota.setInt(2, c.getNumero());
                    psCuota.setDouble(3, c.getMonto());
                    psCuota.addBatch();
                }
                psCuota.executeBatch();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return idCreditoGenerado;
        } catch (SQLException e) {
            System.out.println("Error transaccional crear crédito: " + e.getMessage());
        }
        return -1;
    }
}