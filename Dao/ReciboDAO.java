package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO para obtener los datos necesarios del recibo a partir de una cuota.
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
}