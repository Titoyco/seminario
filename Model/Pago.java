// Pago.java
// Modelo para un pago de una cuota.

package Model;

import java.time.LocalDate;

// Clase que representa un pago de una cuota con sus detalles.
public class Pago {
    private int id;
    private int idCuota;
    private LocalDate fechaPago;
    private double montoPagado;
    private String metodoPago;
    private String observaciones;

    // Constructores
    public Pago(int id, int idCuota, LocalDate fechaPago, double montoPagado, String metodoPago, String observaciones) {
        this.id = id;
        this.idCuota = idCuota;
        this.fechaPago = fechaPago;
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
        this.observaciones = observaciones;
    }

    // Constructor sin id para nuevos pagos
    public Pago(int idCuota, LocalDate fechaPago, double montoPagado, String metodoPago, String observaciones) {
        this.idCuota = idCuota;
        this.fechaPago = fechaPago;
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
        this.observaciones = observaciones;
    }

    // Getters
    public int getId() { return id; }
    public int getIdCuota() { return idCuota; }
    public LocalDate getFechaPago() { return fechaPago; }
    public double getMontoPagado() { return montoPagado; }
    public String getMetodoPago() { return metodoPago; }
    public String getObservaciones() { return observaciones; }
}