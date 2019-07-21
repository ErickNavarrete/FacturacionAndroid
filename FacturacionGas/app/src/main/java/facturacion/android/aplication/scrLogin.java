package facturacion.android.aplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.material.switchmaterial.SwitchMaterial;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.response.ClienteResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scrLogin extends AppCompatActivity {

    com.google.android.material.button.MaterialButton btnIniciar;
    com.google.android.material.button.MaterialButton btnRegistro;
    com.google.android.material.textfield.TextInputEditText tbUsuario;
    com.google.android.material.textfield.TextInputEditText tbPass;

    private SwitchMaterial switchRemember;
    private SharedPreferences prefs;
    private SharedPreferences nombre_u;

    ClienteResponse cliente;

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
            Call<ClienteResponse> call = MyApiAdapter.getApiService().getCliente(tbUsuario.getText().toString(), tbPass.getText().toString());
            call.enqueue(new Callback<ClienteResponse>() {
                @Override
                public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> response) {
                    if(response.code() == 200){
                        cliente = response.body();
                        saveOnPreferences();

                        Intent intent = new Intent(scrLogin.this,scrMenu.class);
                        saveUser(cliente.getNombre(),cliente.getRfc(),cliente.getEmail());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    if(response.code() == 404){
                        tbUsuario.setError("Usuario y/o contraseña incorrectos");
                        tbUsuario.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<ClienteResponse> call, Throwable t) {

                }
            });
        });

        btnRegistro.setOnClickListener(view -> {
            Intent intent = new Intent(scrLogin.this,Registro.class);
            startActivity(intent);
        });
    }

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
}
