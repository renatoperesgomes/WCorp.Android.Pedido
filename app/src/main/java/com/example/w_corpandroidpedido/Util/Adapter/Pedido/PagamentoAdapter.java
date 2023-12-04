package com.example.w_corpandroidpedido.Util.Adapter.Pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.R;

public class PagamentoAdapter extends RecyclerView.Adapter<PagamentoAdapter.PagamentoViewHolder> {
    private Context context;
    private Pedido pedidoAtual;
    public PagamentoAdapter(Context context, Pedido pedidoAtual){
        this.context = context;
        this.pedidoAtual = pedidoAtual;
    }
    @NonNull
    @Override
    public PagamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.listaprodutos, parent, false);
        return new PagamentoViewHolder(itemLista);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull PagamentoViewHolder holder, int position) {
        if(pedidoAtual == null){
            holder.idMaterial.setText(String.valueOf(0));
            holder.nomeMaterial.setText("Não contém nenhum item adicionado");
            holder.valorMaterial.setText("0,00");
            holder.qtdMaterial.setText("0 Un.");
        }else{
            holder.idMaterial.setText(String.valueOf(pedidoAtual.retorno.listPedidoMaterialItem.get(position).idMaterial));
            holder.nomeMaterial.setText(String.valueOf(pedidoAtual.retorno.listPedidoMaterialItem.get(position).material.nome));
            holder.valorMaterial.setText("R$ " + String.format("%.2f", pedidoAtual.retorno.listPedidoMaterialItem.get(position).valorUnitario));
            holder.qtdMaterial.setText(String.valueOf(pedidoAtual.retorno.listPedidoMaterialItem.get(position).quantidade));
        }
    }

    @Override
    public int getItemCount() {
        if(pedidoAtual == null){
            return 1;
        }
        return pedidoAtual.retorno.listPedidoMaterialItem.size();
    }

    class PagamentoViewHolder extends RecyclerView.ViewHolder{
        TextView idMaterial = itemView.findViewById(R.id.idMaterial);
        TextView nomeMaterial = itemView.findViewById(R.id.nomeMaterial);
        TextView valorMaterial = itemView.findViewById(R.id.valorMaterial);
        TextView qtdMaterial = itemView.findViewById(R.id.qtdMaterial);
        public PagamentoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
