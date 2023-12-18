package com.wcorp.w_corpandroidpedido.Service.Empresa;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Models.Empresa.ListEmpresa;
import com.wcorp.w_corpandroidpedido.Util.ApiCall;

public class EmpresaService {
    public ListenableFuture<ListEmpresa> BuscarListEmpresa()
    {
        ApiCall<ListEmpresa> apiCall = new ApiCall<>(ListEmpresa.class);
        return apiCall.CallApi("BuscarListEmpresa", "", null);
    }
}

