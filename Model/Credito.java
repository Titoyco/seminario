package Model;

import java.time.LocalDate;
import java.util.List;

/**
 * Modelo para un crédito otorgado a un cliente. 
 */
public class Credito {
    private int id;
    private int idCliente;
    private double montoTotal;
    private double tasaInteres;
    private int cantidadCuotas;
    private LocalDate fechaOtorgamiento;
    private List<Cuota> cuotas; // Opcional: lista de cuotas asociadas

    // Constructor completo
    public Credito(int id, int idCliente, double montoTotal, double tasaInteres, int cantidadCuotas, LocalDate fechaOtorgamiento, List<Cuota> cuotas) {
        this.id = id;
        this.idCliente = idCliente;
        this.montoTotal = montoTotal;
        this.tasaInteres = tasaInteres;
        this.cantidadCuotas = cantidadCuotas;
        this.fechaOtorgamiento = fechaOtorgamiento;
        this.cuotas = cuotas;
    }

    // Constructor sin id ni cuotas (para crear nuevos créditos)
    public Credito(int idCliente, double montoTotal, double tasaInteres, int cantidadCuotas, LocalDate fechaOtorgamiento) {
        this.idCliente = idCliente;
        this.montoTotal = montoTotal;
        this.tasaInteres = tasaInteres;
        this.cantidadCuotas = cantidadCuotas;
        this.fechaOtorgamiento = fechaOtorgamiento;
    }

    // Getters
    public int getId() { return id; }
    public int getIdCliente() { return idCliente; }
    public double getMontoTotal() { return montoTotal; }
    public double getTasaInteres() { return tasaInteres; }
    public int getCantidadCuotas() { return cantidadCuotas; }
    public LocalDate getFechaOtorgamiento() { return fechaOtorgamiento; }
    public List<Cuota> getCuotas() { return cuotas; }

    // Setters
    public void setCuotas(List<Cuota> cuotas) { this.cuotas = cuotas; }
    public void setId(int id) { this.id = id; }

    // Para mostrar en listas/tablas
    @Override
    public String toString() {
        return "Crédito #" + id + " - Cliente: " + idCliente + " - $" + montoTotal + " - " + cantidadCuotas + " cuotas";
    }
}