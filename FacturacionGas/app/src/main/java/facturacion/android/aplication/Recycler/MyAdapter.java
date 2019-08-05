package facturacion.android.aplication.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import facturacion.android.aplication.R;
import facturacion.android.aplication.io.response.FacturaResponse;
import facturacion.android.aplication.io.response.FacturasResponse;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<FacturasResponse> facturas;
    private int layout;
    private OnItemClickListener listener;

    public MyAdapter(List<FacturasResponse> facturas, int layout, OnItemClickListener listener){
        this.facturas = facturas;
        this.layout = layout;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        holder.bind(facturas.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return facturas.size();
    }
    public static class  ViewHolder extends RecyclerView.ViewHolder{
        public TextView datos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datos = itemView.findViewById(R.id.tbDatos);
        }

        public void bind(final FacturasResponse factura, final OnItemClickListener listener){
            datos.setText(factura.getDatos());
            itemView.setOnClickListener(view -> listener.onItemClick(factura, getAdapterPosition()));
        }
    }
    public interface OnItemClickListener{
        void onItemClick(FacturasResponse factura, int position);
    }
}
