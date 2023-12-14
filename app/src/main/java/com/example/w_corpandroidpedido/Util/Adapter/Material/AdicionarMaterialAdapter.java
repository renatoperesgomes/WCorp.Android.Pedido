package com.example.w_corpandroidpedido.Util.Adapter.Material;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdicionarMaterialAdapter extends RecyclerView.Adapter<AdicionarMaterialAdapter.MaterialInformacaoViewHolder> {

    private final Context context;
    private final boolean multiplaSelecao;
    private final boolean comboCategoriaFilho;
    private final int qtdMaterial;
    private double maiorValor = 0;
    private final ArrayList<Material> items;

    public AdicionarMaterialAdapter(Context context, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = false;
        this.multiplaSelecao = false;
        this.qtdMaterial = 0;
        this.items = items;
    }
    public AdicionarMaterialAdapter(Context context, boolean comboCategoriaFilho, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = comboCategoriaFilho;
        this.multiplaSelecao = false;
        this.qtdMaterial = items.size();
        this.items = items;
    }
    public AdicionarMaterialAdapter(Context context, boolean multiplaSelecao, int qtdMaterial, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = false;
        this.multiplaSelecao = multiplaSelecao;
        this.qtdMaterial = qtdMaterial;
        this.items = items;
    }

    @NonNull
    @Override
    public MaterialInformacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.lista_card_produtos, parent, false);
        return new MaterialInformacaoViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdicionarMaterialAdapter.MaterialInformacaoViewHolder holder, int position) {
        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        if(!multiplaSelecao && !comboCategoriaFilho) {
            holder.nomeMaterial.setText(String.valueOf(items.get(position).nome));
            holder.valorMaterial.setText(formatNumero.format(items.get(position).preco));
            holder.qtdMaterial.setText("1 Un.");

        }else if(multiplaSelecao){
            holder.nomeMaterial.setText(items.get(position).nome);

            for(int i = 0; i < items.size(); i++){
                if(maiorValor < items.get(i).preco){
                    maiorValor = items.get(i).preco;
                }
            }

            holder.valorMaterial.setText(formatNumero.format(maiorValor));

            double divisaoMateriais = 1.0 / qtdMaterial;
            holder.qtdMaterial.setText(divisaoMateriais + " Un.");

        }else{
            holder.nomeMaterial.setText(String.valueOf(items.get(position).nome));
            holder.valorMaterial.setText(formatNumero.format(items.get(position).preco));
            holder.qtdMaterial.setText("1 Un.");
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MaterialInformacaoViewHolder extends RecyclerView.ViewHolder{
        TextView nomeMaterial = itemView.findViewById(R.id.nomeMaterial);
        TextView valorMaterial = itemView.findViewById(R.id.valorMaterial);
        TextView qtdMaterial = itemView.findViewById(R.id.qtdMaterial);
        public MaterialInformacaoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
