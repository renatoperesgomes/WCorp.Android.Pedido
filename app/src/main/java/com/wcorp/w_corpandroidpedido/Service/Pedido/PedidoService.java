package com.wcorp.w_corpandroidpedido.Service.Pedido;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Util.ApiCall;

import java.util.ArrayList;

import kotlin.Triple;

public class PedidoService {
    public ListenableFuture<Pedido> BuscarPedido(String bearer, Integer idComanda) {
        ApiCall<Pedido> apiCall = new ApiCall<>(Pedido.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if (idComanda != null)
            listParametro.add(new Triple<>("idComanda", idComanda.toString(), false));

        return apiCall.CallApi("BuscarPedido", bearer, listParametro);
    }
}
