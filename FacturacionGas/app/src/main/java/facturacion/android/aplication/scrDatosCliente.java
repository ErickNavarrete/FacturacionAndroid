package facturacion.android.aplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


import java.util.ArrayList;
import java.util.Arrays;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.response.ClienteResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scrDatosCliente extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private Button btnActualiza;
    private TextInputEditText tbNombre;
    private TextInputEditText tbAP;
    private TextInputEditText tbAM;
    private TextInputEditText tbMail;
    private TextInputEditText tbRFC;

    private Spinner spinner;
    private String cfdi_option;
    private int index;

    private ClienteResponse usuario;
    private ClienteResponse usuario_update;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_datos_cliente);
        setToolbar();

        btnActualiza = findViewById(R.id.btnActualiza);
        tbNombre = findViewById(R.id.tbNombre);
        tbNombre = findViewById(R.id.tbNombre);
        tbAP = findViewById(R.id.tbAP);
        tbAM = findViewById(R.id.tbAM);
        tbMail = findViewById(R.id.tbMail);
        tbRFC = findViewById(R.id.tbRFC);
        spinner = findViewById(R.id.sCFDI);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(scrDatosCliente.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.CFDI)));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.CFDI,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Call<ClienteResponse> call = MyApiAdapter.getApiService().getClienteUsuario(getRFC());
        call.enqueue(new Callback<ClienteResponse>() {
            @Override
            public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> response) {
                if(response.isSuccessful()){
                    progressDialog.cancel();

                    usuario = response.body();

                    tbNombre.setText(usuario.getNombre());
                    tbAP.setText(usuario.getApellido_paterno());
                    tbAM.setText(usuario.getApellido_materno());
                    tbMail.setText(usuario.getEmail());
                    tbRFC.setText(usuario.getRfc());
                    cfdi_option = usuario.getNombre_razonsocial();

                    for (int i=0;i<22;i++){
                        if(arrayList.get(i).equals(cfdi_option)){
                            index = i;
                            break;
                        }
                    }

                    spinner.setSelection(index);
                }
            }

            @Override
            public void onFailure(Call<ClienteResponse> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG);
                toast.show();
            }
        });

        btnActualiza.setOnClickListener(view -> {
            closeKeyboard();
            progressDialog = new ProgressDialog(scrDatosCliente.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if(!checkFields()){
                progressDialog.cancel();
                return;
            }

            usuario_update = new ClienteResponse(
                    usuario.getId_usuario(),
                    tbNombre.getText().toString(),
                    tbAP.getText().toString(),
                    tbAM.getText().toString(),
                    cfdi_option,
                    cfdi_option,
                    cfdi_option,
                    tbRFC.getText().toString(),
                    tbMail.getText().toString(),
                    cfdi_option
            );

            Call<ClienteResponse> call2 = MyApiAdapter.getApiService().updateCliente(usuario_update);
            call2.enqueue(new Callback<ClienteResponse>() {
                @Override
                public void onResponse(Call<ClienteResponse> call1, Response<ClienteResponse> response) {
                    if(response.isSuccessful()){
                        progressDialog.cancel();

                        AlertDialog.Builder alert = new AlertDialog.Builder(scrDatosCliente.this);
                        alert.setMessage("Cliente actualizado")
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialogInterface, i) -> {
                                    Intent intent = new Intent(scrDatosCliente.this,scrMenu.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                });

                        AlertDialog title = alert.create();
                        title.show();
                    }else{
                        if(response.code() == 409){
                            progressDialog.cancel();
                            tbRFC.setError("RFC registrado");
                            tbRFC.requestFocus();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClienteResponse> call1, Throwable t) {

                }
            });
        });
    }

    public void openClienteDialog(String campo, String hint){
        ClienteDialog clienteDialog = new ClienteDialog(campo,hint);
        clienteDialog.show(getSupportFragmentManager(),"Cliente");
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
                Intent intent = new Intent(scrDatosCliente.this,scrMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cfdi_option = adapterView.getItemAtPosition(i).toString();
        closeKeyboard();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getRFC(){
        return prefs.getString("RFC","");
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

        return true;
    }
}
