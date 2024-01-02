package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.DataStore;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.InfoPagamento;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagActivationData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagAppIdentification;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInitializationResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPaymentData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagTransactionResult;
import io.reactivex.Flowable;

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
        System.out.println(valorPago);

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
            InfoPagamento infoPagamento = new InfoPagamento();
            PagamentoCall pagamentoCall = new PagamentoCall();

            infoPagamento.TipoPagamento = PlugPag.TYPE_DEBITO;
            infoPagamento.ValorPago = valorPago;
            infoPagamento.TipoParcela = PlugPag.INSTALLMENT_TYPE_A_VISTA;
            infoPagamento.NumeroParcela = 1;

            pagamentoCall.EfetuarPagamento(this, infoPagamento);
        });

        btnCredito.setOnClickListener(view ->{
            InfoPagamento infoPagamento = new InfoPagamento();
            PagamentoCall pagamentoCall = new PagamentoCall();

            infoPagamento.TipoPagamento = PlugPag.TYPE_CREDITO;
            infoPagamento.ValorPago = valorPago;
            infoPagamento.TipoParcela = PlugPag.INSTALLMENT_TYPE_A_VISTA;
            infoPagamento.NumeroParcela = 1;

            pagamentoCall.EfetuarPagamento(this, infoPagamento);
        });

        btnCreditoParcelado.setOnClickListener(view ->{
            InfoPagamento infoPagamento = new InfoPagamento();
            PagamentoCall pagamentoCall = new PagamentoCall();

            infoPagamento.TipoPagamento = PlugPag.TYPE_CREDITO;
            infoPagamento.ValorPago = valorPago;
            infoPagamento.TipoParcela = PlugPag.INSTALLMENT_TYPE_PARC_VENDEDOR;
            infoPagamento.NumeroParcela = 3;

            pagamentoCall.EfetuarPagamento(this, infoPagamento);
        });

        btnPix.setOnClickListener(view ->{
            InfoPagamento infoPagamento = new InfoPagamento();
            PagamentoCall pagamentoCall = new PagamentoCall();

            infoPagamento.TipoPagamento = PlugPag.TYPE_PIX;
            infoPagamento.ValorPago = valorPago;
            infoPagamento.TipoParcela = PlugPag.INSTALLMENT_TYPE_A_VISTA;
            infoPagamento.NumeroParcela = 1;

            pagamentoCall.EfetuarPagamento(this, infoPagamento);
        });
    }
}