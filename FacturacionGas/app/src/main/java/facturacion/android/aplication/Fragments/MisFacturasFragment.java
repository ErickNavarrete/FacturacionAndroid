package facturacion.android.aplication.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import facturacion.android.aplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisFacturasFragment extends Fragment {


    public MisFacturasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_facturas, container, false);
    }

}
