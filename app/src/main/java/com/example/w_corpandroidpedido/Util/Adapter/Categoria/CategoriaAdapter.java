package com.example.w_corpandroidpedido.Util.Adapter.Categoria;

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
    private final Context context;
    private final List<MaterialCategoria> items;

    public CategoriaAdapter(Context context, List<MaterialCategoria> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        return new CategoriaViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        holder.nomeCategoria.setText(items.get(position).nome);

        holder.itemView.setOnClickListener(view -> {
            if (items.get(position).pdvMultiplaSelecao) {
                new CategoriaActivity().irParaSubCategoria(context, items.get(position).id,
                        items.get(position).pdvMultiplaSelecao,
                        items.get(position).pdvMultiplaSelecaoQuantidade);
            }else if(items.get(position).pdvComboCategoriaFilho){

                new CategoriaActivity().irParaSubCategoria(context, items.get(position).id,
                        items.get(position).pdvComboCategoriaFilho);

            } else {
                new CategoriaActivity().irParaSubCategoria(context, items.get(position).id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView nomeCategoria = itemView.findViewById(R.id.nome);

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}