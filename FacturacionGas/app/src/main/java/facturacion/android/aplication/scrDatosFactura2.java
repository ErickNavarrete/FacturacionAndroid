package facturacion.android.aplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.response.FacturaResponse;
import facturacion.android.aplication.io.response.TicketResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scrDatosFactura2 extends AppCompatActivity {
    private TextInputEditText tbSerie;
    private TextInputEditText tbFolio;
    private TextInputEditText tbUUID;
    private TextInputEditText tbSubT;
    private TextInputEditText tbTotal;
    private TextInputEditText tbFecha;
    private TextInputEditText tbProducto;
    private TextInputEditText tbVolumen;
    private FacturaResponse facturaResponse;
    private TicketResponse ticket;
    private ProgressDialog progressDialog;
    private Button btnReenvio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_datos_factura2);
        setToolbar();

        tbSerie = findViewById(R.id.tbSerie);
        tbFolio = findViewById(R.id.tbFolio);
        tbUUID = findViewById(R.id.tbUUID);
        tbSubT = findViewById(R.id.tbSubT);
        tbTotal = findViewById(R.id.tbTotal);
        tbFecha = findViewById(R.id.tbFecha);
        tbProducto = findViewById(R.id.tbProducto);
        tbVolumen = findViewById(R.id.tbVolumen);
        btnReenvio = findViewById(R.id.btnReenvio);

        tbSerie.setEnabled(false);
        tbFolio.setEnabled(false);
        tbUUID.setEnabled(false);
        tbSubT.setEnabled(false);
        tbTotal.setEnabled(false);
        tbFecha.setEnabled(false);
        tbProducto.setEnabled(false);
        tbVolumen.setEnabled(false);

        Intent intent = getIntent();
        int id_factura = intent.getIntExtra("intVariableName", 0);

        progressDialog = new ProgressDialog(scrDatosFactura2.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<FacturaResponse> call = MyApiAdapter.getApiService().getFacturasID(id_factura);
        call.enqueue(new Callback<FacturaResponse>() {
            @Override
            public void onResponse(Call<FacturaResponse> call, Response<FacturaResponse> response) {
                if(response.isSuccessful()){
                    progressDialog.cancel();

                    facturaResponse = response.body();
                    tbSerie.setText(facturaResponse.getSerie_t());
                    tbFolio.setText(facturaResponse.getFolio_t());
                    tbUUID.setText(facturaResponse.getUuid());
                    tbSubT.setText(String.valueOf(facturaResponse.getSub_t()));
                    tbTotal.setText(String.valueOf(facturaResponse.getTotal_t()));
                    String[] date = facturaResponse.getFecha_t().split("T");
                    tbFecha.setText(date[0]);

                }
            }

            @Override
            public void onFailure(Call<FacturaResponse> call, Throwable t) {
                progressDialog.cancel();
                Toast toast = Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG);
                toast.show();
            }
        });

        Call<TicketResponse> call2 = MyApiAdapter.getApiService().getTicketFactura(id_factura);
        call2.enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                if(response.isSuccessful()){
                    ticket = response.body();
                    tbVolumen.setText(String.valueOf(ticket.getVolumen()));
                    tbProducto.setText(ticket.getProd_nombre());
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {

            }
        });

        btnReenvio.setOnClickListener(view -> {
            progressDialog = new ProgressDialog(scrDatosFactura2.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Call<Void> call3 = MyApiAdapter.getApiService().getFacturaPrint(id_factura);
            call3.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    progressDialog.cancel();
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Envío relaizado con éxito",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Algo salío mal, intente más tarde",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    progressDialog.cancel();
                    Toast toast = Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG);
                    toast.show();
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
                Intent intent = new Intent(scrDatosFactura2.this,scrMenu.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
