package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.IniciarDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Pedido.ParcelamentoAdapter;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.InfoPagamento;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PlugPagInstance;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagEventData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagEventListener;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInstallment;

public class ParcelamentoActivity extends AppCompatActivity {

    private int valorTotal;
    private ArrayList<Double> valoresParcelados = new ArrayList<>();
    private RecyclerView getRecycleParcelamento;
    private Boolean firstOpen = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelamento);

        Intent intent = getIntent();
        valorTotal =  intent.getIntExtra(TipoPagamentoPedidoActivity.VALORTOTAL, 0);

        for(double i = 2; i <= 12; i++){
            double valorPagoConvertido = valorTotal / 100.00;
            double resultadoValorParcConvertido = valorPagoConvertido / i;
            valoresParcelados.add(resultadoValorParcConvertido);
        }

        popularParcelamento();
    }

    private void popularParcelamento(){
        getRecycleParcelamento = findViewById(R.id.viewCardParcelamento);
        getRecycleParcelamento.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecycleParcelamento.setHasFixedSize(true);

        getRecycleParcelamento.setAdapter(new ParcelamentoAdapter(this, valorTotal ,valoresParcelados));
    }

    public void ChamarPagamento(Context context, View btnClicado, int tipoPagamento, int valorPago, int tipoParcela, int numeroParcela) {
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