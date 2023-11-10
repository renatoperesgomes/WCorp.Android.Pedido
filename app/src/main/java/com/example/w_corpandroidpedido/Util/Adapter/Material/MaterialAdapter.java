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
import java.util.concurrent.atomic.AtomicInteger;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private final Context context;
    private final List<Material.Retorno> items;
    private int contagemSelecao = 0;
    private final boolean multiplaSelecao;
    private final int qtdSelecao;
    private final boolean comboCategoriaFilho;

    public MaterialAdapter(Context context, List<Material.Retorno> items){
        this.context = context;
        this.items = items;
        this.multiplaSelecao = false;
        this.qtdSelecao = 0;
        this.comboCategoriaFilho = false;
    }
    public MaterialAdapter(Context context, List<Material.Retorno> items, boolean comboCategoriaFilho){
        this.context = context;
        this.items = items;
        this.multiplaSelecao = false;
        this.qtdSelecao = 0;
        this.comboCategoriaFilho = comboCategoriaFilho;
    }

    public MaterialAdapter(Context context, List<Material.Retorno> items, boolean multiplaSelecao, int qtdSelecao){
        this.context = context;
        this.items = items;
        this.multiplaSelecao = multiplaSelecao;
        this.qtdSelecao = qtdSelecao;
        this.comboCategoriaFilho = false;
    }
    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        MaterialViewHolder holder = new MaterialViewHolder(itemLista);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialAdapter.MaterialViewHolder holder, int position) {
        holder.nomeProduto.setText(items.get(position).nome);
        holder.itemView.setId(items.get(position).id);
        CardView card = holder.itemView.findViewById(R.id.card);
        holder.itemView.setOnClickListener(view -> {
            if(multiplaSelecao){
                card.setCardBackgroundColor(Color.parseColor("#009574"));

                contagemSelecao++;

                if(contagemSelecao == qtdSelecao){
                    new MaterialActivity().irParaProdutoInformacao(context, items.get(position).id, items.get(position).nome ,items.get(position).preco);
                    contagemSelecao = 0;
                }
            }else if(comboCategoriaFilho){
                card.setClickable(false);
            }
            else{
                new MaterialActivity().irParaProdutoInformacao(context, items.get(position).id, items.get(position).nome ,items.get(position).preco);
            }
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
