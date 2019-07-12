package facturacion.android.aplication.io.response;

import  java.io.Serializable;

public class TicketResponse implements Serializable {
    private String id_venta;
    private String prod_nombre;
    private double volumen;
    private double monto;
    private double precio;
    private String ven_fecha;

    public String getId_venta() {
        return id_venta;
    }

    public void setId_venta(String id_venta) {
        this.id_venta = id_venta;
    }

    public String getProd_nombre() {
        return prod_nombre;
    }

    public void setProd_nombre(String prod_nombre) {
        this.prod_nombre = prod_nombre;
    }

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getVen_fecha() {
        return ven_fecha;
    }

    public void setVen_fecha(String ven_fecha) {
        this.ven_fecha = ven_fecha;
    }
}
