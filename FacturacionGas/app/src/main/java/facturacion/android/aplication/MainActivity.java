package facturacion.android.aplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import facturacion.android.aplication.io.MyApiAdapter;
import facturacion.android.aplication.io.response.TicketResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    Button btnBuscar;
    EditText tbTicket;
    EditText tbMonto;

    String ticket;
    double monto;

    TicketResponse ticketResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBuscar = findViewById(R.id.btnBuscar);
        tbTicket = findViewById(R.id.tbTicket);
        tbMonto =  findViewById(R.id.tbMonto);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ticket = tbTicket.getText().toString();
                monto = Double.valueOf(tbMonto.getText().toString());

                Call<TicketResponse> call =  MyApiAdapter.getApiService().getTicket(ticket,monto);
                call.enqueue(new Callback<TicketResponse>() {
                    @Override
                    public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                        if(response.isSuccessful()){
                            ticketResponse = response.body();

                            Intent intent = new Intent(MainActivity.this,scrFactura.class);
                            intent.putExtra("MyClass",ticketResponse);
                            startActivity(intent);

                        }else{
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setMessage("Ticket no encontrado, verifique sus datos")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("Error");
                            titulo.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TicketResponse> call, Throwable t) {
                        Log.i("Error",t.getMessage());
                    }
                });
            }
        });
    }
}
