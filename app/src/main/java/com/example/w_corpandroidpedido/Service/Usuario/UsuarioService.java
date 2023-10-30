package com.example.w_corpandroidpedido.Service.Usuario;

import com.example.w_corpandroidpedido.Models.Usuario.Usuario;
import com.example.w_corpandroidpedido.Util.Util;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import androidx.concurrent.futures.CallbackToFutureAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UsuarioService {
    static String baseUrl = "http://10.0.2.2:5101/Login/";
    private Executor executor = Executors.newSingleThreadExecutor();
    public ListenableFuture<Usuario> loginAsync(String usuario, String senha, String idEmpresa){
        return CallbackToFutureAdapter.getFuture(completer -> {
            executor.execute(() ->{
                try{
                    String URLchamada = baseUrl + "?usuario=" +  usuario + "&senha=" + senha + "&idEmpresa=" + idEmpresa;
                    URL url = new URL(URLchamada);
                    HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                    conexao.setRequestMethod("POST");
                    conexao.setDoOutput(true);

                    if (conexao.getResponseCode() != HttpURLConnection.HTTP_OK)
                        throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());

                    BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));

                    String jsonEmString = Util.converteJsonEmString(resposta);

                    Gson gson = new Gson();
                    Usuario usuarioJson = gson.fromJson(jsonEmString, Usuario.class);
                    completer.set(usuarioJson);
                }catch (IOException e){
                    completer.setException(e);
                }
            });
            return null;
        });
    }
}