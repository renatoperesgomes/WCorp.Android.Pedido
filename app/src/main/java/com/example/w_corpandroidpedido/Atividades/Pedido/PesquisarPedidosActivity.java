package com.example.w_corpandroidpedido.Atividades.Pedido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.w_corpandroidpedido.Navegacao.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.R;

public class PesquisarPedidosActivity extends AppCompatActivity {
    private EditText pesquisarComanda;
    private Button btnPesquisar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        pesquisarComanda = findViewById(R.id.textPesquisarComanda);
        btnPesquisar = findViewById(R.id.btnPesquisar);

        btnPesquisar.setOnClickListener(view -> {
            String nmrComanda = pesquisarComanda.getText().toString();
            System.out.println(nmrComanda);
        });

        CardView inicio = findViewById(R.id.cardInicio);
        inicio.setOnClickListener(view->{
            NavegacaoBarraApp.irPaginaInicial(this);
        });

        CardView pagamento = findViewById(R.id.cardPagamento);
        pagamento.setOnClickListener(view->{
            NavegacaoBarraApp.irPaginaPagamento(this);
        });

        CardView comanda = findViewById(R.id.cardComanda);
        comanda.setOnClickListener(view->{
            NavegacaoBarraApp.irPaginaPesquisaComanda(this);
        });
    }

}