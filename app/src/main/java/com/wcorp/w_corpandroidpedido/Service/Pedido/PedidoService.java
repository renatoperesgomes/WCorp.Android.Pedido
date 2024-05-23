package com.wcorp.w_corpandroidpedido.Service.Pedido;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Models.BaseApi;
import com.wcorp.w_corpandroidpedido.Models.CupomFiscal.CupomFiscal;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Util.ApiCall;

import java.util.ArrayList;
import java.util.concurrent.Future;

import kotlin.Triple;

public class PedidoService {
    public ListenableFuture<Pedido> BuscarPedido(String bearer, Integer idComanda) {
        ApiCall<Pedido> apiCall = new ApiCall<>(Pedido.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if (idComanda != null)
            listParametro.add(new Triple<>("idComanda", idComanda.toString(), false));

        return apiCall.CallApi("BuscarPedido", bearer, listParametro);
    }

    public Future<Pedido> PagarPedido(String bearer, Integer idPedido, Integer tipoPagamento, Double valorPago){
        ApiCall<Pedido> apiCall = new ApiCall<>(Pedido.class);
        ArrayList<Triple<String, String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if(idPedido != null)
            listParametro.add(new Triple<>("idPedido", idPedido.toString(), false));

        if(tipoPagamento != null)
            listParametro.add(new Triple<>("tipoPagamento", tipoPagamento.toString(), false));

        if(valorPago != null)
            listParametro.add(new Triple<>("valorPago", valorPago.toString(), false));

        return  apiCall.CallApi("PagarPedido", bearer, listParametro);
    }

    public Future<BaseApi> ValidarPedidoMovimentacaoEstoque(String bearer, Integer idPedido){
        ApiCall<BaseApi> apiCall = new ApiCall<>(BaseApi.class);
        ArrayList<Triple<String,String,Boolean>> listParametro = new ArrayList<>();

        if(idPedido != null)
            listParametro.add(new Triple<>("idPedido", idPedido.toString(), false));

        return apiCall.CallApi("ValidarPedidoMovimentacaoEstoque", bearer, listParametro);
    }

    public Future<Pedido> EditarTaxaServicoPedido(String bearer, Integer idComanda,Double novoValorPedido, Double valorTaxaServico,Boolean incluirTaxaServico){
        ApiCall<Pedido> apiCall = new ApiCall<>(Pedido.class);
        ArrayList<Triple<String, String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if(idComanda != null)
            listParametro.add(new Triple<>("idComanda", idComanda.toString(), false));

        if(novoValorPedido != null)
            listParametro.add(new Triple<>("novoValorPedido", novoValorPedido.toString(), false));

        if(valorTaxaServico != null)
            listParametro.add(new Triple<>("valorTaxaServico", valorTaxaServico.toString(), false));

        if(incluirTaxaServico != null)
            listParametro.add(new Triple<>("incluirTaxaServico", incluirTaxaServico.toString(), false));

        return  apiCall.CallApi("EditarTaxaServicoPedido", bearer, listParametro);
    }

    public Future<BaseApi> BuscarParametroIncluirTaxaServico(String bearer) {
        ApiCall<BaseApi> apiCall = new ApiCall<>(CupomFiscal.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        return apiCall.CallApi("BuscarParametroIncluirTaxaServico", bearer, listParametro);
    }
}
