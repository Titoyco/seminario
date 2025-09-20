package Model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla 'creditos' (schema actual).
 * Mapea a modelo Credito (monto -> montoTotal, fecha_otorgado -> fechaOtorgamiento).
 */
public class CreditoDAO {

    public static int insertar(Credito c, int loteOrigen) {
        String sql = "INSERT INTO creditos (id_cliente, monto, fecha_otorgado, tasa_interes, cantidad_cuotas, estado, lote_origen) " +
                     "VALUES (?,?,?,?,?,'vigente',?)";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getIdCliente());
            ps.setDouble(2, c.getMontoTotal());
            ps.setDate(3, Date.valueOf(c.getFechaOtorgamiento()));
            ps.setDouble(4, c.getTasaInteres());
            ps.setInt(5, c.getCantidadCuotas());
            ps.setInt(6, loteOrigen);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertar crédito: " + e.getMessage());
        }
        return -1;
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
            System.out.println("Error listarPorCliente: " + e.getMessage());
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
            System.out.println("Error listarTodos: " + e.getMessage());
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
            System.out.println("Error actualizarEstado crédito: " + e.getMessage());
        }
        return false;
    }

    private static Credito map(ResultSet rs) throws SQLException {
        return new Credito(
            rs.getInt("id"),
            rs.getInt("id_cliente"),
            rs.getDouble("monto"),
            rs.getDouble("tasa_interes"),
            rs.getInt("cantidad_cuotas"),
            rs.getDate("fecha_otorgado").toLocalDate(),
            null
        );
    }
}