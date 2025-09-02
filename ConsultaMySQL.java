import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ConsultaMySQL {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/lamarina?useSSL=true&serverTimezone=UTC";
        String usuario = "root";
        String contrasena = "";

        try {
            Connection conn = DriverManager.getConnection(url, usuario, contrasena);
            Statement stmt = conn.createStatement();

            // Ejemplo de consulta: obtener todos los registros de la tabla 'clientes'
            String consulta = "SELECT DNI, RSOCIAL, INTERIOR FROM clientes";
            ResultSet rs = stmt.executeQuery(consulta);

            // Mostrar los resultados
            while (rs.next()) {
                int id = rs.getInt("DNI");
                String nombre = rs.getString("RSOCIAL");
                String interior = rs.getString("INTERIOR");
                System.out.println("DNI: " + id + ", Nombre: " + nombre + ", Interior: " + interior);
            }

            // Cerrar recursos
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}