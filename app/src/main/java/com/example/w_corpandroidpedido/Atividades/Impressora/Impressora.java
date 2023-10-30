package com.example.w_corpandroidpedido.Atividades.Impressora;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.w_corpandroidpedido.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import br.com.positivo.printermodule.PrintAttributes;
import br.com.positivo.printermodule.Printer;
import br.com.positivo.printermodule.PrinterCallback;

public class Impressora extends Activity implements View.OnClickListener {

    public static final String TAG = "PrinterSample";
    public Printer mPrinter;
    private LinearLayout mRoot;
    private Button mPositivoReceiptText;
    private Context mContext;

    private PrinterCallback mCallback = new PrinterCallback() {
        @Override
        public void onError(int i, String s) {

        }

        @Override
        public void onRealLength(double v, double v1) {

        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressora);
        mContext = this.getApplicationContext();

        mPrinter = new Printer(mContext);

        initLayout();
    }

    private void initLayout(){
        mRoot = (LinearLayout) this.findViewById(R.id.content_view);

        mPositivoReceiptText = (Button) mRoot.findViewById(R.id.id_printer_receiptpositivo);
        mPositivoReceiptText.setVisibility(View.VISIBLE);
        mPositivoReceiptText.setOnClickListener(this);
        mPositivoReceiptText.setEnabled(true);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.id_printer_receiptpositivo){
            onPositivoReceiptClicked();
        }
    }

    private void onPositivoReceiptClicked(){
        final Bitmap positivo_title = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logowcorp), 265, 54, false);
        final Map<String,Integer> map_positivo = new HashMap<String,Integer>();
        map_positivo.put(PrintAttributes.KEY_ALIGN, 0);
        map_positivo.put(PrintAttributes.KEY_TYPEFACE, 1);
        map_positivo.put(PrintAttributes.KEY_TEXTSIZE, 17);

        final String[] textArray = {"                 Visa                 "};
        final String[] textArray2 = {"         CREDITO A VISTA - I          "};
        final String[] textArray3 = {"             424242-4242              "};
        final String[] textArray4 = {" VIA - ESTABELECIMENTO / POS=69000163 "};
        final String[] textArray5 = {"CNPJ: 00.000.000/0000-00              "};
        final String[] textArray6 = {"MENSAGEM TBL F0                       "};
        final String[] textArray7 = {"LOJA CRED SEM FIN                     "};
        final String[] textArray8 = {"ENDERECO1 ENDERECO2 ENDERECO3         \n-ENDERECO4                            "};
        final String[] textArray9 = {"CIDADE1 CIDADE2 CIDADE3 CIDADE4 CI    \n - SP                                 "};
        final String[] textArray10 = {"0000000000000004  DOC=419019  AUT=095121"};
        final String[] textArray11 = {"19/11/19", "09:51", "ONL-X"};
        final String[] textArray12 = {"VENDA A CREDITO                       "};
        final String[] textArray13 = {"VALOR:", "30,00"};
        final String[] textArray14 = {"MENSAGEM TBL D0                       "};

        final Map<String,Integer> maptext1 = new HashMap<String,Integer>();
        maptext1.put(PrintAttributes.KEY_TEXTSIZE, 17);
        maptext1.put(PrintAttributes.KEY_ALIGN, 0);
        maptext1.put(PrintAttributes.KEY_TYPEFACE, 0);

        final Map<String,Integer> maptext2 = new HashMap<String,Integer>();
        maptext2.put(PrintAttributes.KEY_TEXTSIZE, 17);
        maptext2.put(PrintAttributes.KEY_ALIGN, 1);
        maptext2.put(PrintAttributes.KEY_TYPEFACE, 0);
        maptext2.put(PrintAttributes.KEY_MARGINLEFT, 4);

        final Map<String,Integer> maptext3 = new HashMap<String,Integer>();
        maptext3.put(PrintAttributes.KEY_TEXTSIZE, 17);
        maptext3.put(PrintAttributes.KEY_ALIGN, 1);
        maptext3.put(PrintAttributes.KEY_TYPEFACE, 1);
        maptext3.put(PrintAttributes.KEY_MARGINLEFT, 4);

        final Map<String,Integer> maptext4 = new HashMap<String,Integer>();
        maptext4.put(PrintAttributes.KEY_TEXTSIZE, 17);
        maptext4.put(PrintAttributes.KEY_ALIGN, 1);
        maptext4.put(PrintAttributes.KEY_TYPEFACE, 1);
        maptext4.put(PrintAttributes.KEY_MARGINLEFT, 4);
        maptext4.put(PrintAttributes.KEY_WEIGHT, 3);

        final Map<String,Integer> maptext5 = new HashMap<String,Integer>();
        maptext5.put(PrintAttributes.KEY_TEXTSIZE, 17);
        maptext5.put(PrintAttributes.KEY_ALIGN, 1);
        maptext5.put(PrintAttributes.KEY_TYPEFACE, 1);
        maptext5.put(PrintAttributes.KEY_MARGINLEFT, 4);
        maptext5.put(PrintAttributes.KEY_WEIGHT, 2);

        final Map<String,Integer> maptext6 = new HashMap<String,Integer>();
        maptext6.put(PrintAttributes.KEY_TEXTSIZE, 17);
        maptext6.put(PrintAttributes.KEY_ALIGN, 2);
        maptext6.put(PrintAttributes.KEY_TYPEFACE, 1);
        maptext6.put(PrintAttributes.KEY_MARGINLEFT, 4);
        maptext6.put(PrintAttributes.KEY_WEIGHT, 1);

        final List attrCols1 = new ArrayList();
        attrCols1.add(maptext1);

        final List attrCols2 = new ArrayList();
        attrCols2.add(maptext2);

        final List attrCols3 = new ArrayList();
        attrCols3.add(maptext3);

        final List attrCols4 = new ArrayList();
        attrCols4.add(maptext4);
        attrCols4.add(maptext5);
        attrCols4.add(maptext6);

        final List attrCols5 = new ArrayList();
        attrCols5.add(maptext5);
        attrCols5.add(maptext6);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if (!mPrinter.isReady()) {
                        waitForPrinter();
                    }

                    mPrinter.printBitmapWithAttributes(positivo_title, map_positivo, mCallback);
                    mPrinter.printColumnsTextWithAttributes(textArray, attrCols1, mCallback);
                    mPrinter.printColumnsTextWithAttributes(textArray2, attrCols1, mCallback);
                    mPrinter.printColumnsTextWithAttributes(textArray3, attrCols1, mCallback);
                    mPrinter.printColumnsTextWithAttributes(textArray4, attrCols1, mCallback);

                    mPrinter.printColumnsTextWithAttributes(textArray5, attrCols2, mCallback);
                    mPrinter.printColumnsTextWithAttributes(textArray6, attrCols2, mCallback);

                    mPrinter.printColumnsTextWithAttributes(textArray7, attrCols3, mCallback);

                    mPrinter.printColumnsTextWithAttributes(textArray8, attrCols2, mCallback);
                    mPrinter.printColumnsTextWithAttributes(textArray9, attrCols2, mCallback);
                    mPrinter.printColumnsTextWithAttributes(textArray10, attrCols2, mCallback);

                    mPrinter.printColumnsTextWithAttributes(textArray11, attrCols4, mCallback);

                    mPrinter.printColumnsTextWithAttributes(textArray12, attrCols2, mCallback);
                    mPrinter.printColumnsTextWithAttributes(textArray13, attrCols5, mCallback);
                    mPrinter.printColumnsTextWithAttributes(textArray14, attrCols2, mCallback);
                    mPrinter.printStepWrapPaper(180, mCallback);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.d(TAG, e.getMessage());
                }
            }

            private void waitForPrinter() {
                synchronized (this) {
                    try {
                        Log.d(TAG, "Print: waitForPrinter");
                        wait(1000);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "Print: Error waiting for initialization", e);
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
