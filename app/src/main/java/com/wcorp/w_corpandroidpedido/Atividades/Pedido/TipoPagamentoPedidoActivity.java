package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.IniciarDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
    public static final String VALORTOTAL = "com.example.w_corpandroidpedido.VALORTOTAL";
    private EditText txtValorPago;
    private Boolean firstOpen = true;
    private int valorPago;
    private Boolean isParcelado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_pagamento_pedido);

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        Intent intent = getIntent();

        isParcelado = intent.getBooleanExtra("isParcelado", false);

        txtValorPago = findViewById(R.id.txtValorPago);
        TextView txtNumeroComanda = findViewById(R.id.txtNumeroComanda);
        TextView txtValorComanda = findViewById(R.id.txtValorComanda);
        CardView cardViewInicio = findViewById(R.id.cardInicio);
        CardView cardViewPagamento = findViewById(R.id.cardPagamento);
        CardView cardViewComanda = findViewById(R.id.cardComanda);

        txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
        txtValorComanda.setText(formatNumero.format(dadosComanda.GetValorComanda()));
        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicio, cardViewPagamento,cardViewComanda);
        navegacaoBarraApp.addClick(this);

        addEventosClick(this);

        txtValorPago.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Não faz nada
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String typed = onlyDigits(charSequence.toString());
                valorPago = Integer.parseInt(typed);
                if (!TextUtils.isEmpty(typed)) {
                    txtValorPago.removeTextChangedListener(this);
                    double converted = Double.parseDouble(typed) / 100;
                    String convertedString = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(converted);
                    txtValorPago.setText(convertedString);
                    txtValorPago.setSelection(convertedString.length());
                    txtValorPago.addTextChangedListener(this);
                } else {
                    txtValorPago.setText("0");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                //Não faz nada
            }
        });

        if(isParcelado){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            int tipoPagamento = intent.getIntExtra("tipoPagamento", 0);
            int valorTotal = intent.getIntExtra("valorTotal", 0);
            int tipoParcela = intent.getIntExtra("tipoParcela", 0);
            int nmrParcela = intent.getIntExtra("nmrParcela", 0);
            txtValorPago.setText(String.valueOf(valorTotal));
            chamarPagamento(this,tipoPagamento,tipoParcela ,valorTotal, nmrParcela);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        firstOpen = true;
    }
    private void addEventosClick(Context context){
        CardView btnDebito = findViewById(R.id.cardDebito);
        CardView btnCredito = findViewById(R.id.cardCredito);
        CardView btnCreditoParcelado = findViewById(R.id.cardCreditoParcelado);
        CardView btnPix = findViewById(R.id.cardPix);

        btnDebito.setOnClickListener(btnCardView -> {
            chamarPagamento(context, btnCardView, PlugPag.TYPE_DEBITO, valorPago);
        });

        btnCredito.setOnClickListener(btnCardView -> {
            chamarPagamento(context , btnCardView, PlugPag.TYPE_CREDITO, valorPago);
        });

        btnCreditoParcelado.setOnClickListener(btnCardView -> {
            startActivityParcelamento(context);
        });

        btnPix.setOnClickListener(btnCardView -> {
            chamarPagamento(context, btnCardView, PlugPag.TYPE_PIX, valorPago);
        });
    }

    private static String onlyDigits(String textValue) {
        return textValue.replaceAll("[^\\d]", "");
    }

    private void startActivityParcelamento(Context context){
        Intent intent = new Intent(context, TipoCreditoActivity.class);
        intent.putExtra(VALORTOTAL, valorPago);
        context.startActivity(intent);
    }
    private void chamarPagamento(Context context, View btnClicado, int tipoPagamento, int valorPago) {
        btnClicado.setClickable(false);
        PagamentoCall pagamentoCall = new PagamentoCall();
        InfoPagamento infoPagamento = new InfoPagamento();

        infoPagamento.TipoPagamento = tipoPagamento;
        infoPagamento.ValorPago = valorPago;
        infoPagamento.TipoParcela = PlugPag.INSTALLMENT_TYPE_A_VISTA;
        infoPagamento.NumeroParcela = 1;

        pagamentoCall.EfetuarPagamento(context, infoPagamento);
        IniciarDialog(context, firstOpen);
        firstOpen = false;
        btnClicado.setClickable(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void chamarPagamento(Context context, int tipoPagamento, int tipoParcela ,int valorPago, int nmrParcela) {
        PagamentoCall pagamentoCall = new PagamentoCall();
        InfoPagamento infoPagamento = new InfoPagamento();

        infoPagamento.TipoPagamento = tipoPagamento;
        infoPagamento.ValorPago = valorPago;
        infoPagamento.TipoParcela = tipoParcela;
        infoPagamento.NumeroParcela = nmrParcela;

        pagamentoCall.EfetuarPagamento(context, infoPagamento);
        IniciarDialog(context, firstOpen);
        firstOpen = false;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}