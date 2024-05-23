package com.wcorp.w_corpandroidpedido.Util.Adapter.Material;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wcorp.w_corpandroidpedido.Models.Material.Material;
import com.wcorp.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.wcorp.w_corpandroidpedido.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdicionarMaterialAdapter extends RecyclerView.Adapter<AdicionarMaterialAdapter.MaterialInformacaoViewHolder> {

    private final Context context;
    private final boolean multiplaSelecao;
    private final boolean comboCategoriaFilho;
    private final int qtdMaterial;
    private int qtdMaterialNaoPromocional = 0;
    private double maiorValor = 0;
    private boolean primeiroMaterial = true;
    private final ArrayList<Material> items;

    public AdicionarMaterialAdapter(Context context, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = false;
        this.multiplaSelecao = false;
        this.qtdMaterial = 0;
        this.items = items;
    }

    public AdicionarMaterialAdapter(Context context, boolean multiplaSelecao, int qtdMaterial, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = false;
        this.multiplaSelecao = multiplaSelecao;
        this.qtdMaterial = qtdMaterial;
        this.items = items;
    }
    public AdicionarMaterialAdapter(Context context, boolean comboCategoriaFilho, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = comboCategoriaFilho;
        this.multiplaSelecao = false;
        this.qtdMaterial = items.size();
        this.items = items;
    }
    public AdicionarMaterialAdapter(Context context, boolean multiplaSelecao, int qtdMaterial, boolean comboCategoriaFilho, ArrayList<Material> items){
        this.context = context;
        this.comboCategoriaFilho = comboCategoriaFilho;
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

        if(primeiroMaterial){
            for(Material material:
                items){
                if(maiorValor < material.preco){
                    maiorValor = material.preco;
                }

                for (MaterialCategoria materialCategoria:
                    material.listMaterialCategoria) {
                    if(!materialCategoria.pdvCategoriaParaItemPromocional &&
                        materialCategoria.idPai != 0)
                        qtdMaterialNaoPromocional++;
                }
            }
            primeiroMaterial = false;
        }

        for (MaterialCategoria materialCategoria:
                items.get(position).listMaterialCategoria) {
            holder.multiplaSelecaoDividirPreco = materialCategoria.pdvMulltiplaSelecaoDividirPreco;
            if(materialCategoria.idPai != 0){
                holder.categoriaParaItemPromocional = materialCategoria.pdvCategoriaParaItemPromocional;
                break;
            }
        }


        if (!multiplaSelecao && !comboCategoriaFilho) {
            holder.nomeMaterial.setText(String.valueOf(items.get(position).nome));
            holder.valorMaterial.setText(formatNumero.format(items.get(position).preco));
            holder.qtdMaterial.setText("1 Un.");

        } else if (multiplaSelecao && comboCategoriaFilho) {

            if (holder.categoriaParaItemPromocional) {
                holder.nomeMaterial.setText(String.valueOf(items.get(position).nome));
                holder.valorMaterial.setText(formatNumero.format(0));
                holder.qtdMaterial.setText("1 Un.");
            }else{
                holder.nomeMaterial.setText(items.get(position).nome);

                holder.valorMaterial.setText(formatNumero.format(maiorValor));

                double divisaoMateriais = 1.0 / qtdMaterialNaoPromocional;
                holder.qtdMaterial.setText(divisaoMateriais + " Un.");
            }
        } else if (multiplaSelecao) {
            holder.nomeMaterial.setText(items.get(position).nome);
            holder.valorMaterial.setText(formatNumero.format(maiorValor));

            if(holder.multiplaSelecaoDividirPreco){
                double divisaoValorMaior = maiorValor / qtdMaterial;
                holder.valorMaterial.setText(formatNumero.format(divisaoValorMaior));
                holder.qtdMaterial.setText("1 Un.");
            }else{
                double divisaoMateriais = 1.0 / qtdMaterial;
                holder.qtdMaterial.setText(String.format(new Locale("pt", "BR"), "%.2f", divisaoMateriais) + " Un.");
            }
        } else {
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
        boolean categoriaParaItemPromocional;
        boolean multiplaSelecaoDividirPreco;
        public MaterialInformacaoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
