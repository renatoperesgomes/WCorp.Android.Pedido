package com.wcorp.w_corpandroidpedido.Util.Pagamento;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.listeners.PlugPagAbortListener;

public class DialogPagamento {

    static AlertDialog sDialog;

    public static void IniciarDialog(Context context) {
        if (sDialog == null) {
            sDialog = new AlertDialog.Builder(context).create();
        }
    }
    public static void MostrarDialog(Context context, String message) {
        MostrarDialog(context, message, 0, false);
    }

    public static void MostrarDialog(Context context, String message, int resourceMessage, Boolean isCancelable) {
        IniciarDialog(context);
        sDialog.setCancelable(isCancelable);
        sDialog.setMessage(message == null ? context.getString(resourceMessage) : message);
        sDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "FECHAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PlugPag plugPag = PlugPagInstance.getInstance(context);
                plugPag.asyncAbort(new PlugPagAbortListener() {
                    @Override
                    public void onAbortRequested(boolean b) {}
                    @Override
                    public void onError(@NonNull String s) {
                        System.out.println(s);
                    }
                });
            }
        });

        if (!sDialog.isShowing()) {
            sDialog.show();
        }
    }

    public static void FecharDialog() {
        if (sDialog.isShowing()) {
            sDialog.dismiss();
        }
    }
}
