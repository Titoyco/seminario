// Lote.java
// Modelo para un lote de créditos.

package Model;

import java.time.LocalDate;

//  Clase que representa un lote de créditos con su número, fecha de apertura y fecha de cierre.
public class Lote {
    private int nroLote;
    private LocalDate fechaApertura;
    private LocalDate fechaCierre;

    // Constructor
    public Lote(int nroLote, LocalDate fechaApertura, LocalDate fechaCierre) {
        this.nroLote = nroLote;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
    }

    // Getters
    public int getNroLote() { return nroLote; }
    public LocalDate getFechaApertura() { return fechaApertura; }
    public LocalDate getFechaCierre() { return fechaCierre; }
    
    // Método para verificar si el lote está cerrado
    public boolean estaCerrado() { return fechaCierre != null; }
}