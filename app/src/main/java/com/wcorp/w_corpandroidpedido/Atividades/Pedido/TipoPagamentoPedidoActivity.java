package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.IniciarDialog;
import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.MostrarDialog;

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
import androidx.datastore.rxjava2.RxDataStore;

import com.wcorp.w_corpandroidpedido.Atividades.CupomFiscal.CupomFiscalActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.DataStore;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.InfoPagamento;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import io.reactivex.Flowable;

public class TipoPagamentoPedidoActivity extends AppCompatActivity {

    private Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    public static final String VALORTOTAL = "com.example.w_corpandroidpedido.VALORTOTAL";
    private static boolean isCupomFiscal;
    private String CpfString;
    private String CnpjString;
    private EditText txtValorPago;
    private Boolean firstOpen = true;
    private int valorPago;
    private double valorPagoDouble;
    private Boolean isParcelado;
    private DecimalFormat decimalFormat = new DecimalFormat("#.00");
    private String bearer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_pagamento_pedido);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        Intent intent = getIntent();

        isCupomFiscal = intent.getBooleanExtra(CupomFiscalActivity.CUPOM_FISCAL, false);
        CpfString = intent.getStringExtra(CupomFiscalActivity.CUPOM_CPF);
        CnpjString = intent.getStringExtra(CupomFiscalActivity.CUPOM_CNPJ);
        isParcelado = intent.getBooleanExtra(TipoCreditoActivity.ISPARCELADO, false);

        txtValorPago = findViewById(R.id.txtValorPago);
        txtValorPago.setText(formatNumero.format(dadosComanda.GetValorComanda()));
        valorPago = Integer.parseInt(decimalFormat.format(dadosComanda.GetValorComanda()).replace(",", ""));
        valorPagoDouble = dadosComanda.GetValorComanda();

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
        addListenerTextoValor();

        if(isParcelado){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            int tipoPagamento = intent.getIntExtra("tipoPagamento", 0);
            int valorTotal = intent.getIntExtra(TipoCreditoActivity.VALORTOTAL, 0);
            int tipoParcela = intent.getIntExtra(TipoCreditoActivity.TIPOPARCELA, 0);
            int nmrParcela = intent.getIntExtra("nmrParcela", 0);
            txtValorPago.setText(String.valueOf(valorTotal));
            chamarPagamento(this, tipoPagamento, tipoParcela, valorTotal, nmrParcela);
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
            chamarPagamento(context, PlugPag.TYPE_DEBITO, PlugPag.INSTALLMENT_TYPE_A_VISTA, valorPago, 1);
        });

        btnCredito.setOnClickListener(btnCardView -> {
            chamarPagamento(context, PlugPag.TYPE_CREDITO, PlugPag.INSTALLMENT_TYPE_A_VISTA, valorPago,1);
        });

        btnCreditoParcelado.setOnClickListener(btnCardView -> {
            startActivityParcelamento(context);
        });

        btnPix.setOnClickListener(btnCardView -> {
            chamarPagamento(context, PlugPag.TYPE_PIX, PlugPag.INSTALLMENT_TYPE_A_VISTA, valorPago,1);
        });
    }
    private void addListenerTextoValor(){
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
                    valorPagoDouble = converted;
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

    private static String onlyDigits(String textValue) {
        return textValue.replaceAll("[^\\d]", "");
    }

    private void startActivityParcelamento(Context context){
        Intent intent = new Intent(context, TipoCreditoActivity.class);
        intent.putExtra(VALORTOTAL, valorPago);
        intent.putExtra(CupomFiscalActivity.CUPOM_FISCAL, isCupomFiscal);
        context.startActivity(intent);
    }
    private void chamarPagamento(Context context, int tipoPagamento, int tipoParcela ,int valorPago, int nmrParcela) {
        IniciarDialog(context, firstOpen);
        MostrarDialog(context, "Aguarde...");

        PagamentoCall pagamentoCall = new PagamentoCall();
        InfoPagamento infoPagamento = new InfoPagamento();

        infoPagamento.Bearer = bearer;
        infoPagamento.IdPedido = dadosComanda.GetPedido().retorno.id;
        infoPagamento.ValorPagoDouble = valorPagoDouble;
        infoPagamento.TipoPagamento = tipoPagamento;
        infoPagamento.ValorPago = valorPago;
        infoPagamento.TipoParcela = tipoParcela;
        infoPagamento.NumeroParcela = nmrParcela;
        infoPagamento.Cpf = CpfString;
        infoPagamento.Cnpj = CnpjString;

        pagamentoCall.EfetuarPagamento(context, infoPagamento, isCupomFiscal);

        firstOpen = false;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}