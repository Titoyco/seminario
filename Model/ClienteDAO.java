package Model;
import java.sql.*;
import java.util.*;

public class ClienteDAO {

    public static boolean insertar(String nombre, String documento, String direccion, String telefono, String email) {
        String sql = "INSERT INTO clientes (nombre, documento, direccion, telefono, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, documento);
            ps.setString(3, direccion);
            ps.setString(4, telefono);
            ps.setString(5, email);

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
        }
        return false;
    }


        public static boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, documento, direccion, telefono, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
        }
        return false;
    }

    public static boolean modificar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, documento = ?, direccion = ?, telefono = ?, email = ? WHERE id = ?";

        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.setInt(6, cliente.getId());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar cliente: " + e.getMessage());
        }
        return false;
    }

    public static List<Cliente> getTodos() {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = ConexionMySQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM clientes")) {
            while (rs.next()) {
                lista.add(new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("documento"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    public static boolean eliminarPorId(int id) {
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM clientes WHERE id = ?")) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
        return false;
    }

    public static Cliente buscarPorId(int id) {
        Cliente cliente = null;
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM clientes WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
        return cliente;
    }

}