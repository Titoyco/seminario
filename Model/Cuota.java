// Model/Cuota.java
// Modelo para una cuota de un crédito otorgado.

package Model;

// Clase Cuota con atributos principales
public class Cuota {
    private int id;
    private int idCredito;
    private int numero;
    private double monto;
    private String estado; // 'pendiente','pagada','mora'

    // Constructor completo
    public Cuota(int id, int idCredito, int numero, double monto, String estado) {
        this.id = id;
        this.idCredito = idCredito;
        this.numero = numero;
        this.monto = monto;
        this.estado = estado;
    }

    // Constructor para crear antes de insertar
    public Cuota(int idCredito, int numero, double monto) {
        this.idCredito = idCredito;
        this.numero = numero;
        this.monto = monto;
        this.estado = "pendiente";
    }

    // Getters
    public int getId() { return id; }
    public int getIdCredito() { return idCredito; }
    public int getNumero() { return numero; }
    public double getMonto() { return monto; }
    public String getEstado() { return estado; }

    // Métodos de conveniencia para estado
    public boolean isPagada() { return "pagada".equalsIgnoreCase(estado); }
    public boolean isMora() { return "mora".equalsIgnoreCase(estado); }
    public boolean isPendiente() { return "pendiente".equalsIgnoreCase(estado); }

    // Setters
    public void setEstado(String estado) { this.estado = estado; }
    public void setId(int id) { this.id = id; }

    // toString para mostrar información básica
    @Override
    public String toString() {
        return "Cuota #" + numero + " $" + monto + " [" + estado + "]";
    }

    // Obtiene el lote de vencimiento en base al lote_origen del crédito:
    public int loteVencimiento(int loteOrigenCredito) {
        return loteOrigenCredito + numero;
    }
}