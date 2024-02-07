package com.wcorp.w_corpandroidpedido.Service.CupomFiscal;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Models.BaseApi;
import com.wcorp.w_corpandroidpedido.Models.CupomFiscal.CupomFiscal;
import com.wcorp.w_corpandroidpedido.Util.ApiCall;

import java.util.ArrayList;
import java.util.concurrent.Future;

import kotlin.Triple;

public class CupomFiscalService {
    public ListenableFuture<CupomFiscal> EmitirCupomFiscal(String bearer, Integer idPedido, String cpf, String cnpj) {
        ApiCall<CupomFiscal> apiCall = new ApiCall<>(CupomFiscal.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        if (idPedido != null)
            listParametro.add(new Triple<>("idPedido", idPedido.toString(), false));

        if(cpf != null)
            listParametro.add(new Triple<>("cpf", cpf, false));

        if(cnpj != null)
            listParametro.add(new Triple<>("cnpj", cnpj, false));

        return apiCall.CallApi("EmitirCupomFiscal", bearer, listParametro);
    }

    public Future<BaseApi> BuscarParametroCupomFiscal(String bearer) {
        ApiCall<BaseApi> apiCall = new ApiCall<>(CupomFiscal.class);
        ArrayList<Triple<String,String, Boolean>> listParametro = new ArrayList<Triple<String, String, Boolean>>();

        return apiCall.CallApi("BuscarParametroCupomFiscal", bearer, listParametro);
    }
}
