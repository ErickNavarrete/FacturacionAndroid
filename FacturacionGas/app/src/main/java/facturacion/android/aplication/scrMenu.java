package facturacion.android.aplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.response.TicketResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class scrMenu extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextInputEditText tbTicket;
    private TextInputEditText tbMonto;
    private TextView lbNombre;
    private Button btnBuscar;

    private String ticket;
    private double monto;
    private String Nombre;

    private TicketResponse ticketResponse;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_menu);
        setToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navview);
        btnBuscar = findViewById(R.id.btnBuscar);
        tbTicket = findViewById(R.id.tbTicket);
        tbMonto =  findViewById(R.id.tbMonto);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        getNombre();

        btnBuscar.setOnClickListener(view -> {
            ticket = tbTicket.getText().toString();
            monto = Double.valueOf(tbMonto.getText().toString());

            Call<TicketResponse> call =  MyApiAdapter.getApiService().getTicket(ticket,monto);
            call.enqueue(new Callback<TicketResponse>() {
                @Override
                public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                    if(response.isSuccessful()){
                        ticketResponse = response.body();

                        Intent intent = new Intent(scrMenu.this,scrFactura.class);
                        intent.putExtra("MyClass",ticketResponse);
                        startActivity(intent);

                    }else{
                        AlertDialog.Builder alerta = new AlertDialog.Builder(scrMenu.this);
                        alerta.setMessage("Ticket no encontrado, verifique sus datos")
                                .setCancelable(true)
                                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Error");
                        titulo.show();
                    }
                }

                @Override
                public void onFailure(Call<TicketResponse> call, Throwable t) {
                    Log.i("Error",t.getMessage());
                }
            });
        });

        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.btnDatos:
                    break;
                case R.id.btnFacturas:
                    break;
                case R.id.btnSesion:
                    closeSesion();
                    break;
            }

            return false;
        });
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_reorder_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:

                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void closeSesion(){
        prefs.edit().clear().apply();
        Intent intent = new Intent(scrMenu.this,scrLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getNombre(){
        String nombre_p = prefs.getString("Nombre","");
        View headerView = navigationView.getHeaderView(0);
        lbNombre = headerView.findViewById(R.id.lbNombre);
        lbNombre.setText(nombre_p);
    }
}
