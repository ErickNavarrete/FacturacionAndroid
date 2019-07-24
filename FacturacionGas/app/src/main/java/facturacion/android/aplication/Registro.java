package facturacion.android.aplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.body.ClienteBody;
import facturacion.android.aplication.io.response.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    private Button btnRegistro;
    private TextInputEditText tbNombre;
    private TextInputEditText tbAP;
    private TextInputEditText tbAM;
    private TextInputEditText tbUser;
    private TextInputEditText tbPass;
    private TextInputEditText tbPass2;
    private TextInputEditText tbMail;
    private TextInputEditText tbRFC;
    private TextInputEditText tbRazon;

    private String errorResponse;
    private ProgressDialog progressDialog;

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

        setToolbar();
        btnRegistro.setOnClickListener(view -> {
            closeKeyboard();
            progressDialog = new ProgressDialog(Registro.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.show();
            checkFields();
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

            Call<UserResponse> call = MyApiAdapter.getApiService().createCliente(clienteBody);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if(response.code() == 201){
                        progressDialog.hide();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(Registro.this);
                        alerta.setMessage("Usuario creado con éxito")
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Éxito");
                        titulo.show();

                        Intent intent = new Intent(Registro.this,scrLogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    if(response.code() == 409){
                        progressDialog.hide();
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorResponse = jObjError.getString("mensaje");
                            if(errorResponse.equals("Usuario registrado")){
                                tbUser.setError("Usuario registrado");
                                tbUser.requestFocus();
                            }else{
                                tbRFC.setError("RFC registrado");
                                tbRFC.requestFocus();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(response.code() >= 500){
                        progressDialog.hide();
                        AlertDialog.Builder alert = new AlertDialog.Builder(Registro.this);
                        alert.setMessage("No fue posible conectarse con el servidor, intente de nuevo")
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());

                        AlertDialog title = alert.create();
                        title.show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {

                }
            });
        });
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void checkFields(){
        if(TextUtils.isEmpty(tbNombre.getText().toString())){
            tbNombre.setError("Campo obligatorio");
            tbNombre.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(tbAM.getText().toString())){
            tbAM.setError("Campo obligatorio");
            tbAM.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(tbAP.getText().toString())){
            tbAP.setError("Campo obligatorio");
            tbAP.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(tbUser.getText().toString())){
            tbUser.setError("Campo obligatorio");
            tbUser.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(tbPass.getText().toString())){
            tbPass.setError("Campo obligatorio");
            tbPass.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(tbPass2.getText().toString())){
            tbPass2.setError("Campo obligatorio");
            tbPass2.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(tbMail.getText().toString())){
            tbMail.setError("Campo obligatorio");
            tbMail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(tbRFC.getText().toString())){
            tbRFC.setError("Campo obligatorio");
            tbRFC.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(tbRazon.getText().toString())){
            tbRazon.setError("Campo obligatorio");
            tbRazon.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(tbPass.getText().toString()) && !TextUtils.isEmpty(tbPass2.getText().toString())){
            if(!tbPass.getText().toString().equals(tbPass2.getText().toString())){
                tbPass.setError("Las contraseñas no coinciden");
                tbPass.requestFocus();
                return;
            }
        }
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
