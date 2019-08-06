package facturacion.android.aplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import facturacion.android.aplication.Recycler.MyAdapter;
import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.response.FacturasResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scrMisFacturas extends AppCompatActivity {
    private List<FacturasResponse> facturas;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_mis_facturas);
        setToolbar();
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(scrMisFacturas.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Call<List<FacturasResponse>> call = MyApiAdapter.getApiService().getFacturasRFC(getRFC());
        call.enqueue(new Callback<List<FacturasResponse>>() {
            @Override
            public void onResponse(Call<List<FacturasResponse>> call, Response<List<FacturasResponse>> response) {
                if(response.isSuccessful()){
                    progressDialog.cancel();
                    facturas = response.body();
                    mRecyclerView = findViewById(R.id.recyclerView);
                    mLayoutManager = new LinearLayoutManager(scrMisFacturas.this);

                    mAdapter = new MyAdapter(facturas, R.layout.recycler_view_item, (factura, position) -> {
                        Intent myIntent = new Intent(scrMisFacturas.this, scrDatosFactura2.class);
                        myIntent.putExtra("intVariableName", factura.getId_factura());
                        startActivity(myIntent);
                    });

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<FacturasResponse>> call, Throwable t) {
                progressDialog.cancel();
                Toast toast = Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG);
                toast.show();
            }
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
                Intent intent = new Intent(scrMisFacturas.this,scrMenu.class);
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
}
