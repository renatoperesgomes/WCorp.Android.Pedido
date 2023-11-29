package com.example.w_corpandroidpedido.Util.Adapter.Material;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.R;


import java.util.ArrayList;

public class MaterialInformacaoAdapter extends RecyclerView.Adapter<MaterialInformacaoAdapter.MaterialInformacaoViewHolder> {

    private final Context context;
    private final boolean multiplaSelecao;
    private final boolean comboCategoriaFilho;
    private final int qtdMaterial;
    private double maiorValor = 0;
    private final ArrayList<Material> items;

    public MaterialInformacaoAdapter(Context context, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = false;
        this.multiplaSelecao = false;
        this.qtdMaterial = 0;
        this.items = items;
    }
    public MaterialInformacaoAdapter(Context context,boolean comboCategoriaFilho, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = comboCategoriaFilho;
        this.multiplaSelecao = false;
        this.qtdMaterial = items.size();
        this.items = items;
    }
    public MaterialInformacaoAdapter(Context context,boolean multiplaSelecao, int qtdMaterial, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = false;
        this.multiplaSelecao = multiplaSelecao;
        this.qtdMaterial = qtdMaterial;
        this.items = items;
    }

    @NonNull
    @Override
    public MaterialInformacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.listaprodutos, parent, false);
        return new MaterialInformacaoViewHolder(itemLista);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull MaterialInformacaoAdapter.MaterialInformacaoViewHolder holder, int position) {
        if(!multiplaSelecao && !comboCategoriaFilho) {
            holder.idMaterial.setText(String.valueOf(items.get(position).id));
            holder.nomeMaterial.setText(String.valueOf(items.get(position).nome));
            holder.valorMaterial.setText("R$ " + String.format("%.2f", items.get(position).preco));
            holder.qtdMaterial.setText("1 Un.");

        }else if(multiplaSelecao){
            holder.idMaterial.setText(String.valueOf(items.get(position).id));
            holder.nomeMaterial.setText(String.valueOf(items.get(position).nome));

            for(int i = 0; i < items.size(); i++){
                if(maiorValor < items.get(i).preco){
                    maiorValor = items.get(i).preco;
                }
            }

            holder.valorMaterial.setText("R$ " + String.format("%.2f", maiorValor / qtdMaterial));

            double divisaoMateriais = 1.0 / qtdMaterial;
            holder.qtdMaterial.setText(divisaoMateriais + " Un.");
        }
        else{
            holder.idMaterial.setText(String.valueOf(items.get(position).id));
            holder.nomeMaterial.setText(String.valueOf(items.get(position).nome));
            holder.valorMaterial.setText("R$ " + String.format("%.2f", items.get(position).preco));
            holder.qtdMaterial.setText("1 Un.");
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MaterialInformacaoViewHolder extends RecyclerView.ViewHolder{
        TextView idMaterial = itemView.findViewById(R.id.idMaterial);
        TextView nomeMaterial = itemView.findViewById(R.id.nomeMaterial);
        TextView valorMaterial = itemView.findViewById(R.id.valorMaterial);
        TextView qtdMaterial = itemView.findViewById(R.id.qtdMaterial);
        public MaterialInformacaoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
