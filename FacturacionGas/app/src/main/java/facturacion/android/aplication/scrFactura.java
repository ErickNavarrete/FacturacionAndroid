package facturacion.android.aplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.body.FacturaBody;
import facturacion.android.aplication.io.response.ArticuloClass;
import facturacion.android.aplication.io.response.FacturaResponse;
import facturacion.android.aplication.io.response.TicketResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scrFactura extends AppCompatActivity {

    ArticuloClass articuloClass;
    FacturaResponse facturaResponse;
    com.google.android.material.textfield.TextInputEditText tbTicket;
    com.google.android.material.textfield.TextInputEditText tbClave;
    com.google.android.material.textfield.TextInputEditText tbFecha;
    com.google.android.material.textfield.TextInputEditText tbVolumen;
    com.google.android.material.textfield.TextInputEditText tbPrecio;
    Button btnFactura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_factura);

        tbTicket = findViewById(R.id.tbTicket);
        tbClave = findViewById(R.id.tbClave);
        tbFecha = findViewById(R.id.tbFecha);
        tbVolumen = findViewById(R.id.tbVolumen);
        tbPrecio = findViewById(R.id.tbPrecio);
        btnFactura = findViewById(R.id.btnFactura);

        tbTicket.setEnabled(false);
        tbClave.setEnabled(false);
        tbFecha.setEnabled(false);
        tbVolumen.setEnabled(false);
        tbPrecio.setEnabled(false);

        Intent intent = getIntent();
        final TicketResponse ticketResponse = (TicketResponse) intent.getSerializableExtra("MyClass");

        Call<ArticuloClass> call = MyApiAdapter.getApiService().getArticulos(ticketResponse.getProd_nombre());
        call.enqueue(new Callback<ArticuloClass>() {
            @Override
            public void onResponse(Call<ArticuloClass> call, Response<ArticuloClass> response) {
                if(response.isSuccessful()){
                    articuloClass = response.body();

                    tbTicket.setText(ticketResponse.getId_venta());
                    tbClave.setText(articuloClass.getClave());
                    tbFecha.setText(ticketResponse.getVen_fecha());
                    tbVolumen.setText(String.valueOf(ticketResponse.getVolumen()));
                    tbPrecio.setText(String.valueOf(ticketResponse.getMonto()));
                }
            }

            @Override
            public void onFailure(Call<ArticuloClass> call, Throwable t) {
                Log.i("Error",t.getMessage());
            }
        });

        btnFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacturaBody facturaBody = new FacturaBody(
                        ticketResponse.getId_venta(),
                        articuloClass.getClave(),
                        ticketResponse.getVolumen(),
                        ticketResponse.getMonto(),
                        ticketResponse.getPrecio(),
                        articuloClass.getIeps_gasolinas(),
                        articuloClass.getClave_sat(),
                        articuloClass.getU_med_sat(),
                        articuloClass.getU_med_v(),
                        articuloClass.getId_art(),
                        "CONTADO",
                        "01",
                        "PUE",
                        "72760",
                        "AAA010101AAA",
                        "D.M.M. DISPOSITIVOS Y MAQUINADOS MEXICANOS, S.A. DE C.V.",
                        "601",
                        "AAA010101AAA",
                        "Cliente de prueba SA de CV",
                        "G01"
                );


                Call<FacturaResponse> call = MyApiAdapter.getApiService().createFactura(facturaBody);
                call.enqueue(new Callback<FacturaResponse>() {
                    @Override
                    public void onResponse(Call<FacturaResponse> call, Response<FacturaResponse> response) {
                        if(response.isSuccessful()){
                            facturaResponse = response.body();

                            Log.i("EXITO", "onResponse: 'EXITO UUID:" + facturaResponse.getUuid());
                        }
                    }

                    @Override
                    public void onFailure(Call<FacturaResponse> call, Throwable t) {
                        Log.i("ERROR", "Algo salió mal" + t.toString());
                    }
                });
            }
        });
    }


}
