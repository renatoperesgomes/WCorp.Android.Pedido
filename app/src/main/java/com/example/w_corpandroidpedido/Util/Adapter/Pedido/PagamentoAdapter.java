package com.example.w_corpandroidpedido.Util.Adapter.Pedido;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Atividades.Material.MaterialActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PagamentoPedidoActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidoActivity;
import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Pedido.RemoverPedidoItemService;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;


import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Flowable;

public class PagamentoAdapter extends RecyclerView.Adapter<PagamentoAdapter.PagamentoViewHolder> {
    private Context context;
    private Pedido pedidoAtual;
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
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

    @Override
    public void onBindViewHolder(@NonNull PagamentoViewHolder holder, int position) {
        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        if(pedidoAtual == null){
            holder.nomeMaterial.setText("Não contém nenhum item adicionado");
            holder.valorMaterial.setText("0,00");
            holder.qtdMaterial.setText("0 Un.");
            holder.btnExcluirItem.setVisibility(View.GONE);
        }else{
            holder.nomeMaterial.setText(String.valueOf(pedidoAtual.retorno.listPedidoMaterialItem.get(position).material.nome));
            holder.valorMaterial.setText(formatNumero.format(pedidoAtual.retorno.listPedidoMaterialItem.get(position).valorUnitario));
            holder.qtdMaterial.setText(String.valueOf(pedidoAtual.retorno.listPedidoMaterialItem.get(position).quantidade));

            holder.btnExcluirItem.setOnClickListener(view -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Atenção");
                alert.setMessage("Você deseja excluir este item?" + "\n" + pedidoAtual.retorno.listPedidoMaterialItem.get(position).material.nome);
                alert.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new PagamentoPedidoActivity().ExcluirPedidoMaterialItem(context, bearer ,pedidoAtual.retorno.listPedidoMaterialItem.get(position).id);
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
