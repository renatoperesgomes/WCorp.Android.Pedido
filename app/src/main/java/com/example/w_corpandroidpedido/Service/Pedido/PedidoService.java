package com.example.w_corpandroidpedido.Service.Pedido;

import android.util.Pair;

import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;

public class PedidoService {
    public ListenableFuture<Pedido> BuscarPedido(String bearer, Integer idComanda) {
        ApiCall<Pedido> apiCall = new ApiCall<>(BaseApi.class);
        ArrayList<Pair<String,String>> listParametro = new ArrayList<Pair<String, String>>();

        if (idComanda != null)
            listParametro.add(new Pair<>("idComanda", idComanda.toString()));

        return apiCall.CallApi("BuscarPedido", bearer, listParametro);
    }
}
