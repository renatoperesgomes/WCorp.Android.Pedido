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
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.Futures;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.example.w_corpandroidpedido.Atividades.Material.MaterialActivity;
import com.example.w_corpandroidpedido.Models.Material.ListaMaterial;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoriaSelecionado;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.BuscarMaterialCategoriaService;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private final Context context;
    private final List<ListaMaterial.Retorno> items;
    private int contagemSelecao = 0;
    private final boolean multiplaSelecao;
    private final int qtdSelecao;
    private final boolean comboCategoriaFilho;
    private boolean todasCategoriasPreenchidas;
    private final ArrayList<Integer> idMateriais = new ArrayList<>();
    private final ArrayList<Integer> listCategoriasPreenchidas = new ArrayList<>();
    private final BuscarMaterialCategoriaService buscarMaterialCategoriaService = new BuscarMaterialCategoriaService();
    private int idCategoriaMaterialSelecionado = 0;
    public MaterialAdapter(Context context, List<ListaMaterial.Retorno> items){
        this.context = context;
        this.items = items;
        this.multiplaSelecao = false;
        this.qtdSelecao = 0;
        this.comboCategoriaFilho = false;
    }
    public MaterialAdapter(Context context, List<ListaMaterial.Retorno> items,boolean comboCategoriaFilho){
        this.context = context;
        this.items = items;
        this.multiplaSelecao = false;
        this.qtdSelecao = 0;
        this.comboCategoriaFilho = comboCategoriaFilho;
    }

    public MaterialAdapter(Context context, List<ListaMaterial.Retorno> items, boolean multiplaSelecao, int qtdSelecao){
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

    @SuppressLint("SetTextI18n")
    @Override
    public synchronized void onBindViewHolder(@NonNull MaterialAdapter.MaterialViewHolder holder, int position) {

        if (comboCategoriaFilho) {
            final Future<MaterialCategoriaSelecionado> materialCategoriaSelecionado = buscarMaterialCategoriaService.buscarMaterialCategoria(items.get(position).id);
                try {
                    idCategoriaMaterialSelecionado = materialCategoriaSelecionado.get().retorno.id;
                    holder.cardMaterial.setTag(idCategoriaMaterialSelecionado);
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
        }
        holder.cardMaterial.setId(position);
        holder.nomeProduto.setText(items.get(position).nome);
        holder.precoProduto.setText("R$ " + items.get(position).preco);

        holder.itemView.setOnClickListener(view -> {
            if (!multiplaSelecao && !comboCategoriaFilho) {
                new MaterialActivity().irParaProdutoInformacao(context, items.get(position).id, items.get(position).nome, items.get(position).preco);
            } else {
                if (multiplaSelecao) {
                    idMateriais.add(contagemSelecao, items.get(position).id);

                    holder.cardMaterial.setCardBackgroundColor(Color.parseColor("#009574"));
                    holder.cardMaterial.setClickable(true);
                    contagemSelecao++;

                    if (contagemSelecao == qtdSelecao) {

                        new MaterialActivity().irParaProdutoInformacao(context, true, qtdSelecao, idMateriais);
                        contagemSelecao = 0;
                    }
                } else {
                    CardView cardSelecionado = holder.cardMaterial;
                    RecyclerView rvMaterial = (RecyclerView) holder.itemView.getParent();

                    int idMaterialCategoriaSelecionado = holder.cardMaterial.getTag().hashCode();

                    idMateriais.add(contagemSelecao, items.get(position).id);
                    contagemSelecao++;
                    listCategoriasPreenchidas.add(idMaterialCategoriaSelecionado);

                    if (cardSelecionado.isSelected()) {
                        for (int idCard = 0; idCard < items.size(); idCard++) {
                            CardView cardHabilitarTodos = rvMaterial.findViewById(idCard);
                            cardHabilitarTodos.setCardBackgroundColor(Color.parseColor("#005E49"));
                            cardHabilitarTodos.setSelected(false);
                            cardHabilitarTodos.setClickable(false);

                            listCategoriasPreenchidas.clear();
                            idMateriais.clear();
                            contagemSelecao = 0;
                        }
                    } else {
                        todasCategoriasPreenchidas = true;
                        for (int idCard = 0; idCard < items.size(); idCard++) {
                            CardView cardDesabilitar = rvMaterial.findViewById(idCard);

                            final int idCategoriaMaterialBuscar = cardDesabilitar.getTag().hashCode();

                            if (idMaterialCategoriaSelecionado == idCategoriaMaterialBuscar &&
                                    position != idCard) {
                                cardDesabilitar.setCardBackgroundColor(Color.parseColor("#000000"));
                                cardDesabilitar.setSelected(false);
                                cardDesabilitar.setClickable(true);
                            }

                            if (!listCategoriasPreenchidas.contains(idCategoriaMaterialBuscar)) {
                                todasCategoriasPreenchidas = false;
                            }
                        }

                        if (todasCategoriasPreenchidas) {
                            new MaterialActivity().irParaProdutoInformacao(context, true, idMateriais);
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

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
