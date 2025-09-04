package Controller;
import Model.Cliente;
import Model.ClienteDAO;
import java.util.List;

public class ClienteController {

    // Alta cliente (desde AltaClientePanel)
    public static boolean altaCliente(String nombre, String documento, String direccion, String telefono, String email) {
        Cliente nuevo = new Cliente(nombre, documento, direccion, telefono, email);
        return ClienteDAO.insertar(nuevo);
    }

    // Baja cliente (desde BajaClientePanel)
    public static boolean bajaCliente(int idCliente) {
        return ClienteDAO.eliminarPorId(idCliente);
    }

    // Listar clientes (desde ListaClientesPanel)
    public static List<Cliente> listarClientes() {
        return ClienteDAO.getTodos();
    }

    // Buscar cliente por ID (opcional, útil para editar/buscar)
    public static Cliente buscarClientePorId(int idCliente) {
        return ClienteDAO.buscarPorId(idCliente);
    }

    // Modificar cliente (opcional)
    public static boolean modificarCliente(Cliente cliente) {
        return ClienteDAO.modificar(cliente);
    }

    
}