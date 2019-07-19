package facturacion.android.aplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.response.ArticuloClass;
import facturacion.android.aplication.io.response.ClienteResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scrLogin extends AppCompatActivity {

    com.google.android.material.button.MaterialButton btnIniciar;
    com.google.android.material.button.MaterialButton btnRegistro;
    com.google.android.material.textfield.TextInputEditText tbUsuario;
    com.google.android.material.textfield.TextInputEditText tbPass;

    ClienteResponse cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_login);

        btnIniciar = findViewById(R.id.btnIniciar);
        btnRegistro= findViewById(R.id.btnRegistro);
        tbUsuario = findViewById(R.id.tbUsuario);
        tbPass = findViewById(R.id.tbPass);

        btnIniciar.setOnClickListener(view -> {
            Call<ClienteResponse> call = MyApiAdapter.getApiService().getCliente(tbUsuario.getText().toString(), tbPass.getText().toString());
            call.enqueue(new Callback<ClienteResponse>() {
                @Override
                public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> response) {
                    if(response.code() == 200){
                        cliente = response.body();

                        Intent intent = new Intent(scrLogin.this,MainActivity.class);
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

    @Override
    public void onBackPressed() {

    }
}
