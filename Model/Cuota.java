package Model;

/**
 * Modelo adaptado al esquema actual:
 * Tabla cuotas: id, id_credito, numero, monto, estado (pendiente|pagada|mora)
 * No existe fecha_vencimiento ni campo pagada en DB.
 * El vencimiento real se deriva: lote_vencimiento = credito.lote_origen + numero.
 */
public class Cuota {
    private int id;
    private int idCredito;
    private int numero;
    private double monto;
    private String estado; // 'pendiente','pagada','mora'

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

    public int getId() { return id; }
    public int getIdCredito() { return idCredito; }
    public int getNumero() { return numero; }
    public double getMonto() { return monto; }
    public String getEstado() { return estado; }

    public boolean isPagada() { return "pagada".equalsIgnoreCase(estado); }
    public boolean isMora() { return "mora".equalsIgnoreCase(estado); }
    public boolean isPendiente() { return "pendiente".equalsIgnoreCase(estado); }

    public void setEstado(String estado) { this.estado = estado; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return "Cuota #" + numero + " $" + monto + " [" + estado + "]";
    }

    // Deriva el lote de vencimiento en base al lote_origen del crédito:
    public int loteVencimiento(int loteOrigenCredito) {
        return loteOrigenCredito + numero;
    }
}