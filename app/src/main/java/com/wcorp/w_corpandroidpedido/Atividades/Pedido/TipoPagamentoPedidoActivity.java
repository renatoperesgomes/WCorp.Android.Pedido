package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.IniciarDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

import com.wcorp.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.wcorp.w_corpandroidpedido.Atividades.Categoria.SubCategoriaActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.InfoPagamento;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;

public class TipoPagamentoPedidoActivity extends AppCompatActivity {

    private Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    public static final String VALORTOTAL = "com.example.w_corpandroidpedido.VALORTOTAL";
    private EditText txtValorPago;
    private Boolean firstOpen = true;
    private DecimalFormat decimalFormat = new DecimalFormat("#,##");
    private int valorPago;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_pagamento_pedido);

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

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
    }

    private void addEventosClick(Context context){
        CardView btnDebito = findViewById(R.id.cardDebito);
        CardView btnCredito = findViewById(R.id.cardCredito);
        CardView btnCreditoParcelado = findViewById(R.id.cardCreditoParcelado);
        CardView btnPix = findViewById(R.id.cardPix);

        btnDebito.setOnClickListener(btnCardView -> {
            chamarPagamento(context, btnCardView, PlugPag.TYPE_DEBITO, valorPago, PlugPag.INSTALLMENT_TYPE_A_VISTA, 1);
        });

        btnCredito.setOnClickListener(btnCardView -> {
            chamarPagamento(context , btnCardView, PlugPag.TYPE_CREDITO, valorPago, PlugPag.INSTALLMENT_TYPE_A_VISTA, 1);
        });

        btnCreditoParcelado.setOnClickListener(btnCardView -> {
            startActivityParcelamento(context);
        });

        btnPix.setOnClickListener(btnCardView -> {
            chamarPagamento(context, btnCardView, PlugPag.TYPE_PIX, valorPago, PlugPag.INSTALLMENT_TYPE_A_VISTA, 1);
        });
    }

    private static String onlyDigits(String textValue) {
        return textValue.replaceAll("[^\\d]", "");
    }

    private void startActivityParcelamento(Context context){
        Intent intent = new Intent(context, ParcelamentoActivity.class);
        intent.putExtra(VALORTOTAL, valorPago);
        context.startActivity(intent);
    }
    private void chamarPagamento(Context context, View btnClicado, int tipoPagamento, int valorPago, int tipoParcela, int numeroParcela) {
        btnClicado.setClickable(false);
        PagamentoCall pagamentoCall = new PagamentoCall();
        InfoPagamento infoPagamento = new InfoPagamento();

        infoPagamento.TipoPagamento = tipoPagamento;
        infoPagamento.ValorPago = valorPago;
        infoPagamento.TipoParcela = tipoParcela;
        infoPagamento.NumeroParcela = numeroParcela;

        pagamentoCall.EfetuarPagamento(context, infoPagamento);
        IniciarDialog(context, firstOpen);
        firstOpen = false;
        btnClicado.setClickable(true);
    }
}