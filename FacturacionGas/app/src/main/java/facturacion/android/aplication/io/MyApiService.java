package facturacion.android.aplication.io;

import facturacion.android.aplication.io.body.ClienteBody;
import facturacion.android.aplication.io.body.FacturaBody;
import facturacion.android.aplication.io.response.ArticuloClass;
import facturacion.android.aplication.io.response.ClienteResponse;
import facturacion.android.aplication.io.response.FacturaResponse;
import facturacion.android.aplication.io.response.TicketResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyApiService {
    @GET("VentaGas")
    Call<TicketResponse> getTicket(
            @Query("ticket") String ticket,
            @Query("monto")double monto
    );

    @GET("Articulos")
    Call<ArticuloClass> getArticulos(
            @Query("Clave") String clave
    );

    @POST("Facturacion/Facturas")
    Call<FacturaResponse> createFactura(
            @Body FacturaBody facturaBody
    );

    @GET("Cliente")
    Call<ClienteResponse> getCliente(
            @Query("usuario") String usuario,
            @Query("password") String password
    );

    @POST("Cliente")
    Call<ClienteResponse> createCliente(
            @Body ClienteBody clienteBody
    );
}
