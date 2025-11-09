// DeudaDAO.java
// DAO para 'deudas'.

package Dao;

import java.sql.*;
import java.util.*;

public class DeudaDAO {

    // Resumen de deuda de un cliente
    public static Map<String,Double> resumenDeudaCliente(int idCliente) {
        Map<String,Double> res = new HashMap<>();
        Integer loteActual = VariablesDAO.getNroLote();
        if (loteActual == null) {
            res.put("total", 0.0);
            res.put("actual", 0.0);
            res.put("mora", 0.0);
            return res;
        }
        // Consulta SQL para obtener los totales de deuda
        String sql =
            "SELECT " +
            " SUM(CASE WHEN cu.estado IN ('pendiente','mora') THEN cu.monto ELSE 0 END) AS deuda_total, " +
            " SUM(CASE WHEN cu.estado IN ('pendiente','mora') AND (cr.lote_origen + cu.numero) <= ? THEN cu.monto ELSE 0 END) AS deuda_actual, " +
            " SUM(CASE WHEN cu.estado='mora' THEN cu.monto ELSE 0 END) AS deuda_mora " +
            "FROM cuotas cu JOIN creditos cr ON cr.id = cu.id_credito " +
            "WHERE cr.id_cliente = ?";
        // Ejecutar la consulta y obtener resultados
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loteActual);
            ps.setInt(2, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    res.put("total", rs.getDouble("deuda_total"));
                    res.put("actual", rs.getDouble("deuda_actual"));
                    res.put("mora", rs.getDouble("deuda_mora"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error resumenDeudaCliente: " + e.getMessage());
            res.put("total", 0.0);
            res.put("actual", 0.0);
            res.put("mora", 0.0);
        }
        // Retornar resumen de deuda
        return res;
    }

    // Detalle de cuotas pendientes de un cliente
    public static List<Map<String,Object>> detalleCuotasPendientes(int idCliente) {
        List<Map<String,Object>> list = new ArrayList<>();
        Integer loteActual = VariablesDAO.getNroLote();
        if (loteActual == null) loteActual = 0;
        String sql =
            "SELECT cr.id AS id_credito, cu.id AS id_cuota, cu.numero, cu.monto, cu.estado, " +
            "       cr.lote_origen, (cr.lote_origen + cu.numero) AS lote_venc " +
            "FROM cuotas cu JOIN creditos cr ON cr.id = cu.id_credito " +
            "WHERE cr.id_cliente = ? AND cu.estado IN ('pendiente','mora') " +
            "ORDER BY lote_venc, cr.id, cu.numero";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,Object> m = new HashMap<>();
                    m.put("id_credito", rs.getInt("id_credito"));
                    m.put("id_cuota", rs.getInt("id_cuota"));
                    m.put("numero", rs.getInt("numero"));
                    m.put("monto", rs.getDouble("monto"));
                    m.put("estado", rs.getString("estado"));
                    m.put("lote_origen", rs.getInt("lote_origen"));
                    int loteVenc = rs.getInt("lote_venc");
                    m.put("lote_venc", loteVenc);
                    m.put("es_futura", loteVenc > loteActual);
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error detalleCuotasPendientes: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lista el resumen de deuda por cliente.
     * Retorna una lista de mapas con claves:
     *   id_cliente (Integer), nombre_cliente (String),
     *   deuda_total (Double), deuda_actual (Double), deuda_mora (Double)
     *
     * Incluye todos los clientes (las sumas pueden ser 0.0).
     */
    public static List<Map<String,Object>> listarResumenDeudaPorClientes() {
        List<Map<String,Object>> lista = new ArrayList<>();
        Integer loteActual = VariablesDAO.getNroLote();
        if (loteActual == null) loteActual = 0;

        String sql =
            "SELECT cl.id AS id_cliente, cl.nombre AS nombre_cliente, " +
            "  COALESCE(SUM(CASE WHEN cu.estado IN ('pendiente','mora') THEN cu.monto ELSE 0 END),0) AS deuda_total, " +
            "  COALESCE(SUM(CASE WHEN cu.estado IN ('pendiente','mora') AND (cr.lote_origen + cu.numero) <= ? THEN cu.monto ELSE 0 END),0) AS deuda_actual, " +
            "  COALESCE(SUM(CASE WHEN cu.estado='mora' THEN cu.monto ELSE 0 END),0) AS deuda_mora " +
            "FROM clientes cl " +
            "LEFT JOIN creditos cr ON cr.id_cliente = cl.id " +
            "LEFT JOIN cuotas cu ON cu.id_credito = cr.id " +
            "GROUP BY cl.id, cl.nombre " +
            "ORDER BY cl.nombre ASC";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loteActual);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,Object> m = new HashMap<>();
                    m.put("id_cliente", rs.getInt("id_cliente"));
                    m.put("nombre_cliente", rs.getString("nombre_cliente"));
                    m.put("deuda_total", rs.getDouble("deuda_total"));
                    m.put("deuda_actual", rs.getDouble("deuda_actual"));
                    m.put("deuda_mora", rs.getDouble("deuda_mora"));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error listarResumenDeudaPorClientes: " + e.getMessage());
        }
        return lista;
    }


}