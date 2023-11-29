package com.example.w_corpandroidpedido.Service.Pedido;

import android.util.Pair;

import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;

public class AdicionarPedidoService {
    public ListenableFuture<BaseApi<Pedido>> AdicionarPedido(String bearer, Integer idComanda, PedidoMaterialItem pedidoMaterialItem) {
        ApiCall<BaseApi<Pedido>> apiCall = new ApiCall<>(BaseApi.class);
        ArrayList<Pair<String,String>> listParametro = new ArrayList<Pair<String, String>>();

        if (idComanda != null)
            listParametro.add(new Pair<>("idComanda", idComanda.toString()));

        listParametro.add(new Pair<>("idMaterial", String.valueOf(pedidoMaterialItem.idMaterial)));
        listParametro.add(new Pair<>("valorUnitario", String.valueOf(pedidoMaterialItem.valorUnitario)));
        listParametro.add(new Pair<>("quantidade", String.valueOf(pedidoMaterialItem.quantidade)));
        listParametro.add(new Pair<>("observacao", pedidoMaterialItem.observacao));
        listParametro.add(new Pair<>("bonificacao", String.valueOf(pedidoMaterialItem.bonificacao)));

        return apiCall.CallApi("AdicionarPedidoMaterialItemPorComanda", bearer, listParametro);
    }
}
