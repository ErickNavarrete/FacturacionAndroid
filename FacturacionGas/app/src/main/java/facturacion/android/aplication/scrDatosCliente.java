package facturacion.android.aplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import facturacion.android.aplication.io.response.ClienteResponse;

public class scrDatosCliente extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private Button btnActualiza;
    private TextInputEditText tbNombre;
    private TextInputEditText tbAP;
    private TextInputEditText tbAM;
    private TextInputEditText tbMail;
    private TextInputEditText tbRFC;

    private Spinner spinner;
    private String cfdi_option;

    private ClienteResponse usuario;
    private ClienteResponse cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_datos_cliente);
        setToolbar();

        tbNombre = findViewById(R.id.tbNombre);
        btnActualiza = findViewById(R.id.btnActualiza);
        tbNombre = findViewById(R.id.tbNombre);
        tbAP = findViewById(R.id.tbAP);
        tbAM = findViewById(R.id.tbAM);
        tbMail = findViewById(R.id.tbMail);
        tbRFC = findViewById(R.id.tbRFC);
        spinner = findViewById(R.id.sCFDI);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.CFDI,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
