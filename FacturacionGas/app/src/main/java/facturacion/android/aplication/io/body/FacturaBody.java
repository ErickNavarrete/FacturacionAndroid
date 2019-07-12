package facturacion.android.aplication.io.body;

public class FacturaBody {
    private String folio_venta;
    private String clave;
    private double volumen;
    private double monto;
    private double precio_unitario;
    private double ieps_gasolinas;
    private String clave_sat;
    private String u_med_sat;
    private String u_med_v;
    private int id_art;
    private String condiciones_pago;
    private String forma_pago;
    private String metodo_pago;
    private String cp_emisor;
    private String rfc_emisor;
    private String nombre_emisor;
    private String regimen_fiscal;
    private String rfc_cliente;
    private String nombre_cliente;
    private String uso_cfdi;

    public FacturaBody(String folio_venta, String clave, double volumen, double monto, double precio_unitario, double ieps_gasolinas, String clave_sat, String u_med_sat, String u_med_v, int id_art, String condiciones_pago, String forma_pago, String metodo_pago, String cp_emisor, String rfc_emisor, String nombre_emisor, String regimen_fiscal, String rfc_cliente, String nombre_cliente, String uso_cfdi) {
        this.folio_venta = folio_venta;
        this.clave = clave;
        this.volumen = volumen;
        this.monto = monto;
        this.precio_unitario = precio_unitario;
        this.ieps_gasolinas = ieps_gasolinas;
        this.clave_sat = clave_sat;
        this.u_med_sat = u_med_sat;
        this.u_med_v = u_med_v;
        this.id_art = id_art;
        this.condiciones_pago = condiciones_pago;
        this.forma_pago = forma_pago;
        this.metodo_pago = metodo_pago;
        this.cp_emisor = cp_emisor;
        this.rfc_emisor = rfc_emisor;
        this.nombre_emisor = nombre_emisor;
        this.regimen_fiscal = regimen_fiscal;
        this.rfc_cliente = rfc_cliente;
        this.nombre_cliente = nombre_cliente;
        this.uso_cfdi = uso_cfdi;
    }
}
