package facturacion.android.aplication.io.response;

import  java.io.Serializable;

public class FacturaResponse implements Serializable {
    private String uuid;
    private String serie_t;
    private String folio_t;
    private float sub_t;
    private float total_t;
    private String fecha_t;
    private String id_venta_gas;

    public String getSerie_t() {
        return serie_t;
    }

    public String getFolio_t() {
        return folio_t;
    }

    public float getSub_t() {
        return sub_t;
    }

    public float getTotal_t() {
        return total_t;
    }

    public String getFecha_t() {
        return fecha_t;
    }

    public String getUuid() {
        return uuid;
    }

    public String getId_venta_gas() {
        return id_venta_gas;
    }
}
