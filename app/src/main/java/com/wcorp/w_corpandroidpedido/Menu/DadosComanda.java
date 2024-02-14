package com.wcorp.w_corpandroidpedido.Menu;

import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Models.Pedido.PedidoCaixaItem;


public class DadosComanda {
    private static DadosComanda dadosComanda;
    private static Pedido pedidoAtual;
    private String numeroComanda;
    private double valorComanda;
    private double valorTotalPago;
    public DadosComanda() {}
    public static DadosComanda GetDadosComanda() {
        if (dadosComanda == null) {
            dadosComanda = new DadosComanda();
        }
        return dadosComanda;
    }

    public Pedido GetPedido() {
        return pedidoAtual;
    }

    public void SetPedido(Pedido pedidoAtual) {
        if(pedidoAtual == null){
            DadosComanda.pedidoAtual = null;
        }else{
            DadosComanda.pedidoAtual = pedidoAtual;
            this.numeroComanda = pedidoAtual.retorno.comanda;

            for (PedidoCaixaItem pedidoCaixaItem:
                 pedidoAtual.retorno.listPedidoCaixaItem) {
                valorTotalPago += pedidoCaixaItem.valorCartao + pedidoCaixaItem.valorPix;
            }

            this.valorComanda = pedidoAtual.retorno.valorTotalPedido - valorTotalPago;
        }
    }

    public String GetNumeroComanda(){
        return numeroComanda;
    }
    public void SetNumeroComanda(String numeroComanda){
        this.numeroComanda = numeroComanda;
    }
    public double GetValorComanda(){
        return valorComanda;
    }
    public void SetValorComanda(double valorComanda){
        this.valorComanda = valorComanda;
    }

}
