// Model/Cliente.java
// Modelo para la entidad Cliente

package Model;

// Clase Cliente con atributos básicos
public class Cliente {
    private int id;
    private String nombre;
    private String documento;
    private String direccion;
    private String telefono;
    private String email;

    // Constructor completo
    public Cliente(int id, String nombre, String documento, String direccion, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    // Constructor sin ID (para nuevos clientes)
    public Cliente(String nombre, String documento, String direccion, String telefono, String email) {
        this.nombre = nombre;
        this.documento = documento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }


    // Método para mostrar el nombre e ID en el JComboBox
    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
}