package Model;

import java.time.LocalDate;

/**
 * Modelo para una cuota de un crédito.
 */
public class Cuota {
    private int id;
    private int idCredito;
    private int nroCuota;
    private double monto;
    private LocalDate fechaVencimiento;
    private boolean pagada;

    // Constructor completo
    public Cuota(int id, int idCredito, int nroCuota, double monto, LocalDate fechaVencimiento, boolean pagada) {
        this.id = id;
        this.idCredito = idCredito;
        this.nroCuota = nroCuota;
        this.monto = monto;
        this.fechaVencimiento = fechaVencimiento;
        this.pagada = pagada;
    }

    // Constructor sin id (usado para crear nuevas cuotas)
    public Cuota(int idCredito, int nroCuota, double monto, LocalDate fechaVencimiento, boolean pagada) {
        this.idCredito = idCredito;
        this.nroCuota = nroCuota;
        this.monto = monto;
        this.fechaVencimiento = fechaVencimiento;
        this.pagada = pagada;
    }

    // Getters
    public int getId() { return id; }
    public int getIdCredito() { return idCredito; }
    public int getNroCuota() { return nroCuota; }
    public double getMonto() { return monto; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public boolean isPagada() { return pagada; }

    // Setters
    public void setPagada(boolean pagada) { this.pagada = pagada; }
    public void setId(int id) { this.id = id; }

    // Para mostrar en listas/tablas
    @Override
    public String toString() {
        return "Cuota #" + nroCuota + " - $" + monto + " - Vence: " + fechaVencimiento + (pagada ? " (Pagada)" : " (Pendiente)");
    }
}