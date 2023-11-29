package com.example.w_corpandroidpedido.Menu;

import com.example.w_corpandroidpedido.Models.Pedido.Pedido;


public class DadosComanda {
    public static Pedido pedidoAtual;
    public String numeroComanda = "9999999";
    public String valorComanda = "9.999.999";

    public DadosComanda(Pedido pedidoAtual){
        this.pedidoAtual = pedidoAtual;
        this.numeroComanda = pedidoAtual.comanda;
        this.valorComanda = String.valueOf(pedidoAtual.valorTotalPedido);
    }
}
