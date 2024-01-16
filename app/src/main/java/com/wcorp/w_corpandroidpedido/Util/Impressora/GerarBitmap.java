package com.wcorp.w_corpandroidpedido.Util.Impressora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GerarBitmap {
    private static DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private static NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    public static Bitmap GerarBitmap(Context context){
        Pedido pedidoAtual = dadosComanda.GetPedido();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        Date data = new Date();
        String dataFomatada = simpleDateFormat.format(data);
        double valorMaisPorcentagem = pedidoAtual.retorno.valorTotalPedido * 1.1;
        ReceiptBuilder receipt = new ReceiptBuilder(1200);
        receipt.setMargin(30, 20).
                setAlign(Paint.Align.CENTER).
                setColor(Color.BLACK).
                setTypeface(context, "fonts/RobotoMono-Bold.ttf").
                setTextSize(100).
                addText("WCorp Software").
                addBlankSpace(30).
                setTextSize(60).
                setTypeface(context, "fonts/RobotoMono-Regular.ttf").
                setAlign(Paint.Align.LEFT).
                addText(dataFomatada, false).
                setAlign(Paint.Align.RIGHT).
                addText("MÁQUINA 01").
                setAlign(Paint.Align.LEFT).
                addLine().
                addBlankSpace(10).
                setAlign(Paint.Align.LEFT).
                addText("ITEM", false).
                setAlign(Paint.Align.RIGHT).
                addText("VALOR").
                addBlankSpace(10).
                addParagraph();

        for (PedidoMaterialItem pedido:
                pedidoAtual.retorno.listPedidoMaterialItem) {
            receipt.
                    setTypeface(context, "fonts/RobotoMono-Regular.ttf").
                    setAlign(Paint.Align.LEFT).
                    addText(pedido.quantidade + " x " + pedido.material.nome, false).
                    setAlign(Paint.Align.RIGHT).
                    addText(formatNumero.format(pedido.valorUnitario)).
                    addBlankSpace(30);
        }
        receipt.
                setAlign(Paint.Align.LEFT).
                addLine().
                addParagraph().
                setAlign(Paint.Align.LEFT).
                setTypeface(context, "fonts/RobotoMono-Regular.ttf").
                addText("VALOR TOTAL", false).
                setAlign(Paint.Align.RIGHT).
                addText(formatNumero.format(pedidoAtual.retorno.valorTotalPedido)).
                addParagraph().
                setAlign(Paint.Align.LEFT).
                addText("VALOR C/ 10%", false).
                setAlign(Paint.Align.RIGHT).
                addText(formatNumero.format(valorMaisPorcentagem));
        return receipt.build();
    }
}
