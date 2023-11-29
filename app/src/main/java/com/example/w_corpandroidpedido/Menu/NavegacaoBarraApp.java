package com.example.w_corpandroidpedido.Menu;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PagamentoPedidoActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidoActivity;


public class NavegacaoBarraApp {
    CardView inicio;
    CardView pagamento;
    CardView comanda;

    public NavegacaoBarraApp(CardView inicio, CardView pagamento, CardView comanda){
        this.inicio = inicio;
        this.pagamento = pagamento;
        this.comanda = comanda;
    }

    public void addClick(Context context){
        inicio.setOnClickListener(view->{
            Intent intent = new Intent(context, CategoriaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });

        pagamento.setOnClickListener(view->{
            Intent intent = new Intent(context, PagamentoPedidoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });

        comanda.setOnClickListener(view->{
            Intent intent = new Intent(context, PesquisarPedidoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });
    }

    public void addClickError(Context context){
        inicio.setOnClickListener(view->{
            Toast.makeText(context, "É necessário criar/abrir uma comanda!", Toast.LENGTH_SHORT).show();
        });

        pagamento.setOnClickListener(view->{
            Toast.makeText(context, "É necessário criar/abrir uma comanda!", Toast.LENGTH_SHORT).show();
        });

        comanda.setOnClickListener(view->{
            Toast.makeText(context, "É necessário criar/abrir uma comanda!", Toast.LENGTH_SHORT).show();
        });
    }
}
