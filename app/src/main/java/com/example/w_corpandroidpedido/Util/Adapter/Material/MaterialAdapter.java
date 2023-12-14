package com.example.w_corpandroidpedido.Util.Adapter.Material;

import android.annotation.SuppressLint;
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
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.example.w_corpandroidpedido.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private final Context context;
    private final List<Material> items;
    private final boolean multiplaSelecao;
    private final int qtdSelecao;
    private final boolean comboCategoriaFilho;
    private boolean todasCategoriasPreenchidas;
    private final ArrayList<Material> listMaterialSelecionado = new ArrayList<>();
    private final ArrayList<Integer> listCategoriasPreenchidas = new ArrayList<>();
    private final ArrayList<MaterialViewHolder> listMaterialViewHolder = new ArrayList<>();
    public MaterialAdapter(Context context, List<Material> items){
        this.context = context;
        this.items = items;
        this.multiplaSelecao = false;
        this.qtdSelecao = 0;
        this.comboCategoriaFilho = false;
    }
    public MaterialAdapter(Context context, List<Material> items,boolean comboCategoriaFilho){
        this.context = context;
        this.items = items;
        this.multiplaSelecao = false;
        this.qtdSelecao = 0;
        this.comboCategoriaFilho = comboCategoriaFilho;
    }

    public MaterialAdapter(Context context, List<Material> items, boolean multiplaSelecao, int qtdSelecao){
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
        MaterialViewHolder materialViewHolder = new MaterialViewHolder(itemLista);
        listMaterialViewHolder.add(materialViewHolder);
        return materialViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MaterialAdapter.MaterialViewHolder holder, int position) {
        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        if(comboCategoriaFilho){
            for(int i = 0; i < items.get(position).listMaterialCategoria.size(); i++){
                if(items.get(position).listMaterialCategoria.get(i).pdvComboCategoriaFilho &&
                        items.get(position).listMaterialCategoria.get(i).idPai != 0){
                    holder.idMaterialCategoria = items.get(position).listMaterialCategoria.get(i).id;
                    break;
                }
            }
        }

        holder.cardMaterial.setId(position);
        holder.nomeProduto.setText(items.get(position).nome);
        holder.precoProduto.setText(formatNumero.format(items.get(position).preco));
        holder.material = items.get(position);
        holder.listMaterialCategoria = items.get(position).listMaterialCategoria;

        holder.itemView.setOnClickListener(view -> {
            if (!multiplaSelecao && !comboCategoriaFilho) {
                listMaterialSelecionado.add(items.get(position));
                new MaterialActivity().irParaMaterialInformacao(context, listMaterialSelecionado);
            } else {
                if (multiplaSelecao) {

                    CardView cardSelecionado = holder.cardMaterial;

                    if (cardSelecionado.isSelected()) {
                        cardSelecionado.setCardBackgroundColor(Color.parseColor("#005E49"));
                        cardSelecionado.setSelected(false);
                        listMaterialSelecionado.clear();
                    } else {
                        cardSelecionado.setCardBackgroundColor(Color.parseColor("#009574"));
                        cardSelecionado.setSelected(true);
                    }

                    for (MaterialViewHolder materialViewHolder :
                            listMaterialViewHolder) {
                        if (materialViewHolder.cardMaterial.isSelected() &&
                                !listMaterialSelecionado.contains(materialViewHolder.material)) {
                            listMaterialSelecionado.add(materialViewHolder.material);
                        }
                    }

                    if (listMaterialSelecionado.size() == qtdSelecao) {
                        new MaterialActivity().irParaMaterialInformacao(context, true, qtdSelecao, listMaterialSelecionado);
                    }
                } else {
                    CardView cardSelecionado = holder.cardMaterial;

                    listMaterialSelecionado.add(holder.material);
                    listCategoriasPreenchidas.add(holder.idMaterialCategoria);

                    if(cardSelecionado.isSelected()){
                        for (MaterialViewHolder materialViewHolder:
                             listMaterialViewHolder) {
                            materialViewHolder.cardMaterial.setCardBackgroundColor(Color.parseColor("#005E49"));
                            materialViewHolder.cardMaterial.setSelected(false);
                            materialViewHolder.cardMaterial.setClickable(false);
                        }
                        listCategoriasPreenchidas.clear();
                        listMaterialSelecionado.clear();
                    }else{
                        todasCategoriasPreenchidas = true;

                        for (MaterialViewHolder materialViewHolder:
                                listMaterialViewHolder) {

                            if (holder.idMaterialCategoria == materialViewHolder.idMaterialCategoria &&
                                    position != materialViewHolder.cardMaterial.getId()) {
                                materialViewHolder.cardMaterial.setCardBackgroundColor(Color.parseColor("#001C13"));
                                materialViewHolder.cardMaterial.setSelected(false);
                                materialViewHolder.cardMaterial.setClickable(true);
                            }

                            if (!listCategoriasPreenchidas.contains(materialViewHolder.idMaterialCategoria)) {
                                todasCategoriasPreenchidas = false;
                            }
                        }
                        if (todasCategoriasPreenchidas) {
                            new MaterialActivity().irParaMaterialInformacao(context, true, listMaterialSelecionado);
                        }

                        cardSelecionado.setCardBackgroundColor(Color.parseColor("#009574"));
                        cardSelecionado.setSelected(true);
                    }
                }
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
        int idMaterialCategoria = 0;
        public Material material;
        public ArrayList<MaterialCategoria> listMaterialCategoria;
        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
