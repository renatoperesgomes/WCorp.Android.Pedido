package com.example.w_corpandroidpedido.Navegacao;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidosActivity;

public class NavegacaoBarraApp {

    public static void irPaginaInicial(Context context){
        Intent intent = new Intent(context, CategoriaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void irPaginaPagamento(Context context){
        Toast.makeText(context, "Clicou no pagamento!", Toast.LENGTH_SHORT).show();
    }

    public static void irPaginaPesquisaComanda(Context context){
        Intent intent = new Intent(context, PesquisarPedidosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        Toast.makeText(context, "Clicou na comanda!", Toast.LENGTH_SHORT).show();
    }
}
