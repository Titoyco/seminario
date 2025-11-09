// Dao/VariablesDAO.java
// DAO para la tabla 'variables'.

package Dao;
import java.sql.*;

import Model.Variables;

public class VariablesDAO {

    // Mapea un ResultSet a un objeto Variables
    private static Variables map(ResultSet rs) throws SQLException {
        return new Variables(
            rs.getString("pass"),
            rs.getString("master_pass"),
            rs.getInt("nro_credito"),
            rs.getInt("nro_lote"),
            rs.getDouble("interes_mensual")
        );
    }

    // Retorna el objeto Variables (única fila)
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

    // Métodos para obtener el valor del campo pass
    public static String getPassword() {
        Variables v = getVariables();
        return v != null ? v.getPassword() : null;
    }

    // Métodos para obtener el valor del campo master_pass
    public static String getMasterPassword() {
        Variables v = getVariables();
        return v != null ? v.getMasterPassword() : null;
    }

    // Métodos para obtener el valor del campo nro_lote
    public static Integer getNroLote() {
        Variables v = getVariables();
        return v != null ? v.getNroLote() : null;
    }

    // Métodos para obtener el valor del campo nro_credito
    public static Integer getNroCredito() {
        Variables v = getVariables();
        return v != null ? v.getNroCredito() : null;
    }

    // Métodos para obtener el valor del campo interes_mensual
    public static Double getInteresMensual() {
        Variables v = getVariables();
        return v != null ? v.getInteresMensual() : null;
    }

    // Métodos para actualizar el campo pass
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

    // Métodos para actualizar el campo interes_mensual
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

    // Métodos para actualizar el campo nro_credito
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

    // Métodos para actualizar el campo nro_lote
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