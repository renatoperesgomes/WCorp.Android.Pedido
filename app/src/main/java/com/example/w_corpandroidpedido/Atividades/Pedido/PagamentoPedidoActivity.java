package com.example.w_corpandroidpedido.Atividades.Pedido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Util.Adapter.Pedido.PagamentoAdapter;

public class PagamentoPedidoActivity extends AppCompatActivity {
    private RecyclerView getRecyclerViewPagamento;
    private Pedido pedidoAtual = DadosComanda.pedidoAtual;
    private Button getBtnVoltar;

    private DadosComanda dadosComanda = PesquisarPedidosActivity.dadosComanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_pedido);

        CardView inicio = findViewById(R.id.cardInicio);
        CardView pagamento = findViewById(R.id.cardPagamento);
        CardView comanda = findViewById(R.id.cardComanda);
        TextView numeroComanda = findViewById(R.id.txtIdComanda);
        TextView valorComanda = findViewById(R.id.txtValorComanda);

        getRecyclerViewPagamento = findViewById(R.id.viewCarrinhoPagamento);

        getRecyclerViewPagamento.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecyclerViewPagamento.setHasFixedSize(true);

        getRecyclerViewPagamento.setAdapter(new PagamentoAdapter(this, pedidoAtual));


        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(inicio, pagamento,comanda);
        navegacaoBarraApp.addClick(this);

        if(dadosComanda != null){
            numeroComanda.setText(dadosComanda.numeroComanda);
            valorComanda.setText(dadosComanda.valorComanda);
        }

        getBtnVoltar = findViewById(R.id.btnVoltar);

        getBtnVoltar.setOnClickListener(view ->{
            voltarParaPaginaInicial();
        });
    }

    private void voltarParaPaginaInicial(){
        finish();
    }
}