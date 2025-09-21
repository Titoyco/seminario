package Model;

import java.time.LocalDate;

public class Lote {
    private int nroLote;
    private LocalDate fechaApertura;
    private LocalDate fechaCierre;

    public Lote(int nroLote, LocalDate fechaApertura, LocalDate fechaCierre) {
        this.nroLote = nroLote;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
    }

    public int getNroLote() { return nroLote; }
    public LocalDate getFechaApertura() { return fechaApertura; }
    public LocalDate getFechaCierre() { return fechaCierre; }

    public boolean estaCerrado() { return fechaCierre != null; }
}