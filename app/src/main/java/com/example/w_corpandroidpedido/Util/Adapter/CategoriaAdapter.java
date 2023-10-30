package com.example.w_corpandroidpedido.Util.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.example.w_corpandroidpedido.R;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {
    private Context context;
    private List<MaterialCategoria.Retorno> items;

    public CategoriaAdapter(Context context, List<MaterialCategoria.Retorno> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        CategoriaViewHolder holder = new CategoriaViewHolder(itemLista);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        holder.nomeCategoria.setText(items.get(position).nome);
        holder.itemView.setOnClickListener(view -> {
            new CategoriaActivity().irParaSubCategoria(context, items.get(position).id);
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView nomeCategoria = itemView.findViewById(R.id.nome);

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}