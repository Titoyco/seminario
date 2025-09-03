package Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VariablesDAO {
    public static String getPassword() {
        String password = null;
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT pass FROM variables LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                password = rs.getString("pass");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener contraseña: " + e.getMessage());
        }
        return password;
    }

    public static String getMasterPassword() {
        String master = null;
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT master_pass FROM variables LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                master = rs.getString("master_pass");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener contraseña maestra: " + e.getMessage());
        }
        return master;
    }


    // Actualiza la contraseña
    public static boolean updatePassword(String nuevaPass) {
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE variables SET pass = ?")) {
            ps.setString(1, nuevaPass);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar contraseña: " + e.getMessage());
        }
        return false;
    }
}