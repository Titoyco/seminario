package Model;

/**
 * Modelo para la única fila de la tabla 'variables'.
 * Supone:
 *  - interesMensual se guarda como decimal (ej: 0.05 = 5%)
 *  - nroCredito y nroLote se usan como contadores / estado actual.
 */
public class Variables {
    private String password;
    private String masterPassword;
    private int nroCredito;
    private int nroLote;
    private double interesMensual;

    public Variables(String password, String masterPassword, int nroCredito, int nroLote, double interesMensual) {
        this.password = password;
        this.masterPassword = masterPassword;
        this.nroCredito = nroCredito;
        this.nroLote = nroLote;
        this.interesMensual = interesMensual;
    }

    public String getPassword() { return password; }
    public String getMasterPassword() { return masterPassword; }
    public int getNroCredito() { return nroCredito; }
    public int getNroLote() { return nroLote; }
    public double getInteresMensual() { return interesMensual; }

    public void setPassword(String password) { this.password = password; }
    public void setMasterPassword(String masterPassword) { this.masterPassword = masterPassword; }
    public void setNroCredito(int nroCredito) { this.nroCredito = nroCredito; }
    public void setNroLote(int nroLote) { this.nroLote = nroLote; }
    public void setInteresMensual(double interesMensual) { this.interesMensual = interesMensual; }
}