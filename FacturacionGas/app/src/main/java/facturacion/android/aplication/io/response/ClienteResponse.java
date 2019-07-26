package facturacion.android.aplication.io.response;

public class ClienteResponse {
    private int id_usuario;
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String usuario;
    private String pass;
    private String nombre_razonsocial;
    private String rfc;
    private String email;
    private String CFDI;

    public ClienteResponse(int id_usuario, String nombre, String apellido_paterno, String apellido_materno, String usuario, String pass, String nombre_razonsocial, String rfc, String email, String CFDI) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.usuario = usuario;
        this.pass = pass;
        this.nombre_razonsocial = nombre_razonsocial;
        this.rfc = rfc;
        this.email = email;
        this.CFDI = CFDI;
    }

    public String getCFDI() {
        return CFDI;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPass() {
        return pass;
    }

    public String getNombre_razonsocial() {
        return nombre_razonsocial;
    }

    public String getRfc() {
        return rfc;
    }

    public String getEmail() {
        return email;
    }
}
