package Model;

import java.time.LocalDate;
import java.util.List;

/**
 * Modelo para un crédito otorgado a un cliente.
 * Ahora incluye estado y loteOrigen para poder reflejar correctamente la información de la DB.
 */
public class Credito {
    private int id;
    private int idCliente;
    private double montoTotal;
    private double tasaInteres;     // Guardada como porcentaje (ej: 10.00 = 10%)
    private int cantidadCuotas;
    private LocalDate fechaOtorgamiento;
    private String estado;          // 'vigente','cancelado','mora'
    private int loteOrigen;         // Lote en el que se otorgó
    private List<Cuota> cuotas;     // Opcional

    // Constructor completo
    public Credito(int id, int idCliente, double montoTotal, double tasaInteres,
                   int cantidadCuotas, LocalDate fechaOtorgamiento,
                   String estado, int loteOrigen, List<Cuota> cuotas) {
        this.id = id;
        this.idCliente = idCliente;
        this.montoTotal = montoTotal;
        this.tasaInteres = tasaInteres;
        this.cantidadCuotas = cantidadCuotas;
        this.fechaOtorgamiento = fechaOtorgamiento;
        this.estado = estado;
        this.loteOrigen = loteOrigen;
        this.cuotas = cuotas;
    }

    // Constructor para creación (sin id ni lista de cuotas)
    public Credito(int idCliente, double montoTotal, double tasaInteres,
                   int cantidadCuotas, LocalDate fechaOtorgamiento) {
        this.idCliente = idCliente;
        this.montoTotal = montoTotal;
        this.tasaInteres = tasaInteres;
        this.cantidadCuotas = cantidadCuotas;
        this.fechaOtorgamiento = fechaOtorgamiento;
        this.estado = "vigente";
    }

    public int getId() { return id; }
    public int getIdCliente() { return idCliente; }
    public double getMontoTotal() { return montoTotal; }
    public double getTasaInteres() { return tasaInteres; }
    public int getCantidadCuotas() { return cantidadCuotas; }
    public LocalDate getFechaOtorgamiento() { return fechaOtorgamiento; }
    public String getEstado() { return estado; }
    public int getLoteOrigen() { return loteOrigen; }
    public List<Cuota> getCuotas() { return cuotas; }

    public void setId(int id) { this.id = id; }
    public void setCuotas(List<Cuota> cuotas) { this.cuotas = cuotas; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setLoteOrigen(int loteOrigen) { this.loteOrigen = loteOrigen; }

    @Override
    public String toString() {
        return "Credito #" + id +
                " Cliente=" + idCliente +
                " $" + montoTotal +
                " (" + cantidadCuotas + " cuotas) " +
                " Estado=" + estado +
                " Lote=" + loteOrigen;
    }
}