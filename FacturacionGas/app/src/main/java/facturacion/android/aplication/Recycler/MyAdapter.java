package facturacion.android.aplication.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import facturacion.android.aplication.R;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> UUIDS;
    private int layout;
    private OnItemClickListener listener;

    public MyAdapter(List<String> UUIDS, int layout, OnItemClickListener listener){
        this.UUIDS = UUIDS;
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
        holder.bind(UUIDS.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return UUIDS.size();
    }
    public static class  ViewHolder extends RecyclerView.ViewHolder{
        public TextView uuid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.uuid = itemView.findViewById(R.id.tbTextViewName);
        }

        public void bind(final String uuid, final OnItemClickListener listener){
            this.uuid.setText(uuid);
            itemView.setOnClickListener(view -> listener.onItemClick(uuid, getAdapterPosition()));
        }
    }
    public interface OnItemClickListener{
        void onItemClick(String name, int position);
    }
}
