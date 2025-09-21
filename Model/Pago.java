package Model;

import java.time.LocalDate;

public class Pago {
    private int id;
    private int idCuota;
    private LocalDate fechaPago;
    private double montoPagado;
    private String metodoPago;
    private String observaciones;

    public Pago(int id, int idCuota, LocalDate fechaPago, double montoPagado, String metodoPago, String observaciones) {
        this.id = id;
        this.idCuota = idCuota;
        this.fechaPago = fechaPago;
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
        this.observaciones = observaciones;
    }

    public Pago(int idCuota, LocalDate fechaPago, double montoPagado, String metodoPago, String observaciones) {
        this.idCuota = idCuota;
        this.fechaPago = fechaPago;
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public int getIdCuota() { return idCuota; }
    public LocalDate getFechaPago() { return fechaPago; }
    public double getMontoPagado() { return montoPagado; }
    public String getMetodoPago() { return metodoPago; }
    public String getObservaciones() { return observaciones; }
}