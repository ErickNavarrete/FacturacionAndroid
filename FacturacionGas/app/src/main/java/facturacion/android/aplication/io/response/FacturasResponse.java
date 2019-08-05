package facturacion.android.aplication.io.response;

public class FacturasResponse {
    private int id_factura;
    private String datos;

    public FacturasResponse() {
    }

    public FacturasResponse(int id_factura, String datos) {
        this.id_factura = id_factura;
        this.datos = datos;
    }

    public int getId_factura() {
        return id_factura;
    }

    public String getDatos() {
        return datos;
    }
}
