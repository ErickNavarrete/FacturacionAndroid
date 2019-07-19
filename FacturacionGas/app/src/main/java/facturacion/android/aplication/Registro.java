package facturacion.android.aplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.body.ClienteBody;
import facturacion.android.aplication.io.response.ClienteResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    Button btnRegistro;
    com.google.android.material.textfield.TextInputEditText tbNombre;
    com.google.android.material.textfield.TextInputEditText tbAP;
    com.google.android.material.textfield.TextInputEditText tbAM;
    com.google.android.material.textfield.TextInputEditText tbUser;
    com.google.android.material.textfield.TextInputEditText tbPass;
    com.google.android.material.textfield.TextInputEditText tbPass2;
    com.google.android.material.textfield.TextInputEditText tbMail;
    com.google.android.material.textfield.TextInputEditText tbRFC;
    com.google.android.material.textfield.TextInputEditText tbRazon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btnRegistro = findViewById(R.id.btnRegistro);
        tbNombre = findViewById(R.id.tbNombre);
        tbAP = findViewById(R.id.tbAP);
        tbAM = findViewById(R.id.tbAM);
        tbUser = findViewById(R.id.tbUser);
        tbPass = findViewById(R.id.tbPass);
        tbPass2 = findViewById(R.id.tbPass2);
        tbMail = findViewById(R.id.tbMail);
        tbRFC = findViewById(R.id.tbRFC);
        tbRazon = findViewById(R.id.tbRazon);

        btnRegistro.setOnClickListener(view -> {
            ClienteBody clienteBody = new ClienteBody(
                    tbNombre.getText().toString(),
                    tbAM.getText().toString(),
                    tbAP.getText().toString(),
                    tbUser.getText().toString(),
                    tbPass.getText().toString(),
                    tbRazon.getText().toString(),
                    tbRFC.getText().toString(),
                    tbMail.getText().toString()
            );

            Call<ClienteResponse> call = MyApiAdapter.getApiService().createCliente(clienteBody);
            call.enqueue(new Callback<ClienteResponse>() {
                @Override
                public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> response) {
                    if(response.code() == 201){
                        AlertDialog.Builder alerta = new AlertDialog.Builder(Registro.this);
                        alerta.setMessage("Usuario creado con éxito")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Éxito");
                        titulo.show();
                    }

                    if(response.code() == 409){

                    }
                }

                @Override
                public void onFailure(Call<ClienteResponse> call, Throwable t) {

                }
            });
        });
    }
}
