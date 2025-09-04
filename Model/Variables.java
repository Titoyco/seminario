package Model;

/**
 * Modelo para los datos de la tabla 'variables'
 */
public class Variables {
    private String password;
    private String masterPassword;

    public Variables(String password, String masterPassword) {
        this.password = password;
        this.masterPassword = masterPassword;
    }

    public String getPassword() {
        return password;
    }
    public String getMasterPassword() {
        return masterPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }
}