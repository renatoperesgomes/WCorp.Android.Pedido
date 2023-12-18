package com.wcorp.w_corpandroidpedido.Service.Material;

import com.wcorp.w_corpandroidpedido.Models.Material.ListMaterialCategoria;
import com.wcorp.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;

public class MaterialCategoriaService {
    public ListenableFuture<ListMaterialCategoria> BuscarListaMaterialCategoria(String bearer, Integer idMaterialCategoria) {
        ApiCall<ListMaterialCategoria> apiCall = new ApiCall<>(ListMaterialCategoria.class);
        List<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if (idMaterialCategoria != null)
            listParametro.add(new Triple<>("idPai", idMaterialCategoria.toString(), false));

        return apiCall.CallApi("BuscarListaMaterialCategoria", bearer, listParametro);
    }
}
