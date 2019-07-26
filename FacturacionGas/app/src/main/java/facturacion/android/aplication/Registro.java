package facturacion.android.aplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.body.ClienteBody;
import facturacion.android.aplication.io.response.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btnRegistro;
    private TextInputEditText tbNombre;
    private TextInputEditText tbAP;
    private TextInputEditText tbAM;
    private TextInputEditText tbUser;
    private TextInputEditText tbPass;
    private TextInputEditText tbPass2;
    private TextInputEditText tbMail;
    private TextInputEditText tbRFC;

    private String errorResponse;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private String cfdi_option;

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
        spinner = findViewById(R.id.sCFDI);

        setToolbar();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.CFDI,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btnRegistro.setOnClickListener(view -> {
            closeKeyboard();
            progressDialog = new ProgressDialog(Registro.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            if (!checkFields()){
                progressDialog.cancel();
                return;
            }
            ClienteBody clienteBody = new ClienteBody(
                    tbNombre.getText().toString(),
                    tbAM.getText().toString(),
                    tbAP.getText().toString(),
                    tbUser.getText().toString(),
                    tbPass.getText().toString(),
                    cfdi_option,
                    tbRFC.getText().toString(),
                    tbMail.getText().toString()
            );

            Call<UserResponse> call = MyApiAdapter.getApiService().createCliente(clienteBody);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if(response.code() == 201){
                        progressDialog.cancel();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(Registro.this);
                        alerta.setMessage("Usuario creado con éxito")
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialogInterface, i) -> {
                                    Intent intent = new Intent(Registro.this,scrLogin.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                });

                        AlertDialog titulo = alerta.create();
                        titulo.show();
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
    }
    private boolean checkFields(){
        if(TextUtils.isEmpty(tbNombre.getText().toString())){
            tbNombre.setError("Campo obligatorio");
            tbNombre.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(tbAM.getText().toString())){
            tbAM.setError("Campo obligatorio");
            tbAM.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(tbAP.getText().toString())){
            tbAP.setError("Campo obligatorio");
            tbAP.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(tbUser.getText().toString())){
            tbUser.setError("Campo obligatorio");
            tbUser.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(tbPass.getText().toString())){
            tbPass.setError("Campo obligatorio");
            tbPass.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(tbPass2.getText().toString())){
            tbPass2.setError("Campo obligatorio");
            tbPass2.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(tbMail.getText().toString())){
            tbMail.setError("Campo obligatorio");
            tbMail.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(tbRFC.getText().toString())){
            tbRFC.setError("Campo obligatorio");
            tbRFC.requestFocus();
            return false;
        }
        if (!TextUtils.isEmpty(tbPass.getText().toString()) && !TextUtils.isEmpty(tbPass2.getText().toString())){
            if(!tbPass.getText().toString().equals(tbPass2.getText().toString())){
                tbPass.setError("Las contraseñas no coinciden");
                tbPass.requestFocus();
                return false;
            }
        }

        return true;
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cfdi_option = adapterView.getItemAtPosition(i).toString();
        closeKeyboard();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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
