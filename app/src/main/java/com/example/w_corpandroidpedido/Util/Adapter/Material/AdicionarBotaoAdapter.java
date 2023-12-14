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

import com.example.w_corpandroidpedido.R;

import java.util.ArrayList;

public class AdicionarBotaoAdapter extends RecyclerView.Adapter<AdicionarBotaoAdapter.AdicionarBotaoViewHolder> {
    private Context context;
    private ArrayList<AdicionarBotaoViewHolder> listCardQuantidade = new ArrayList<>();
    int txtQuantidadeCard = 1;
    int valorQtdItemDivisao = 0;

    public AdicionarBotaoAdapter(Context context){
        this.context = context;;
    }
    @NonNull
    @Override
    public AdicionarBotaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.botao_quantidade, parent, false);
        AdicionarBotaoViewHolder adicionarQuantidadeBotaoViewHolder = new AdicionarBotaoViewHolder(itemLista);
        listCardQuantidade.add(adicionarQuantidadeBotaoViewHolder);

        if(getItemCount() % 2 == 0){
            valorQtdItemDivisao = getItemCount() / 2;
        }else {
            valorQtdItemDivisao = (getItemCount() / 2) + 1;
        }
        return adicionarQuantidadeBotaoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdicionarBotaoViewHolder holder, int position) {
        holder.cardQuantidade.setCardBackgroundColor(Color.parseColor("#001c13"));

        if(position == 0){
            holder.cardQuantidade.setCardBackgroundColor(Color.parseColor("#009574"));
            holder.cardQuantidade.setSelected(true);
        }

        if(position % 2 == 1){
            holder.txtViewQuantidade.setText(String.valueOf(txtQuantidadeCard + valorQtdItemDivisao));
            holder.cardQuantidade.setId(txtQuantidadeCard + valorQtdItemDivisao);
            txtQuantidadeCard++;
        }else{
            holder.txtViewQuantidade.setText(String.valueOf(txtQuantidadeCard));
            holder.cardQuantidade.setId(txtQuantidadeCard);
        }

        holder.cardQuantidade.setOnClickListener(view ->{
            for (AdicionarBotaoViewHolder botaoQuantidadeViewHolder:
                    listCardQuantidade) {
                botaoQuantidadeViewHolder.cardQuantidade.setCardBackgroundColor(Color.parseColor("#001c13"));
                botaoQuantidadeViewHolder.cardQuantidade.setSelected(false);
            }

            listCardQuantidade.get(position).cardQuantidade.setSelected(true);
            listCardQuantidade.get(position).cardQuantidade.setCardBackgroundColor(Color.parseColor("#009574"));
        });
    }
    @Override
    public int getItemCount() {
        return 10;
    }

    static class AdicionarBotaoViewHolder extends RecyclerView.ViewHolder{
        CardView cardQuantidade = itemView.findViewById(R.id.cardQuantidade);
        TextView txtViewQuantidade = itemView.findViewById(R.id.txtViewQuantidade);
        public AdicionarBotaoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
