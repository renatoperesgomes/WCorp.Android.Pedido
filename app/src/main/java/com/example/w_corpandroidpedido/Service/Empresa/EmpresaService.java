package com.example.w_corpandroidpedido.Service.Empresa;

import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Empresa.Empresa;
import com.example.w_corpandroidpedido.Util.ApiCall;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;

public class EmpresaService {
    public ListenableFuture<BaseApi<List<Empresa>>> BuscarListEmpresa()
    {
        ApiCall<BaseApi<List<Empresa>>> apiCall = new ApiCall<>((new BaseApi<Empresa>()).getClass());
        return apiCall.CallApi("BuscarListEmpresa", "", null);
    }
}

