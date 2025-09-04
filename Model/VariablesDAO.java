package Model;
import java.sql.*;

public class VariablesDAO {

    // Obtiene el modelo completo de variables (password y master)
    public static Variables getVariables() {
        Variables vars = null;
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT pass, master_pass FROM variables LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                vars = new Variables(rs.getString("pass"), rs.getString("master_pass"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener variables: " + e.getMessage());
        }
        return vars;
    }

    // Métodos individuales si aún quieres mantenerlos
    public static String getPassword() {
        Variables vars = getVariables();
        return vars != null ? vars.getPassword() : null;
    }

    public static String getMasterPassword() {
        Variables vars = getVariables();
        return vars != null ? vars.getMasterPassword() : null;
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