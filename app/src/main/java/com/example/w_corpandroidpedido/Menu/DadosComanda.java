package com.example.w_corpandroidpedido.Menu;

import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;


public class DadosComanda {
    public static Pedido pedidoAtual;
    public String numeroComanda;
    public String valorComanda;

    public DadosComanda(Pedido pedidoAtual){
        this.pedidoAtual = pedidoAtual;
        this.numeroComanda = pedidoAtual.retorno.comanda;
        this.valorComanda = String.valueOf(pedidoAtual.retorno.valorTotalPedido);
    }
    public DadosComanda(String nmrComanda){
        this.pedidoAtual = null;
        this.numeroComanda = nmrComanda;
        this.valorComanda = "0,00";
    }
}
