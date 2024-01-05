package com.wcorp.w_corpandroidpedido.Atividades.Impressora;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;

import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrintResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrinterData;

public class Impressora extends Activity {
    private Button mImpressao;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressora);
        mImpressao = findViewById(R.id.id_printer_receiptpositivo);
        mImpressao.setOnClickListener(view ->{
            printFile(this);
        });
    }

    public void printFile(Context context) {
        executor.execute(() -> {
            // Cria a referência do PlugPag
            PlugPag plugPag = new PlugPag(context);

            InputStream inputStream = null;
            Bitmap bitmap = null;

            try{
                inputStream = context.getAssets().open("receipt.bmp");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean isSalvo = Util.SalvarImagemEmExternalStorage(context, bitmap, "receipt.bmp");

            if(isSalvo) {
                // Cria objeto com informações da impressão
                PlugPagPrinterData plugPagPrinterData = new PlugPagPrinterData(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/receipt.bmp",
                        4, 10 * 12);

                PlugPagPrintResult result = plugPag.printFromFile(plugPagPrinterData);
            }
        });
    }

}
