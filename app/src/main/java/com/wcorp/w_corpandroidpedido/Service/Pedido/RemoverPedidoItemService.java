package com.wcorp.w_corpandroidpedido.Service.Pedido;

import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Util.ApiCall;

import java.util.ArrayList;
import java.util.concurrent.Future;

import kotlin.Triple;

public class RemoverPedidoItemService {
    public Future<Pedido> RemoverPedidoItem(String bearer, Integer idComanda, Integer idPedidoMaterialItem) {
        ApiCall<Pedido> apiCall = new ApiCall<>(Pedido.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if (idComanda != null)
            listParametro.add(new Triple<>("idComanda", idComanda.toString(), false));

        if(idPedidoMaterialItem != null)
            listParametro.add(new Triple<>("idPedidoMaterialItem", idPedidoMaterialItem.toString(), false));

        return apiCall.CallApi("RemoverPedidoMaterialItemPorComanda", bearer, listParametro);
    }
}
