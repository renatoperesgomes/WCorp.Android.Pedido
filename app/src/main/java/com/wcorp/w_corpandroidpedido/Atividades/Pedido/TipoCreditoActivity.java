package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

import com.wcorp.w_corpandroidpedido.Atividades.CupomFiscal.CupomFiscalActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.R;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;

public class TipoCreditoActivity extends AppCompatActivity {

    private Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    public static final String TIPOPARCELA = "com.example.w_corpandroidpedido.TIPOCREDITO";
    public static final String VALORTOTAL = "com.example.w_corpandroidpedido.VALORTOTAL";
    public static final String ISPARCELADO = "com.example.w_corpandroidpedido.ISPARCELADO";
    private boolean isCupomFiscal;
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private int valorTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_credito);

        Intent intent = getIntent();
        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        valorTotal =  intent.getIntExtra(TipoPagamentoPedidoActivity.VALORTOTAL, 0);
        isCupomFiscal =  intent.getBooleanExtra(CupomFiscalActivity.CUPOM_FISCAL, false);

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
    }

    private void addEventosClick(Context context){
        CardView creditoComprador = findViewById(R.id.cardCreditoComprador);
        CardView creditoVendedor = findViewById(R.id.cardCreditoVendedor);
        Intent intent = new Intent(context, ParcelamentoActivity.class);

        creditoComprador.setOnClickListener(view ->{
            intent.putExtra(TIPOPARCELA, PlugPag.INSTALLMENT_TYPE_PARC_COMPRADOR);
            intent.putExtra(VALORTOTAL, valorTotal);
            intent.putExtra(CupomFiscalActivity.CUPOM_FISCAL, isCupomFiscal);
            context.startActivity(intent);
        });

        creditoVendedor.setOnClickListener(view ->{
            intent.putExtra(TIPOPARCELA, PlugPag.INSTALLMENT_TYPE_PARC_VENDEDOR);
            intent.putExtra(VALORTOTAL, valorTotal);
            intent.putExtra(CupomFiscalActivity.CUPOM_FISCAL, isCupomFiscal);
            context.startActivity(intent);
        });

    }
}