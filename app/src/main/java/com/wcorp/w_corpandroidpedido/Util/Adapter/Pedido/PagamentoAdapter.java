package com.wcorp.w_corpandroidpedido.Util.Adapter.Pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.wcorp.w_corpandroidpedido.Atividades.Pedido.PagamentoPedidoActivity;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.R;

import java.text.NumberFormat;
import java.util.Locale;

public class PagamentoAdapter extends RecyclerView.Adapter<PagamentoAdapter.PagamentoViewHolder> {
    private Context context;
    private Pedido pedidoAtual;
    private String bearer;
    public PagamentoAdapter(Context context, String bearer ,Pedido pedidoAtual){
        this.context = context;
        this.bearer = bearer;
        this.pedidoAtual = pedidoAtual;
    }
    @NonNull
    @Override
    public PagamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.lista_pagamento_produto, parent, false);
        return new PagamentoViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PagamentoViewHolder holder, int position) {
        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        if(pedidoAtual == null){
            holder.nomeMaterial.setText("Não contém item");
            holder.valorMaterial.setText("0,00");
            holder.qtdMaterial.setText("0 Un.");
            holder.btnExcluirItem.setVisibility(View.GONE);
        }else{
            holder.nomeMaterial.setText(String.valueOf(pedidoAtual.retorno.listPedidoMaterialItem.get(position).material.nome));
            holder.valorMaterial.setText(formatNumero.format(pedidoAtual.retorno.listPedidoMaterialItem.get(position).valorUnitario));
            holder.qtdMaterial.setText(String.format(new Locale("pt", "BR"),"%.2f",pedidoAtual.retorno.listPedidoMaterialItem.get(position).quantidade));

            holder.btnExcluirItem.setOnClickListener(view -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Atenção");
                alert.setMessage("Você deseja excluir este item?" + "\n" + pedidoAtual.retorno.listPedidoMaterialItem.get(holder.getAbsoluteAdapterPosition()).material.nome);
                alert.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new PagamentoPedidoActivity().ExcluirPedidoMaterialItem(context, bearer ,pedidoAtual.retorno.listPedidoMaterialItem.get(holder.getAbsoluteAdapterPosition()).id);
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

    static class PagamentoViewHolder extends RecyclerView.ViewHolder{
        TextView qtdMaterial = itemView.findViewById(R.id.qtdMaterial);
        TextView nomeMaterial = itemView.findViewById(R.id.nomeMaterial);
        TextView valorMaterial = itemView.findViewById(R.id.valorMaterial);
        ImageButton btnExcluirItem = itemView.findViewById(R.id.btnImageExcluir);
        public PagamentoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
