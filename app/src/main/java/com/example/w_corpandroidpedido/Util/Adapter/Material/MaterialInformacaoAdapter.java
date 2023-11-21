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
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.example.w_corpandroidpedido.Atividades.Material.MaterialInformacaoActivity;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.BuscarMaterialService;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MaterialInformacaoAdapter extends RecyclerView.Adapter<MaterialInformacaoAdapter.MaterialInformacaoViewHolder> {

    private final Context context;
    private final String idMaterial;
    private final String nomeMaterial;
    private final String valorMaterial;
    private final boolean multiplaSelecao;
    private final boolean comboCategoriaFilho;
    private final int qtdMaterial;
    private double maiorValor = 0;
    private final ArrayList<Material> items;

    public MaterialInformacaoAdapter(Context context, String idMaterial, String nomeMaterial, String valorMaterial){
        this.context = context;
        this.idMaterial = idMaterial;
        this.nomeMaterial = nomeMaterial;
        this.valorMaterial = valorMaterial;
        this.comboCategoriaFilho = false;
        this.multiplaSelecao = false;
        this.qtdMaterial = 0;
        this.items = null;
    }
    public MaterialInformacaoAdapter(Context context, String nomeMaterial, String valorMaterial ,boolean comboCategoriaFilho, ArrayList<Material> items){
        this.context = context;
        this.idMaterial = "";
        this.nomeMaterial = nomeMaterial;
        this.valorMaterial = valorMaterial;
        this.comboCategoriaFilho = comboCategoriaFilho;
        this.multiplaSelecao = false;
        this.qtdMaterial = items.size();
        this.items = items;
    }
    public MaterialInformacaoAdapter(Context context, String nomeMaterial, String valorMaterial ,boolean multiplaSelecao, int qtdMaterial, ArrayList<Material> items){
        this.context = context;
        this.idMaterial = "";
        this.nomeMaterial = nomeMaterial;
        this.valorMaterial = valorMaterial;
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

    @Override
    public void onBindViewHolder(@NonNull MaterialInformacaoAdapter.MaterialInformacaoViewHolder holder, int position) {
        if(!multiplaSelecao && !comboCategoriaFilho) {
            holder.idMaterial.setText(idMaterial);
            holder.nomeMaterial.setText(nomeMaterial);
            holder.valorMaterial.setText(valorMaterial);
            holder.qtdMaterial.setText("1");
        }else if(multiplaSelecao){
            holder.idMaterial.setText(String.valueOf(items.get(position).retorno.id));
            holder.nomeMaterial.setText(String.valueOf(items.get(position).retorno.nome));

            for(int i = 0; i < items.size(); i++){
                if(maiorValor < items.get(i).retorno.preco){
                    maiorValor = items.get(i).retorno.preco;
                }
            }

            holder.valorMaterial.setText(String.valueOf(maiorValor / qtdMaterial));

            double divisaoMateriais = 1.0 / qtdMaterial;
            holder.qtdMaterial.setText(String.valueOf(divisaoMateriais));
        }
        else{
            holder.idMaterial.setText(String.valueOf(items.get(position).retorno.id));
            holder.nomeMaterial.setText(String.valueOf(items.get(position).retorno.nome));
            holder.valorMaterial.setText(String.valueOf(items.get(position).retorno.preco));
            holder.qtdMaterial.setText(String.valueOf(1));
        }
    }
    @Override
    public int getItemCount() {
        if(multiplaSelecao || comboCategoriaFilho){
            return items.size();
        }else{
            return 1;
        }
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
