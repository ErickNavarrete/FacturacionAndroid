package facturacion.android.aplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

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

    private TicketResponse ticketResponse;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;

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
            closeKeyboard();
            progressDialog = new ProgressDialog(scrMenu.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if(!checkInputs()){
                progressDialog.cancel();
                return;
            }

            ticket = tbTicket.getText().toString();
            monto = Double.valueOf(tbMonto.getText().toString());

            Call<TicketResponse> call =  MyApiAdapter.getApiService().getTicket(ticket,monto);
            call.enqueue(new Callback<TicketResponse>() {
                @Override
                public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                    if(response.isSuccessful()){
                        ticketResponse = response.body();
                        progressDialog.cancel();
                        Intent intent = new Intent(scrMenu.this,scrFactura.class);
                        intent.putExtra("MyClass",ticketResponse);
                        startActivity(intent);

                    }else{
                        progressDialog.cancel();
                        if(response.code() == 409){
                            AlertDialog.Builder alerta = new AlertDialog.Builder(scrMenu.this);
                            alerta.setMessage("Ticket ya facturado")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("Error");
                            titulo.show();
                        }
                        if(response.code() == 404){
                            AlertDialog.Builder alerta = new AlertDialog.Builder(scrMenu.this);
                            alerta.setMessage("Ticket no encontrado, verifique sus datos")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("Error");
                            titulo.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TicketResponse> call, Throwable t) {
                    progressDialog.cancel();
                    AlertDialog.Builder alert = new AlertDialog.Builder(scrMenu.this);
                    alert.setMessage("No fue posible conectarse con el servidor, intente de nuevo")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());

                    AlertDialog title = alert.create();
                    title.show();
                }
            });
        });
        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.btnDatos:
                    Intent intent = new Intent(scrMenu.this,scrDatosCliente.class);
                    startActivity(intent);
                    break;
                case R.id.btnFacturas:
                    Intent intent2 = new Intent(scrMenu.this,scrMisFacturas.class);
                    startActivity(intent2);
                    break;
                case R.id.btnSesion:
                    closeSesion();
                    break;
            }

            return false;
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                closeKeyboard();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                closeKeyboard();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
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

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean checkInputs(){
        if(TextUtils.isEmpty(tbTicket.getText().toString())){
            tbTicket.setError("Campo obligatorio");
            tbTicket.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(tbMonto.getText().toString())){
            tbMonto.setError("Campo obligatorio");
            tbMonto.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiStateExtra){
                case WifiManager.WIFI_STATE_DISABLED:
                    Toast toast2 = Toast.makeText(getApplicationContext(),"Conexión perdida",Toast.LENGTH_LONG);
                    toast2.show();
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    Toast toast = Toast.makeText(getApplicationContext(),"Conexión establecida",Toast.LENGTH_LONG);
                    toast.show();
                    break;
            }
        }
    };
}
