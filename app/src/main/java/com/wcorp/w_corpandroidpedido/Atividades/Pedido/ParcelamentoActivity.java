package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Pedido.ParcelamentoAdapter;

import java.util.ArrayList;

public class ParcelamentoActivity extends AppCompatActivity {

    private int valorTotal;
    private ArrayList<Double> valoresParcelados = new ArrayList<>();
    private RecyclerView getRecycleParcelamento;
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

        popularParcelamento(this);
    }

    private void popularParcelamento(Context context){
        getRecycleParcelamento = findViewById(R.id.viewCardParcelamento);
        getRecycleParcelamento.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecycleParcelamento.setHasFixedSize(true);

        getRecycleParcelamento.setAdapter(new ParcelamentoAdapter(context, valorTotal ,valoresParcelados));
    }
}