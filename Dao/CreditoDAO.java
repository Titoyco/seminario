// CreditoDAO.java
// DAO para la tabla 'creditos'.
// Crea crédito + cuotas en una transacción.

package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Credito;
import Model.Cuota;


public class CreditoDAO {

    // Mapea un ResultSet a un objeto Credito
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

    // Busca un crédito por su ID
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

    // Lista créditos por ID de cliente
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

    // Lista todos los créditos
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

    // Actualiza el estado de un crédito
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

    // Crea un crédito y sus cuotas en una transacción
    public static int crearCreditoConCuotas(Credito credito, List<Cuota> cuotas, int loteOrigen) {
        String sqlCredito = "INSERT INTO creditos (id_cliente, monto, fecha_otorgado, tasa_interes, cantidad_cuotas, estado, lote_origen) " +
                            "VALUES (?,?,?,?,?,'vigente',?)";
        String sqlCuota = "INSERT INTO cuotas (id_credito, numero, monto, estado) VALUES (?,?,?,'pendiente')";
        Connection conn = null;
        try {
            conn = ConexionMySQL.getConnection();
            conn.setAutoCommit(false);
            int idCreditoGenerado;
            // Insertar crédito
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
            // Insertar cuotas
            try (PreparedStatement psCuota = conn.prepareStatement(sqlCuota)) {
                for (Cuota c : cuotas) {
                    psCuota.setInt(1, idCreditoGenerado);
                    psCuota.setInt(2, c.getNumero());
                    psCuota.setDouble(3, c.getMonto());
                    psCuota.addBatch();
                }
                psCuota.executeBatch();
            }
            // Confirmar transacción
            conn.commit();
            // Retornar ID del crédito creado
            return idCreditoGenerado;
            // Manejo de errores y rollback
        } catch (SQLException e) {
            System.out.println("Error transaccional crear crédito: " + e.getMessage());
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

    // Retorna la lista de cuotas de un crédito.
    public static List<Map<String,Object>> cuotasDeCredito(int idCredito) {
        List<Map<String,Object>> list = new ArrayList<>();
        String sql = "SELECT numero, monto FROM cuotas WHERE id_credito = ? ORDER BY numero ASC";
        try (Connection conn = ConexionMySQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCredito);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,Object> fila = new HashMap<>();
                    fila.put("numero", rs.getInt("numero"));
                    fila.put("monto", rs.getDouble("monto"));
                    list.add(fila);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error cuotasDeCredito: " + e.getMessage());
        }
        return list;
    }
    
    // Verifica si un cliente tiene créditos vigentes
    public static boolean existeCreditoVigentePorCliente(int idCliente) {
        String sql = "SELECT 1 FROM creditos WHERE id_cliente = ? AND estado = 'vigente' LIMIT 1";
        try (Connection conn = ConexionMySQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { /* log */ }
        return false;
    }

    // Verifica si el cliente tiene cualquier crédito asociado (independientemente del estado).
    public static boolean existeCreditosPorCliente(int idCliente) {
        String sql = "SELECT 1 FROM creditos WHERE id_cliente = ? LIMIT 1";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error existeCreditosPorCliente: " + e.getMessage());
        }
        return false;
    }

}