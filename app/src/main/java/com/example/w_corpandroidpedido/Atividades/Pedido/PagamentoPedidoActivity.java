package com.example.w_corpandroidpedido.Atividades.Pedido;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        CardView getCardView = findViewById(R.id.cardPagamentoDividido);
        ImageView arrow = findViewById(R.id.arrow_button);
        LinearLayout hiddenView = findViewById(R.id.hidden_view);
        EditText nmrDivPessoas = findViewById(R.id.qtdPessoas);
        TextView txtValorDivPessoas = findViewById(R.id.txtValorDivPessoas);
        Button btnCalcular = findViewById(R.id.btnCalcular);

        arrow.setOnClickListener(view -> {
            TextView txtValorTotal = findViewById(R.id.txtValorTotal);
            txtValorTotal.setText(String.format(java.util.Locale.US,"%.2f",dadosComanda.GetValorComanda()));

            if (hiddenView.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(getCardView,
                        new AutoTransition());
                hiddenView.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.baseline_arrow_drop_down_24);
            } else {

                TransitionManager.beginDelayedTransition(getCardView,
                        new AutoTransition());
                hiddenView.setVisibility(View.VISIBLE);
                arrow.setImageResource(R.drawable.baseline_arrow_drop_up_24);
            }
        });

        btnCalcular.setOnClickListener(view ->{
            try{
                float nmrDivisao = Integer.parseInt(nmrDivPessoas.getText().toString());
                double resultadoDivisaoPessoas = dadosComanda.GetValorComanda() / nmrDivisao;

                txtValorDivPessoas.setText(String.format(java.util.Locale.US,"%.2f",resultadoDivisaoPessoas));
            }catch (Exception e){
                Toast.makeText(this, "Necessário colocar a quantidade de pessoas!", Toast.LENGTH_SHORT).show();
            }
        });

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
            txtValorComanda.setText(String.format(java.util.Locale.US,"%,.2f",dadosComanda.GetValorComanda()));
        }else{
            txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
            txtValorComanda.setText(String.format(java.util.Locale.US,"%,.2f",dadosComanda.GetValorComanda()));
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




