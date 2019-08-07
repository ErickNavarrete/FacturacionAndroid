package facturacion.android.aplication.io;

import java.util.List;

import facturacion.android.aplication.io.body.ClienteBody;
import facturacion.android.aplication.io.body.FacturaBody;
import facturacion.android.aplication.io.response.ArticuloClass;
import facturacion.android.aplication.io.response.ClienteResponse;
import facturacion.android.aplication.io.response.EmpresaResponse;
import facturacion.android.aplication.io.response.FacturaResponse;
import facturacion.android.aplication.io.response.FacturasResponse;
import facturacion.android.aplication.io.response.TicketResponse;
import facturacion.android.aplication.io.response.UserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MyApiService {
    @GET("VentaGas")
    Call<TicketResponse> getTicket(
            @Query("ticket") String ticket,
            @Query("monto")double monto
    );

    @GET("Facturacion/Facturas_Det")
    Call<TicketResponse> getTicketFactura(
            @Query("id_factura") int id_factura
    );

    @GET("Facturacion/Print")
    Call<Void> getFacturaPrint(
            @Query("id_factura") int id_factura
    );

    @GET("Articulos")
    Call<ArticuloClass> getArticulos(
            @Query("Clave") String clave
    );

    @POST("Facturacion/Facturas")
    Call<FacturaResponse> createFactura(
            @Body FacturaBody facturaBody
    );

    @GET("Facturacion/Facturas")
    Call<List<FacturasResponse>> getFacturasRFC(
            @Query("RFC") String rfc
    );

    @GET("Facturacion/Facturas")
    Call<FacturaResponse> getFacturasID(
            @Query("id_factura") int id_factura
    );


    @GET("Cliente")
    Call<ClienteResponse> getCliente(
            @Query("usuario") String usuario,
            @Query("password") String password
    );

    @GET("Cliente")
    Call<ClienteResponse> getClienteCFDI(
            @Query("RFC") String RFC
    );

    @POST("Cliente")
    Call<UserResponse> createCliente(
            @Body ClienteBody clienteBody
    );

    @GET("Empresa")
    Call<EmpresaResponse> getEmpresa();

    @GET("Cliente/Usuarios")
    Call<ClienteResponse> getClienteUsuario(
            @Query("RFC") String RFC
    );

    @PUT("Cliente")
    Call<ClienteResponse> updateCliente(
            @Body ClienteResponse clienteResponse
    );
}
