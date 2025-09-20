package Model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla 'lotes'.
 * Maneja apertura y cierre. En cierre dispara el marcado de mora de cuotas.
 */
public class LoteDAO {

    public static boolean abrirNuevoLote(LocalDate fechaApertura) {
        Integer loteActual = VariablesDAO.getNroLote();
        if (loteActual == null) return false;
        int nuevo = loteActual + 1;

        String sqlInsert = "INSERT INTO lotes (nro_lote, fecha_apertura) VALUES (?,?)";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setInt(1, nuevo);
            ps.setDate(2, Date.valueOf(fechaApertura));
            int rows = ps.executeUpdate();
            if (rows > 0) {
                if (VariablesDAO.setNroLote(nuevo)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error abrirNuevoLote: " + e.getMessage());
        }
        return false;
    }

    public static boolean cerrarLote(int nroLote, LocalDate fechaCierre) {
        // 1) Cerrar lote (si no está cerrado)
        String sql = "UPDATE lotes SET fecha_cierre = ? WHERE nro_lote = ? AND fecha_cierre IS NULL";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaCierre));
            ps.setInt(2, nroLote);
            int updated = ps.executeUpdate();
            // updated puede ser 0 si ya estaba cerrado o no existe
        } catch (SQLException e) {
            System.out.println("Error cerrarLote: " + e.getMessage());
            return false;
        }

        // 2) Marcar cuotas en mora cuyo lote_vencimiento == nroLote
        return CuotaDAO.marcarMoraPorLoteCerrado(nroLote);
    }

    public static boolean loteEstaCerrado(int nroLote) {
        String sql = "SELECT fecha_cierre FROM lotes WHERE nro_lote = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nroLote);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date fc = rs.getDate("fecha_cierre");
                    return fc != null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loteEstaCerrado: " + e.getMessage());
        }
        return false;
    }

    public static List<Integer> lotesAbiertos() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT nro_lote FROM lotes WHERE fecha_cierre IS NULL ORDER BY nro_lote";
        try (Connection conn = ConexionMySQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(rs.getInt(1));
        } catch (SQLException e) {
            System.out.println("Error lotesAbiertos: " + e.getMessage());
        }
        return list;
    }
}