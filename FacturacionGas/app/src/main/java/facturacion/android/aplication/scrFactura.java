package facturacion.android.aplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.body.FacturaBody;
import facturacion.android.aplication.io.response.ArticuloClass;
import facturacion.android.aplication.io.response.ClienteResponse;
import facturacion.android.aplication.io.response.EmpresaResponse;
import facturacion.android.aplication.io.response.FacturaResponse;
import facturacion.android.aplication.io.response.TicketResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scrFactura extends AppCompatActivity {

    private ArticuloClass articuloClass;
    private FacturaResponse facturaResponse;
    private ClienteResponse clienteResponse;
    private EmpresaResponse empresaResponse;
    private String CFDI;
    private String errorResponse;

    private TextInputEditText tbTicket;
    private TextInputEditText tbClave;
    private TextInputEditText tbFecha;
    private TextInputEditText tbVolumen;
    private TextInputEditText tbPrecio;
    private Button btnFactura;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_factura);
        setToolbar();

        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

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
            }
        });

        Call<ClienteResponse> cliente_call = MyApiAdapter.getApiService().getClienteCFDI(getRFC());
        cliente_call.enqueue(new Callback<ClienteResponse>() {
            @Override
            public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> response) {
                if(response.isSuccessful()){
                    clienteResponse = response.body();
                    CFDI = clienteResponse.getCFDI();
                }else{
                    CFDI = "";
                }
                Log.i("Prueba", CFDI);
            }

            @Override
            public void onFailure(Call<ClienteResponse> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });

        Call<EmpresaResponse> empresa_call = MyApiAdapter.getApiService().getEmpresa();
        empresa_call.enqueue(new Callback<EmpresaResponse>() {
            @Override
            public void onResponse(Call<EmpresaResponse> call, Response<EmpresaResponse> response) {
                if (response.isSuccessful()){
                    empresaResponse = response.body();
                }
            }

            @Override
            public void onFailure(Call<EmpresaResponse> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });

        btnFactura.setOnClickListener(view -> {
            progressDialog = new ProgressDialog(scrFactura.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();

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
                    "PUE | Pago en una sola exhibición",
                    "01 | Efectivo",
                    "PUE | Pago en una sola exhibición",
                    empresaResponse.getCp(),
                    empresaResponse.getRfc(),
                    empresaResponse.getEmpresa1(),
                    empresaResponse.getClave(),
                    getRFC(),
                    getNombre(),
                    CFDI
            );

            Call<FacturaResponse> call1 = MyApiAdapter.getApiService().createFactura(facturaBody);
            call1.enqueue(new Callback<FacturaResponse>() {
                @Override
                public void onResponse(Call<FacturaResponse> call1, Response<FacturaResponse> response) {
                    if(response.isSuccessful()){
                        facturaResponse = response.body();
                        progressDialog.cancel();

                        AlertDialog.Builder alerta = new AlertDialog.Builder(scrFactura.this);
                        alerta.setMessage("Factura realizada con éxito")
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialogInterface, i) -> {
                                    Intent intent = new Intent(scrFactura.this,scrDatosFactura.class);
                                    intent.putExtra("Factura",facturaResponse);
                                    startActivity(intent);
                                });

                        AlertDialog titulo = alerta.create();
                        titulo.show();
                    }else{
                        progressDialog.cancel();

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorResponse = jObjError.getString("Message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        AlertDialog.Builder alerta = new AlertDialog.Builder(scrFactura.this);
                        alerta.setMessage(errorResponse)
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Error");
                        titulo.show();
                    }
                }

                @Override
                public void onFailure(Call<FacturaResponse> call1, Throwable t) {
                }
            });
        });
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(scrFactura.this,scrMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getRFC(){
        String RFC = preferences.getString("RFC","");
        return RFC;
    }
    private String getNombre(){
        String Nombre = preferences.getString("Nombre","");
        return Nombre;
    }

}
