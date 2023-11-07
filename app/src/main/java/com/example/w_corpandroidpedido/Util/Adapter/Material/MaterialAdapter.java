package com.example.w_corpandroidpedido.Util.Adapter.Material;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.Atividades.Material.MaterialActivity;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.R;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private final Context context;
    private final List<Material.Retorno> items;

    public MaterialAdapter(Context context, List<Material.Retorno> items){
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        MaterialViewHolder holder = new MaterialViewHolder(itemLista);
        CardView card = holder.itemView.findViewById(R.id.card);
        card.setCardBackgroundColor(Color.parseColor("#E1EA75"));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialAdapter.MaterialViewHolder holder, int position) {
        holder.nomeProduto.setText(items.get(position).nome);
        holder.itemView.setOnClickListener(view -> {
            new MaterialActivity().irParaProdutoInformacao(context, items.get(position).id, items.get(position).nome ,items.get(position).preco);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MaterialViewHolder extends RecyclerView.ViewHolder{
        TextView nomeProduto = itemView.findViewById(R.id.nome);
        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
