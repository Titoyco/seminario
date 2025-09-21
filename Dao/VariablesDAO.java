package Dao;
import java.sql.*;

import Model.Variables;

/**
 * DAO para la tabla 'variables'.
 * Ahora la base está en InnoDB, por lo que operaciones son transaccionales si se agrupan en una misma conexión.
 * Se asume UNA sola fila (id=1).
 */
public class VariablesDAO {

    private static Variables map(ResultSet rs) throws SQLException {
        return new Variables(
            rs.getString("pass"),
            rs.getString("master_pass"),
            rs.getInt("nro_credito"),
            rs.getInt("nro_lote"),
            rs.getDouble("interes_mensual")
        );
    }

    public static Variables getVariables() {
        String sql = "SELECT pass, master_pass, nro_credito, nro_lote, interes_mensual FROM variables LIMIT 1";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.out.println("Error getVariables: " + e.getMessage());
        }
        return null;
    }

    public static String getPassword() {
        Variables v = getVariables();
        return v != null ? v.getPassword() : null;
    }

    public static String getMasterPassword() {
        Variables v = getVariables();
        return v != null ? v.getMasterPassword() : null;
    }

    public static Integer getNroLote() {
        Variables v = getVariables();
        return v != null ? v.getNroLote() : null;
    }

    public static Integer getNroCredito() {
        Variables v = getVariables();
        return v != null ? v.getNroCredito() : null;
    }

    public static Double getInteresMensual() {
        Variables v = getVariables();
        return v != null ? v.getInteresMensual() : null;
    }

    public static boolean updatePassword(String nuevaPass) {
        String sql = "UPDATE variables SET pass = ? LIMIT 1";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevaPass);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updatePassword: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateInteresMensual(double nuevoValor) {
        String sql = "UPDATE variables SET interes_mensual = ? LIMIT 1";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, nuevoValor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updateInteresMensual: " + e.getMessage());
        }
        return false;
    }

    public static synchronized int incrementarNroCredito() {
        Integer actual = getNroCredito();
        if (actual == null) return -1;
        int nuevo = actual + 1;
        String sql = "UPDATE variables SET nro_credito = ? LIMIT 1";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevo);
            if (ps.executeUpdate() > 0) return nuevo;
        } catch (SQLException e) {
            System.out.println("Error incrementarNroCredito: " + e.getMessage());
        }
        return -1;
    }

    public static synchronized boolean setNroLote(int nuevoLote) {
        String sql = "UPDATE variables SET nro_lote = ? LIMIT 1";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevoLote);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error setNroLote: " + e.getMessage());
        }
        return false;
    }
}