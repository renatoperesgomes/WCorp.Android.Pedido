package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.IniciarDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.InfoPagamento;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;

public class TipoPagamentoPedidoActivity extends AppCompatActivity {

    private Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private int valorPago;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_pagamento_pedido);

        Intent intent = getIntent();

        valorPago = intent.getIntExtra(PagamentoPedidoActivity.VALORPAGO, 0);

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        TextView txtNumeroComanda = findViewById(R.id.txtNumeroComanda);
        TextView txtValorComanda = findViewById(R.id.txtValorComanda);
        CardView cardViewInicio = findViewById(R.id.cardInicio);
        CardView cardViewPagamento = findViewById(R.id.cardPagamento);
        CardView cardViewComanda = findViewById(R.id.cardComanda);
        CardView btnDebito = findViewById(R.id.cardDebito);
        CardView btnCredito = findViewById(R.id.cardCredito);
        CardView btnCreditoParcelado = findViewById(R.id.cardCreditoParcelado);
        CardView btnPix = findViewById(R.id.cardPix);

        txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
        txtValorComanda.setText(formatNumero.format(dadosComanda.GetValorComanda()));
        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicio, cardViewPagamento,cardViewComanda);
        navegacaoBarraApp.addClick(this);

        btnDebito.setOnClickListener(view ->{
            chamarPagamento(PlugPag.TYPE_DEBITO, valorPago, PlugPag.INSTALLMENT_TYPE_A_VISTA, 1);
        });

        btnCredito.setOnClickListener(view ->{
            chamarPagamento(PlugPag.TYPE_CREDITO, valorPago, PlugPag.INSTALLMENT_TYPE_A_VISTA, 1);
        });

        btnCreditoParcelado.setOnClickListener(view ->{
            chamarPagamento(PlugPag.TYPE_CREDITO, valorPago, PlugPag.INSTALLMENT_TYPE_PARC_VENDEDOR, 3);
        });

        btnPix.setOnClickListener(view ->{
            chamarPagamento(PlugPag.TYPE_PIX, valorPago, PlugPag.INSTALLMENT_TYPE_A_VISTA, 1);
        });
    }

    private void chamarPagamento(int tipoPagamento, int valorPago, int tipoParcela, int numeroParcela){
        PagamentoCall pagamentoCall = new PagamentoCall();
        InfoPagamento infoPagamento = new InfoPagamento();

        infoPagamento.TipoPagamento = tipoPagamento;
        infoPagamento.ValorPago = valorPago;
        infoPagamento.TipoParcela = tipoParcela;
        infoPagamento.NumeroParcela = numeroParcela;

        IniciarDialog(this);

        pagamentoCall.EfetuarPagamento(this, infoPagamento);
    }
}