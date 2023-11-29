package com.example.w_corpandroidpedido.Service.Material;

import android.util.Pair;

import androidx.concurrent.futures.CallbackToFutureAdapter;

import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.example.w_corpandroidpedido.Util.Util;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MaterialService {
    public ListenableFuture<BaseApi<List<Material>>> BuscarMaterial(String bearer, Integer idMaterial, Integer idMaterialCategoria) {
        ApiCall<BaseApi<List<Material>>> apiCall = new ApiCall<>(BaseApi.class);
        ArrayList<Pair<String,String>> listParametro = new ArrayList<Pair<String, String>>();

        if (idMaterial != null)
            listParametro.add(new Pair<>("idMaterial", idMaterial.toString()));

        if (idMaterialCategoria != null)
            listParametro.add(new Pair<>("idMaterialCategoria", idMaterialCategoria.toString()));

        return apiCall.CallApi("BuscarMaterial", bearer, listParametro);
    }
}
