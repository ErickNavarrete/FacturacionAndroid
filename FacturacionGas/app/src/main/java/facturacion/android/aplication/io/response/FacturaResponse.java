package facturacion.android.aplication.io.response;

import  java.io.Serializable;

public class FacturaResponse implements Serializable {
    private String uuid;
    private String serie_t;
    private String folio_t;

    public String getSerie_t() {
        return serie_t;
    }

    public String getFolio_t() {
        return folio_t;
    }

    public String getUuid() {
        return uuid;
    }

}
