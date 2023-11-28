package com.example.w_corpandroidpedido.Menu;

import android.content.Context;
import android.content.Intent;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PagamentoPedidoActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidosActivity;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;


public class DadosComanda {
    public static Pedido pedidoAtual;
    public String numeroComanda = "9999999";
    public String valorComanda = "9.999.999";

    public DadosComanda(Pedido pedidoAtual){
        this.pedidoAtual = pedidoAtual;
        this.numeroComanda = pedidoAtual.retorno.comanda;
        this.valorComanda = String.valueOf(pedidoAtual.retorno.valorTotalPedido);
    }
}
