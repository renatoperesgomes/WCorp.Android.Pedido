package com.example.w_corpandroidpedido.Service.Material;

import androidx.concurrent.futures.CallbackToFutureAdapter;

import com.example.w_corpandroidpedido.Atividades.Material.MaterialActivity;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.example.w_corpandroidpedido.Models.Material.MaterialSubCategoria;
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

public class MaterialSubCategoriaService {
    static String baseUrl = "http://dashboard.wcorp.com.br:5000/GetListSubCategoria/";
    private Executor executor = Executors.newSingleThreadExecutor();

    public ListenableFuture<MaterialSubCategoria> getSubCategoria(String bearer, int idCategoria){
        return CallbackToFutureAdapter.getFuture(completer -> {
            executor.execute(() -> {
                try{
                    String URLchamada = baseUrl + "?idCategoria=" +  idCategoria;
                    URL url = new URL(URLchamada);
                    HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                    conexao.setRequestProperty("Authorization", "Bearer " + bearer);
                    conexao.setRequestMethod("POST");
                    conexao.setDoOutput(true);

                    if(conexao.getResponseCode() != HttpURLConnection.HTTP_OK)
                        throw new RuntimeException("HTTP error code: " + conexao.getResponseCode());

                    BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
                    String jsonEmString = Util.converteJsonEmString(resposta);

                    Gson gson = new Gson();
                    MaterialSubCategoria subCategoriaJson = gson.fromJson(jsonEmString, MaterialSubCategoria.class);
                    completer.set(subCategoriaJson);
                }catch (Exception e){
                    completer.setException(e);
                }
            });
            return null;
        });
    }
}
