package facturacion.android.aplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.response.ClienteResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scrLogin extends AppCompatActivity {

    private MaterialButton btnIniciar;
    private MaterialButton btnRegistro;
    private TextInputEditText tbUsuario;
    private TextInputEditText tbPass;
    private SwitchMaterial switchRemember;

    private SharedPreferences prefs;
    private ClienteResponse cliente;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_login);
        setToolbar();

        btnIniciar = findViewById(R.id.btnIniciar);
        btnRegistro= findViewById(R.id.btnRegistro);
        tbUsuario = findViewById(R.id.tbUsuario);
        tbPass = findViewById(R.id.tbPass);
        switchRemember = findViewById(R.id.swRecuerda);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        ifCredentialsExist();

        btnIniciar.setOnClickListener(view -> {
            closeKeyboard();
            progressDialog = new ProgressDialog(scrLogin.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            if(!checkInputs()){
                progressDialog.cancel();
                return;
            }
            Call<ClienteResponse> call = MyApiAdapter.getApiService().getCliente(tbUsuario.getText().toString(), tbPass.getText().toString());
            call.enqueue(new Callback<ClienteResponse>() {
                @Override
                public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> response) {
                    if(response.code() == 200){
                        cliente = response.body();
                        saveOnPreferences();

                        progressDialog.cancel();

                        Intent intent = new Intent(scrLogin.this,scrMenu.class);
                        saveUser(cliente.getNombre(),cliente.getRfc(),cliente.getEmail());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    if(response.code() == 404){
                        progressDialog.cancel();
                        tbUsuario.setError("Usuario y/o contraseña incorrectos");
                        tbUsuario.requestFocus();
                    }
                    if(response.code() >= 500){
                        progressDialog.cancel();
                        AlertDialog.Builder alert = new AlertDialog.Builder(scrLogin.this);
                        alert.setMessage("No fue posible conectarse con el servidor, intente de nuevo")
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());

                        AlertDialog title = alert.create();
                        title.show();
                    }
                }

                @Override
                public void onFailure(Call<ClienteResponse> call, Throwable t) {
                    progressDialog.cancel();
                    Toast toast = Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        });

        btnRegistro.setOnClickListener(view -> {
            Intent intent = new Intent(scrLogin.this,Registro.class);
            startActivity(intent);
        });
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

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void saveOnPreferences(){
        if(switchRemember.isChecked()){
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("Recordar","SI");
            edit.apply();
        }
    }

    private void saveUser(String nombre, String RFC, String mail){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("Nombre",nombre);
        edit.putString("RFC",RFC);
        edit.putString("Mail",mail);
        edit.apply();
    }

    private void ifCredentialsExist(){
        String RFC = prefs.getString("Recordar","");
        if(!TextUtils.isEmpty(RFC)){
            Intent intent = new Intent(scrLogin.this,scrMenu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean checkInputs(){
        if(TextUtils.isEmpty(tbUsuario.getText().toString())){
            tbUsuario.setError("Campo obligatorio");
            tbUsuario.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(tbPass.getText().toString())){
            tbPass.setError("Campo obligatorio");
            tbPass.requestFocus();
            return false;
        }
        return true;
    }
}
