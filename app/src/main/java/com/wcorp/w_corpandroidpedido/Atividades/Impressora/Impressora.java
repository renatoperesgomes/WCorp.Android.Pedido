package com.wcorp.w_corpandroidpedido.Atividades.Impressora;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;

import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrintResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrinterData;

public class Impressora extends Activity {
    private Button mImpressao;
    private Executor executor = Executors.newSingleThreadExecutor();
    DadosComanda dadosComanda  = DadosComanda.GetDadosComanda();
    NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressora);
        mImpressao = findViewById(R.id.btnPrinter);
        mImpressao.setOnClickListener(view ->{
            printFile(this);
        });
    }

    private void printFile(Context context) {
        Bitmap bitmap = gerarBitmap();
        executor.execute(() -> {
            // Cria a referência do PlugPag
            PlugPag plugPag = new PlugPag(context);

            boolean isSalvo = Util.SalvarImagemEmExternalStorage(context, bitmap, "receipt.bmp");

            if(isSalvo) {
                // Cria objeto com informações da impressão
                PlugPagPrinterData plugPagPrinterData = new PlugPagPrinterData(
                        "/storage/emulated/0/Android/data/com.wcorp.w_corpandroidpedido/files/Download/receipt.bmp",
                        4, 10 * 12);

                PlugPagPrintResult result = plugPag.printFromFile(plugPagPrinterData);
            }
        });
    }

    private Bitmap gerarBitmap(){
        Pedido pedidoAtual = dadosComanda.GetPedido();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        Date data = new Date();
        String dataFomatada = simpleDateFormat.format(data);
        double valorMaisPorcentagem = pedidoAtual.retorno.valorTotalPedido * 1.1;
        ReceiptBuilder receipt = new ReceiptBuilder(1200);
        receipt.setMargin(30, 20).
                setAlign(Paint.Align.CENTER).
                setColor(Color.BLACK).
                setTypeface(this, "fonts/RobotoMono-Bold.ttf").
                setTextSize(100).
                addText("WCorp Software").
                addBlankSpace(30).
                setTextSize(60).
                setTypeface(this, "fonts/RobotoMono-Regular.ttf").
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
                setTypeface(this, "fonts/RobotoMono-Regular.ttf").
                setAlign(Paint.Align.LEFT).
                addText(pedido.material.nome, false).
                setAlign(Paint.Align.RIGHT).
                addText(formatNumero.format(pedido.valorUnitario * pedido.quantidade)).
                addBlankSpace(30);
        }
            receipt.
                setAlign(Paint.Align.LEFT).
                addLine().
                addParagraph().
                setAlign(Paint.Align.LEFT).
                setTypeface(this, "fonts/RobotoMono-Regular.ttf").
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
