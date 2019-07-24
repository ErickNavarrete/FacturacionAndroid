package facturacion.android.aplication.io.response;

public class UserResponse {
    private String mensaje;
    private ClienteResponse usuarios;

    public String getMensaje() {
        return mensaje;
    }

    public ClienteResponse getUsuarios() {
        return usuarios;
    }
}
