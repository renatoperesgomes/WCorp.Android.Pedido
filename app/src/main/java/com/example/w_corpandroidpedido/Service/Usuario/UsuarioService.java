package com.example.w_corpandroidpedido.Service.Usuario;

import android.util.Pair;

import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Usuario.Usuario;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;

public class UsuarioService {
    public ListenableFuture<Usuario> Login(String usuario, String senha, String idEmpresa){
        ApiCall<Usuario> apiCall = new ApiCall<>(Usuario.class);
        ArrayList<Pair<String,String>> listParametro = new ArrayList<Pair<String, String>>();
        listParametro.add(new Pair<>("usuario", usuario));
        listParametro.add(new Pair<>("senha", senha));
        listParametro.add(new Pair<>("idEmpresa", idEmpresa));

        return apiCall.CallApi("Login", "", listParametro, false);
    }
}