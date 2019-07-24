package facturacion.android.aplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputEditText;

import facturacion.android.aplication.io.response.FacturaResponse;

public class scrDatosFactura extends AppCompatActivity {
    private TextInputEditText tbSerie;
    private TextInputEditText tbFolio;
    private TextInputEditText tbUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scr_datos_factura);
        setToolbar();

        tbSerie = findViewById(R.id.tbSerie);
        tbFolio = findViewById(R.id.tbFolio);
        tbUUID = findViewById(R.id.tbUUID);

        tbSerie.setEnabled(false);
        tbFolio.setEnabled(false);
        tbUUID.setEnabled(false);

        Intent intent = getIntent();
        final FacturaResponse facturaResponse = (FacturaResponse) intent.getSerializableExtra("Factura");

        tbSerie.setText(facturaResponse.getSerie_t());
        tbFolio.setText(facturaResponse.getFolio_t());
        tbUUID.setText(facturaResponse.getUuid());
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
                Intent intent = new Intent(scrDatosFactura.this,scrMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
