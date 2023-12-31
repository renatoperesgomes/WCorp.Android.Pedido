package com.wcorp.w_corpandroidpedido.Service.Material;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Models.Material.ListMaterial;
import com.wcorp.w_corpandroidpedido.Util.ApiCall;

import java.util.ArrayList;

import kotlin.Triple;

public class MaterialService {
    public ListenableFuture<ListMaterial> BuscarMaterial(String bearer, Integer idMaterial, Integer idMaterialCategoria) {
        ApiCall<ListMaterial> apiCall = new ApiCall<>(ListMaterial.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if (idMaterial != null)
            listParametro.add(new Triple<>("idMaterial", idMaterial.toString(), false));

        if (idMaterialCategoria != null)
            listParametro.add(new Triple<>("idMaterialCategoria", idMaterialCategoria.toString(), false));

        return apiCall.CallApi("BuscarMaterial", bearer, listParametro);
    }
}
