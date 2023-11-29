package com.example.w_corpandroidpedido.Service.Material;

import android.util.Pair;

import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Material.ListMaterialCategoria;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

public class MaterialCategoriaService {
    public ListenableFuture<ListMaterialCategoria> BuscarListaMaterialCategoria(String bearer, Integer idMaterialCategoria) {
        ApiCall<ListMaterialCategoria> apiCall = new ApiCall<>(ListMaterialCategoria.class);
        List<Pair<String,String>> listParametro = new ArrayList<Pair<String, String>>();

        if (idMaterialCategoria != null)
            listParametro.add(new Pair<>("idMaterialCategoria", idMaterialCategoria.toString()));

        return apiCall.CallApi("BuscarListaMaterialCategoria", bearer, listParametro);
    }
}
