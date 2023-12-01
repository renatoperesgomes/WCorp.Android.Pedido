package com.example.w_corpandroidpedido.Service.Empresa;

import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Empresa.Empresa;
import com.example.w_corpandroidpedido.Models.Empresa.ListEmpresa;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;

public class EmpresaService {
    public ListenableFuture<ListEmpresa> BuscarListEmpresa()
    {
        ApiCall<ListEmpresa> apiCall = new ApiCall<>(ListEmpresa.class);
        return apiCall.CallApi("BuscarListEmpresa", "", null, false);
    }
}

