package com.example.w_corpandroidpedido.Util.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.Atividades.Categoria.SubCategoriaActivity;
import com.example.w_corpandroidpedido.Models.Material.MaterialSubCategoria;
import com.example.w_corpandroidpedido.R;

import java.util.List;

public class SubCategoriaAdapter extends RecyclerView.Adapter<SubCategoriaAdapter.SubCategoriaViewHolder>{
    private Context context;
    private List<MaterialSubCategoria.Retorno> items;

    public SubCategoriaAdapter(Context context, List<MaterialSubCategoria.Retorno> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SubCategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        SubCategoriaViewHolder holder = new SubCategoriaViewHolder(itemLista);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriaViewHolder holder, int position) {
        holder.nomeSubCategoria.setText(items.get(position).nome);
        holder.itemView.setOnClickListener(view -> {
            new SubCategoriaActivity().irParaProdutos(context, items.get(position).id);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SubCategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView nomeSubCategoria = itemView.findViewById(R.id.nome);
        public SubCategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
