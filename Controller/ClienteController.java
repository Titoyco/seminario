// ClienteController.java
// Controlador para gestionar las operaciones relacionadas con los clientes.

package Controller;
import Model.Cliente;

import java.util.List;

import Dao.ClienteDAO;
import Dao.CreditoDAO;

public class ClienteController {

    // Alta cliente (desde AltaClientePanel)
    public static boolean altaCliente(String nombre, String documento, String direccion, String telefono, String email) {
        Cliente nuevo = new Cliente(nombre, documento, direccion, telefono, email);
        return ClienteDAO.insertar(nuevo);
    }

    // Baja cliente (desde BajaClientePanel)
    public static boolean bajaCliente(int idCliente) {
        // Política: NO permitir la eliminación si existe cualquier registro asociado (crédito),
        // independientemente de su estado. Se verifica la existencia de créditos vinculados.
        if (CreditoDAO.existeCreditosPorCliente(idCliente)) {
            // No se puede eliminar el cliente si tiene créditos asociados
            System.out.println("Error: No se puede eliminar el cliente con créditos asociados.");
            return false;
        }
        return ClienteDAO.eliminarPorId(idCliente);
    }

    // Listar clientes (desde ListaClientesPanel)
    public static List<Cliente> listarClientes() {
        return ClienteDAO.getTodos();
    }

    // Buscar cliente por ID
    public static Cliente buscarClientePorId(int idCliente) {
        return ClienteDAO.buscarPorId(idCliente);
    }

    // Modificar cliente
    public static boolean modificarCliente(Cliente cliente) {
        return ClienteDAO.modificar(cliente);
    }

    
}