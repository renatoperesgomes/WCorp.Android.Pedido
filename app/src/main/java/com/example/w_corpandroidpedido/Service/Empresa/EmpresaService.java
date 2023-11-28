package com.example.w_corpandroidpedido.Service.Empresa;

import androidx.concurrent.futures.CallbackToFutureAdapter;

import com.example.w_corpandroidpedido.Models.Empresa.Empresa;
import com.example.w_corpandroidpedido.Util.Util;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class EmpresaService {
    static String baseUrl = "http://dashboard.wcorp.com.br:5000/GetListEmpresa";
    private Executor executor = Executors.newSingleThreadExecutor();

    public ListenableFuture<Empresa> getEmpresa() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            executor.execute(() -> {
                try {
                    String URLchamada = baseUrl;
                    URL url = new URL(URLchamada);
                    HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                    conexao.setRequestMethod("POST");
                    conexao.setDoOutput(true);

                    if (conexao.getResponseCode() != HttpURLConnection.HTTP_OK)
                        throw new RuntimeException("HTTP error code: " + conexao.getResponseCode());

                    BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
                    String jsonEmString = Util.converteJsonEmString(resposta);

                    Gson gson = new Gson();
                    Empresa empresaJson = gson.fromJson(jsonEmString, Empresa.class);
                    completer.set(empresaJson);
                } catch (Exception e) {
                    completer.setException(e);
                }
            });
            return null;
        });
    }
}
