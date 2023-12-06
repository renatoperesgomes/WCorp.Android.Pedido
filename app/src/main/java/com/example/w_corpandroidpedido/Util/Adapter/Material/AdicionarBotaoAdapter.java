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

import com.example.w_corpandroidpedido.R;

import java.util.ArrayList;

public class AdicionarBotaoAdapter extends RecyclerView.Adapter<AdicionarBotaoAdapter.AdicionarBotaoViewHolder> {
    private Context context;
    private ArrayList<AdicionarBotaoViewHolder> listCardQuantidade = new ArrayList<>();
    int nmrQuantidade = 1;

    public AdicionarBotaoAdapter(Context context){
        this.context = context;;
    }
    @NonNull
    @Override
    public AdicionarBotaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.botao_quantidade, parent, false);
        AdicionarBotaoViewHolder pagamentoBotaoViewHolder = new AdicionarBotaoViewHolder(itemLista);
        listCardQuantidade.add(pagamentoBotaoViewHolder);
        return pagamentoBotaoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdicionarBotaoViewHolder holder, int position) {

        if(position % 2 == 1){
            holder.txtQuantidade.setText(String.valueOf(nmrQuantidade + 5));
            holder.btnQuantidade.setId(nmrQuantidade + 5);
            nmrQuantidade++;
        }else{
            holder.txtQuantidade.setText(String.valueOf(nmrQuantidade));
            holder.btnQuantidade.setId(nmrQuantidade);
        }

        holder.btnQuantidade.setOnClickListener(view ->{

            if(listCardQuantidade.get(position).btnQuantidade.isSelected()){
                for (AdicionarBotaoViewHolder botaoQuantidadeViewHolder:
                        listCardQuantidade) {
                     botaoQuantidadeViewHolder.btnQuantidade.setCardBackgroundColor(Color.parseColor("#005E49"));
                     botaoQuantidadeViewHolder.btnQuantidade.setClickable(true);
                     botaoQuantidadeViewHolder.btnQuantidade.setSelected(false);
                }

            }else{
                listCardQuantidade.get(position).btnQuantidade.setCardBackgroundColor(Color.parseColor("#009574"));
                listCardQuantidade.get(position).btnQuantidade.setSelected(true);

                for (AdicionarBotaoViewHolder botaoQuantidadeViewHolder:
                     listCardQuantidade) {
                    if(!botaoQuantidadeViewHolder.btnQuantidade.isSelected()){
                        botaoQuantidadeViewHolder.btnQuantidade.setCardBackgroundColor(Color.parseColor("#001c13"));
                        botaoQuantidadeViewHolder.btnQuantidade.setClickable(false);
                        botaoQuantidadeViewHolder.btnQuantidade.setSelected(false);
                    }
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return 10;
    }

    static class AdicionarBotaoViewHolder extends RecyclerView.ViewHolder{
        CardView btnQuantidade = itemView.findViewById(R.id.cardQuantidade);
        TextView txtQuantidade = itemView.findViewById(R.id.valorQtd);
        public AdicionarBotaoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
