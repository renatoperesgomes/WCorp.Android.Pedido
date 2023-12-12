package com.example.w_corpandroidpedido.Util.Adapter.Pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
        View itemLista = LayoutInflater.from(context).inflate(R.layout.lista_pagamento_produto, parent, false);
        return new PagamentoViewHolder(itemLista);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull PagamentoViewHolder holder, int position) {
        if(pedidoAtual == null){
            holder.nomeMaterial.setText("Não contém nenhum item adicionado");
            holder.valorMaterial.setText("0,00");
            holder.qtdMaterial.setText("0 Un.");
        }else{
            holder.nomeMaterial.setText(String.valueOf(pedidoAtual.retorno.listPedidoMaterialItem.get(position).material.nome));
            holder.valorMaterial.setText("R$ " + String.format("%.2f", pedidoAtual.retorno.listPedidoMaterialItem.get(position).valorUnitario));
            holder.qtdMaterial.setText(String.valueOf(pedidoAtual.retorno.listPedidoMaterialItem.get(position).quantidade));

            holder.btnExcluirMaterial.setOnClickListener(view ->{
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Atenção");
                alert.setMessage("Você deseja excluir este item?");
                alert.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Item excluido!", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("Cancelar", null);
                alert.show();
            });
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
        TextView qtdMaterial = itemView.findViewById(R.id.qtdMaterial);
        TextView nomeMaterial = itemView.findViewById(R.id.nomeMaterial);
        TextView valorMaterial = itemView.findViewById(R.id.valorMaterial);
        ImageView btnExcluirMaterial = itemView.findViewById(R.id.imageExcluir);
        public PagamentoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
