import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/lamarina";
        String usuario = "root";
        String contrasena = "";

        try {
            Connection conn = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("¡Conexión exitosa!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
}