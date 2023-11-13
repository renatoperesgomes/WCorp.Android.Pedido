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
        View itemLista = LayoutInflater.from(context).inflate(R.layout.card_material, parent, false);
        return new MaterialViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialAdapter.MaterialViewHolder holder, int position) {

        holder.cardMaterial.setId(position);
        holder.nomeProduto.setText(items.get(position).nome);
        holder.precoProduto.setText("R$ " + items.get(position).preco);

        holder.itemView.setOnClickListener(view -> {
            if(multiplaSelecao){
                holder.cardMaterial.setCardBackgroundColor(Color.parseColor("#009574"));

                contagemSelecao++;

                if(contagemSelecao == qtdSelecao){
                    new MaterialActivity().irParaProdutoInformacao(context, items.get(position).id, items.get(position).nome ,items.get(position).preco);
                    contagemSelecao = 0;
                }
            }else if(comboCategoriaFilho){
                for(int i = 0; i < items.size(); i++){
                    if(position == i){
                        System.out.println("É igualé");
                    }else{
                        System.out.println(view);
                    }

                }
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

    static class MaterialViewHolder extends RecyclerView.ViewHolder{
        CardView cardMaterial = itemView.findViewById(R.id.cardMaterial);
        TextView nomeProduto = itemView.findViewById(R.id.nomeMaterial);
        TextView precoProduto = itemView.findViewById(R.id.precoMaterial);
        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
