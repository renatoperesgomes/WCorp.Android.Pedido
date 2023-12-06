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
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Util.Adapter.Pedido.PagamentoAdapter;

public class PagamentoPedidoActivity extends AppCompatActivity {
    private RecyclerView getRecyclerViewPagamento;

    private Button getBtnVoltar;
    private Button getBtnFazerPagamento;
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_pedido);

        CardView cardViewInicio = findViewById(R.id.cardInicio);
        CardView cardViewPagamento = findViewById(R.id.cardPagamento);
        CardView cardViewComanda = findViewById(R.id.cardComanda);
        TextView txtNumeroComanda = findViewById(R.id.txtIdComanda);
        TextView txtValorComanda = findViewById(R.id.txtValorComanda);

        getRecyclerViewPagamento = findViewById(R.id.viewCarrinhoPagamento);


        getRecyclerViewPagamento.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecyclerViewPagamento.setHasFixedSize(true);
        getRecyclerViewPagamento.setAdapter(new PagamentoAdapter(this, dadosComanda.GetPedido()));


        getBtnVoltar = findViewById(R.id.btnVoltar);

        getBtnFazerPagamento = findViewById(R.id.btnFazerPagamento);


        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicio, cardViewPagamento,cardViewComanda);
        navegacaoBarraApp.addClick(this);

        if(dadosComanda.GetPedido() != null){
            txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
            txtValorComanda.setText(String.valueOf(dadosComanda.GetValorComanda()));
        }else{
            txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
            txtValorComanda.setText(dadosComanda.GetValorComanda());
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