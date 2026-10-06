package com.example.taller2sebastianjimenez.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taller2sebastianjimenez.R;
import com.example.taller2sebastianjimenez.entidades.Universidad;

import java.util.ArrayList;
import java.util.List;

public class UniversidadAdapter extends RecyclerView.Adapter<UniversidadAdapter.UniversidadViewHolder> {

    public interface OnItemActionListener {
        void onEditar(Universidad universidad);
        void onEliminar(Universidad universidad);
    }

    private List<Universidad> listaUniversidades = new ArrayList<>();
    private final OnItemActionListener listener;

    public UniversidadAdapter(OnItemActionListener listener) {
        this.listener = listener;
    }

    public void setListaUniversidades(List<Universidad> nuevaLista) {
        this.listaUniversidades = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UniversidadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_universidad, parent, false);
        return new UniversidadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversidadViewHolder holder, int position) {
        Universidad universidad = listaUniversidades.get(position);
        holder.bind(universidad, listener);
    }

    @Override
    public int getItemCount() {
        return listaUniversidades.size();
    }

    static class UniversidadViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvBadgeId;
        private final TextView tvNombreItem;
        private final TextView tvWwwItem;
        private final ImageButton btnItemEditar;
        private final ImageButton btnItemEliminar;

        public UniversidadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBadgeId = itemView.findViewById(R.id.tvBadgeId);
            tvNombreItem = itemView.findViewById(R.id.tvNombreItem);
            tvWwwItem = itemView.findViewById(R.id.tvWwwItem);
            btnItemEditar = itemView.findViewById(R.id.btnItemEditar);
            btnItemEliminar = itemView.findViewById(R.id.btnItemEliminar);
        }

        public void bind(Universidad universidad, OnItemActionListener listener) {
            tvBadgeId.setText("#" + universidad.getId());
            tvNombreItem.setText(universidad.getNombre());

            String www = universidad.getWww();
            if (www == null || www.trim().isEmpty()) {
                tvWwwItem.setText("Sin sitio web");
            } else {
                tvWwwItem.setText(www);
            }

            btnItemEditar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditar(universidad);
                }
            });

            btnItemEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEliminar(universidad);
                }
            });
        }
    }
}
