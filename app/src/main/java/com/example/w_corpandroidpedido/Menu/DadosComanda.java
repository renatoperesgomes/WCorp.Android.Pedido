package com.example.w_corpandroidpedido.Menu;

import com.example.w_corpandroidpedido.Models.Pedido.Pedido;

import java.text.NumberFormat;
import java.util.Locale;


public class DadosComanda {
    private static DadosComanda dadosComanda;
    private static Pedido pedidoAtual;
    private String numeroComanda;
    private double valorComanda;
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
            this.valorComanda = pedidoAtual.retorno.valorTotalPedido;
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
