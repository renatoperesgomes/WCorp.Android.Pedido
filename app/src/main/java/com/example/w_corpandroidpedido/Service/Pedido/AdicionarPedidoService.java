package com.example.w_corpandroidpedido.Service.Pedido;

import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.util.ArrayList;

import kotlin.Triple;

public class AdicionarPedidoService {
    public ListenableFuture<Pedido> AdicionarPedido(String bearer, Integer idComanda, PedidoMaterialItem pedidoMaterialItem) {
        ApiCall<Pedido> apiCall = new ApiCall<>(Pedido.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if (idComanda != null)
            listParametro.add(new Triple<>("idComanda", idComanda.toString(), false));

        Gson gson = new Gson();
        String jsonBody = gson.toJson(pedidoMaterialItem);
        listParametro.add(new Triple<>("Body", jsonBody, true));
/*

        listParametro.add(new Triple<>("idMaterial", String.valueOf(pedidoMaterialItem.idMaterial), false));
        listParametro.add(new Triple<>("valorUnitario", String.valueOf(pedidoMaterialItem.valorUnitario), false));
        listParametro.add(new Triple<>("quantidade", String.valueOf(pedidoMaterialItem.quantidade), false));
        listParametro.add(new Triple<>("observacao", pedidoMaterialItem.observacao, false));
        //listParametro.add(new Pair<>("bonificacao", String.valueOf(pedidoMaterialItem.bonificacao)));
*/

        return apiCall.CallApi("AdicionarPedidoMaterialItemPorComanda", bearer, listParametro);
    }
}
