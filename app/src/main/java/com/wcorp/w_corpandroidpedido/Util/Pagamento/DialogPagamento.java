package com.wcorp.w_corpandroidpedido.Util.Pagamento;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogPagamento {

    static AlertDialog sDialog;

    public static void IniciarDialog(Context context) {
        if (sDialog == null) {
            sDialog = new AlertDialog.Builder(context).create();
        }
    }
    public static void MostrarDialog(Context context, String message) {
        MostrarDialog(context, message, 0, true, null);
    }

    public static void MostrarDialog(Context context, String message, int resourceMessage, Boolean isCancelable, DialogInterface.OnCancelListener cancelListener) {
        IniciarDialog(context);
        sDialog.setCancelable(isCancelable);
        sDialog.setMessage(message == null ? context.getString(resourceMessage) : message);
        sDialog.setOnCancelListener(cancelListener);
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
