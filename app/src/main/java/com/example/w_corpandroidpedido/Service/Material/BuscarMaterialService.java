package com.example.w_corpandroidpedido.Service.Material;

import androidx.concurrent.futures.CallbackToFutureAdapter;

import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoriaSelecionado;
import com.example.w_corpandroidpedido.Util.Util;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BuscarMaterialService {
    static String baseUrl = "http://192.168.2.189:45455/GetMaterial";

    private Executor executor = Executors.newSingleThreadExecutor();

    public ListenableFuture<Material> buscarMaterial(int idMaterial) {
        return CallbackToFutureAdapter.getFuture(completer -> {
            executor.execute(() -> {
                try {
                    String URLchamada = baseUrl + "?idMaterial=" + idMaterial;
                    URL url = new URL(URLchamada);
                    HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                    conexao.setRequestMethod("POST");
                    conexao.setDoOutput(true);

                    if (conexao.getResponseCode() != HttpURLConnection.HTTP_OK)
                        throw new RuntimeException("HTTP error code: " + conexao.getResponseCode());

                    BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
                    String jsonEmString = Util.converteJsonEmString(resposta);

                    Gson gson = new Gson();
                    Material materialJson = gson.fromJson(jsonEmString, Material.class);

                    completer.set(materialJson);
                } catch (Exception e) {
                    completer.setException(e);
                }

            });
            return null;
        });
    }
}