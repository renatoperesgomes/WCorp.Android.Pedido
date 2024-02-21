package com.wcorp.w_corpandroidpedido.Service.BancoDados;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Models.BancoDados.BancoDados;
import com.wcorp.w_corpandroidpedido.Util.DBCall;

import java.util.ArrayList;

import kotlin.Triple;

public class BancoDadosService {
    public ListenableFuture<BancoDados> BuscarAcesso(String serialNumber) {
        DBCall<BancoDados> dbCall = new DBCall<>(BancoDados.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if (serialNumber != null)
            listParametro.add(new Triple<>("chaveConsulta", serialNumber, false));

        return dbCall.CallDb("BuscarAcesso", "", listParametro);
    }
}
