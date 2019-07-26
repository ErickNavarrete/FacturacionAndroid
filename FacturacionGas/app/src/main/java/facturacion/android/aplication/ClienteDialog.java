package facturacion.android.aplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ClienteDialog extends AppCompatDialogFragment {

    private TextInputEditText tbModificacion;
    private TextInputLayout lbModificacion;
    private String hint;
    private String campo;

    ClienteDialog(String campo, String hint) {
        this.hint = hint;
        this.campo = campo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.resource_file,null);

        builder.setView(view)
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        tbModificacion = view.findViewById(R.id.tbModificacion);
        lbModificacion = view.findViewById(R.id.lbModificacion);
        tbModificacion.setText(campo);
        lbModificacion.setHint(hint);

        return builder.create();
    }
}
