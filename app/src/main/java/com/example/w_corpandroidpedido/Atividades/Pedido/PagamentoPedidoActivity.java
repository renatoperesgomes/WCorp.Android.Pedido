package com.example.w_corpandroidpedido.Atividades.Pedido;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Util.Adapter.Pedido.PagamentoAdapter;

public class PagamentoPedidoActivity extends AppCompatActivity {
    private RecyclerView getRecyclerViewPagamento;
    private Button getBtnVoltar;
    private Button getBtnFazerPagamento;
    private Pedido pedidoAtual = DadosComanda.pedidoAtual;

    private DadosComanda dadosComanda = PesquisarPedidoActivity.dadosComanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_pedido);

        CardView inicio = findViewById(R.id.cardInicio);
        CardView pagamento = findViewById(R.id.cardPagamento);
        CardView comanda = findViewById(R.id.cardComanda);
        TextView txtNumeroComanda = findViewById(R.id.txtIdComanda);
        TextView txtValorComanda = findViewById(R.id.txtValorComanda);

        getRecyclerViewPagamento = findViewById(R.id.viewCarrinhoPagamento);

        getRecyclerViewPagamento.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecyclerViewPagamento.setHasFixedSize(true);

        getRecyclerViewPagamento.setAdapter(new PagamentoAdapter(this, pedidoAtual));

        getBtnVoltar = findViewById(R.id.btnVoltar);

        getBtnFazerPagamento =findViewById(R.id.btnFazerPagamento);


        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(inicio, pagamento,comanda);
        navegacaoBarraApp.addClick(this);

        if(pedidoAtual != null){
            txtNumeroComanda.setText(pedidoAtual.retorno.comanda);
            txtValorComanda.setText(String.valueOf(pedidoAtual.retorno.valorTotalPedido));
        }else{
            txtNumeroComanda.setText(dadosComanda.numeroComanda);
            txtValorComanda.setText(dadosComanda.valorComanda);
            getBtnFazerPagamento.setOnClickListener(view ->{
                Toast.makeText(this,"Necessário adicionar um material para fazer o pagamento.",Toast.LENGTH_SHORT).show();
            });
        }

        getBtnVoltar.setOnClickListener(view ->{
            voltarParaPaginaInicial();
        });
    }

    private void voltarParaPaginaInicial(){
        finish();
    }
}