package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO para obtener los datos necesarios del recibo.
 */
public class ReciboDAO {

    /**
     * Retorna un Map con:
     *  - cliente_nombre (String)
     *  - cliente_dni (String)
     *  - credito_id (int)
     *  - credito_fecha (java.time.LocalDate)
     *  - credito_monto (double)
     *  - credito_cantidad (int)
     *  - cuota_numero (int)
     *  - cuota_monto (double)
     */
    public static Map<String, Object> datosReciboPorCuota(int idCuota) {
        String sql =
            "SELECT cl.nombre AS cliente_nombre, cl.documento AS cliente_dni, " +
            "       cr.id AS credito_id, cr.fecha_otorgado, cr.monto AS credito_monto, cr.cantidad_cuotas AS credito_cantidad, " +
            "       cu.numero AS cuota_numero, cu.monto AS cuota_monto " +
            "FROM cuotas cu " +
            "JOIN creditos cr ON cr.id = cu.id_credito " +
            "JOIN clientes cl ON cl.id = cr.id_cliente " +
            "WHERE cu.id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCuota);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Map<String, Object> map = new HashMap<>();
                map.put("cliente_nombre", rs.getString("cliente_nombre"));
                map.put("cliente_dni", rs.getString("cliente_dni"));
                map.put("credito_id", rs.getInt("credito_id"));
                map.put("credito_fecha", rs.getDate("fecha_otorgado").toLocalDate());
                map.put("credito_monto", rs.getDouble("credito_monto"));
                map.put("credito_cantidad", rs.getInt("credito_cantidad"));
                map.put("cuota_numero", rs.getInt("cuota_numero"));
                map.put("cuota_monto", rs.getDouble("cuota_monto"));
                return map;
            }
        } catch (SQLException e) {
            System.out.println("Error datosReciboPorCuota: " + e.getMessage());
        }
        return null;
    }

    /**
     * NUEVO: Retorna datos del recibo a partir del ID de pago.
     * Incluye además:
     *  - pago_id (int)
     *  - pago_fecha (LocalDate)
     *  - pago_metodo (String)
     */
    public static Map<String, Object> datosReciboPorPago(int idPago) {
        String sql =
            "SELECT p.id AS pago_id, p.fecha_pago, p.metodo_pago, " +
            "       cl.nombre AS cliente_nombre, cl.documento AS cliente_dni, " +
            "       cr.id AS credito_id, cr.fecha_otorgado, cr.monto AS credito_monto, cr.cantidad_cuotas AS credito_cantidad, " +
            "       cu.numero AS cuota_numero, cu.monto AS cuota_monto " +
            "FROM pagos p " +
            "JOIN cuotas cu ON cu.id = p.id_cuota " +
            "JOIN creditos cr ON cr.id = cu.id_credito " +
            "JOIN clientes cl ON cl.id = cr.id_cliente " +
            "WHERE p.id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPago);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Map<String, Object> map = new HashMap<>();
                map.put("pago_id", rs.getInt("pago_id"));
                map.put("pago_fecha", rs.getDate("fecha_pago").toLocalDate());
                map.put("pago_metodo", rs.getString("metodo_pago"));
                map.put("cliente_nombre", rs.getString("cliente_nombre"));
                map.put("cliente_dni", rs.getString("cliente_dni"));
                map.put("credito_id", rs.getInt("credito_id"));
                map.put("credito_fecha", rs.getDate("fecha_otorgado").toLocalDate());
                map.put("credito_monto", rs.getDouble("credito_monto"));
                map.put("credito_cantidad", rs.getInt("credito_cantidad"));
                map.put("cuota_numero", rs.getInt("cuota_numero"));
                map.put("cuota_monto", rs.getDouble("cuota_monto"));
                return map;
            }
        } catch (SQLException e) {
            System.out.println("Error datosReciboPorPago: " + e.getMessage());
        }
        return null;
    }

    public static Map<String,Object> datosComprobanteCredito(int idCredito) {
        String sql = "SELECT cr.id AS credito_id, cr.fecha_otorgado, cr.monto AS capital, " +
                    "cr.tasa_interes AS tasa, cr.cantidad_cuotas, cl.nombre AS cliente_nombre, cl.documento AS cliente_dni " +
                    "FROM creditos cr JOIN clientes cl ON cl.id = cr.id_cliente WHERE cr.id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCredito);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Map<String,Object> m = new HashMap<>();
                m.put("credito_id", rs.getInt("credito_id"));
                m.put("fecha_otorgado", rs.getDate("fecha_otorgado").toLocalDate());
                m.put("capital", rs.getDouble("capital"));
                m.put("tasa", rs.getDouble("tasa"));
                m.put("cantidad_cuotas", rs.getInt("cantidad_cuotas"));
                m.put("cliente_nombre", rs.getString("cliente_nombre"));
                m.put("cliente_dni", rs.getString("cliente_dni"));

                // Calcular total con interés lineal
                double capital = rs.getDouble("capital");
                int n = rs.getInt("cantidad_cuotas");
                double tasaDecimal = rs.getDouble("tasa") / 100.0;
                double totalConInteres = capital * (1 + n * tasaDecimal);
                m.put("total_con_interes", totalConInteres);
                return m;
            }
        } catch (SQLException e) {
            System.out.println("Error datosComprobanteCredito: " + e.getMessage());
        }
        return null;
    }

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


}