package com.example.w_corpandroidpedido.Service.Usuario;

import com.example.w_corpandroidpedido.Models.Usuario.Usuario;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;

import kotlin.Triple;

public class UsuarioService {
    public ListenableFuture<Usuario> Login(String usuario, String senha, String idEmpresa){
        ApiCall<Usuario> apiCall = new ApiCall<>(Usuario.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();
        listParametro.add(new Triple<>("usuario", usuario, false));
        listParametro.add(new Triple<>("senha", senha, false));
        listParametro.add(new Triple<>("idEmpresa", idEmpresa, false));

        return apiCall.CallApi("Login", "", listParametro);
    }
}