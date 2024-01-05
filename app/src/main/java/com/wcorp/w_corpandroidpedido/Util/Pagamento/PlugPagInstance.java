package com.wcorp.w_corpandroidpedido.Util.Pagamento;

import android.content.Context;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;

public class PlugPagInstance {
    private static PlugPag instance;
    private PlugPagInstance() {}

    public static PlugPag getInstance(Context context) {
        if (instance == null) {
            instance = new PlugPag(context);
        }
        return instance;
    }
}
