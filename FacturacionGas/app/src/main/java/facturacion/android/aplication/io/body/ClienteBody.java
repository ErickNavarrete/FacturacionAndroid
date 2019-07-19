package facturacion.android.aplication.io.body;

public class ClienteBody {
    private String Nombre;
    private String apellido_materno;
    private String apellido_paterno;
    private String usuario;
    private String pass;
    private String nombre_razonsocial;
    private String RFC;
    private String email;

    public ClienteBody(String nombre, String apellido_materno, String apellido_paterno, String usuario, String pass, String nombre_razonsocial, String RFC, String email) {
        Nombre = nombre;
        this.apellido_materno = apellido_materno;
        this.apellido_paterno = apellido_paterno;
        this.usuario = usuario;
        this.pass = pass;
        this.nombre_razonsocial = nombre_razonsocial;
        this.RFC = RFC;
        this.email = email;
    }
}
