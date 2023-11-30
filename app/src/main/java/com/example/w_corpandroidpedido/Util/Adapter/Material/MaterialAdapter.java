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
import com.example.w_corpandroidpedido.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private final Context context;
    private final List<Material> items;
    private int contagemSelecao = 0;
    private final boolean multiplaSelecao;
    private final int qtdSelecao;
    private final boolean comboCategoriaFilho;
    private boolean todasCategoriasPreenchidas;
    private final ArrayList<Material> listMaterialSelecionado = new ArrayList<>();
    private final ArrayList<Integer> listCategoriasPreenchidas = new ArrayList<>();
    private final ArrayList<MaterialAdapter.MaterialViewHolder> listMaterialViewHolder = new ArrayList<>();
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

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull MaterialAdapter.MaterialViewHolder holder, int position) {

        if (comboCategoriaFilho) {
            int idMaterialCategoriaSelecionado = 0;
            for(int i = 0; i < items.get(position).listMaterialCategoria.size(); i++){
                if(items.get(position).listMaterialCategoria.get(i).pdvComboCategoriaFilho &&
                    items.get(position).listMaterialCategoria.get(i).idPai != 0){
                    idMaterialCategoriaSelecionado = items.get(position).listMaterialCategoria.get(i).id;
                    break;
                }
            }
            holder.cardMaterial.setTag(idMaterialCategoriaSelecionado);
        }

        holder.cardMaterial.setId(position);
        holder.nomeProduto.setText(items.get(position).nome);
        holder.precoProduto.setText("R$ " + String.format("%.2f", items.get(position).preco));
        holder.material = items.get(position);

        holder.itemView.setOnClickListener(view -> {
            if (!multiplaSelecao && !comboCategoriaFilho) {
                listMaterialSelecionado.add(contagemSelecao, items.get(position));
                new MaterialActivity().irParaMaterialInformacao(context, listMaterialSelecionado);
            } else {
                if (multiplaSelecao) {

                    holder.cardMaterial.setCardBackgroundColor(Color.parseColor("#009574"));
                    holder.cardMaterial.setClickable(false);

                    listMaterialSelecionado.add(holder.material);
                    contagemSelecao++;

                    if (contagemSelecao == qtdSelecao) {
                        new MaterialActivity().irParaMaterialInformacao(context, true, qtdSelecao, listMaterialSelecionado);
                        contagemSelecao = 0;
                    }
                } else {
                    CardView cardSelecionado = holder.cardMaterial;
                    RecyclerView rvMaterial = (RecyclerView) holder.itemView.getParent();

                    int idMaterialCategoriaSelecionado = holder.cardMaterial.getTag().hashCode();

                    listMaterialSelecionado.add(contagemSelecao, items.get(position));
                    contagemSelecao++;
                    listCategoriasPreenchidas.add(idMaterialCategoriaSelecionado);

                    if (cardSelecionado.isSelected()) {
                        for (int idCard = 0; idCard < items.size(); idCard++) {
                            CardView cardHabilitarTodos = rvMaterial.findViewById(idCard);
                            if(cardHabilitarTodos != null){
                                cardHabilitarTodos.setCardBackgroundColor(Color.parseColor("#005E49"));
                                cardHabilitarTodos.setSelected(false);
                                cardHabilitarTodos.setClickable(false);

                                listCategoriasPreenchidas.clear();
                                listMaterialSelecionado.clear();
                                contagemSelecao = 0;
                            }
                        }
                    } else {
                        todasCategoriasPreenchidas = true;
                        for (int idCard = 0; idCard < items.size(); idCard++) {
                            CardView cardDesabilitar = rvMaterial.findViewById(idCard);
                            if(cardDesabilitar != null){
                                int idCategoriaMaterialBuscar = cardDesabilitar.getTag().hashCode();

                                if (idMaterialCategoriaSelecionado == idCategoriaMaterialBuscar &&
                                        position != idCard) {
                                    cardDesabilitar.setCardBackgroundColor(Color.parseColor("#001c13"));
                                    cardDesabilitar.setSelected(false);
                                    cardDesabilitar.setClickable(true);
                                }

                                if (!listCategoriasPreenchidas.contains(idCategoriaMaterialBuscar)) {
                                    todasCategoriasPreenchidas = false;
                                }
                            }
                        }

                        if (todasCategoriasPreenchidas) {
                            new MaterialActivity().irParaMaterialInformacao(context, true, listMaterialSelecionado);
                        }

                        cardSelecionado.setCardBackgroundColor(Color.parseColor("#009574"));
                        cardSelecionado.setSelected(true);
                        cardSelecionado.setClickable(false);

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

        public boolean Selecionado = false;

        public Material material;
        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
