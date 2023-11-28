package com.example.w_corpandroidpedido.Service.Empresa;

import android.util.Pair;

import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Empresa.Empresa;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;

public class EmpresaService {
    public ListenableFuture<BaseApi<List<Empresa>>> GetListEmpresa(String bearer)
    {
        ApiCall<BaseApi<List<Empresa>>> apiCall = new ApiCall<>(BaseApi.class);
        return apiCall.CallApi("GetListEmpresa", bearer, new ArrayList<Pair<String, String>>());
    }
}

