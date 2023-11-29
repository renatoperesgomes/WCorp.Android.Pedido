package com.example.w_corpandroidpedido.Util.Adapter.Categoria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.Atividades.Categoria.SubCategoriaActivity;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.example.w_corpandroidpedido.R;

import java.util.List;

public class SubCategoriaAdapter extends RecyclerView.Adapter<SubCategoriaAdapter.SubCategoriaViewHolder>{
    private final Context context;
    private final List<MaterialCategoria> items;

    public SubCategoriaAdapter(Context context, List<MaterialCategoria> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SubCategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        return new SubCategoriaViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriaViewHolder holder, int position) {
        holder.nomeSubCategoria.setText(items.get(position).nome);
        holder.itemView.setOnClickListener(view -> {
            if(items.get(position).pdvMultiplaSelecao){
                new SubCategoriaActivity().irParaProdutos(context, items.get(position).id,
                    items.get(position).pdvMultiplaSelecao,
                    items.get(position).pdvMultiplaSelecaoQuantidade);
            }else if(items.get(position).pdvComboCategoriaFilho){
                new SubCategoriaActivity().irParaProdutos(context,
                        items.get(position).id,
                        items.get(position).pdvComboCategoriaFilho);
            }else{
                new SubCategoriaActivity().irParaProdutos(context, items.get(position).id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class SubCategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView nomeSubCategoria = itemView.findViewById(R.id.nome);
        public SubCategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
