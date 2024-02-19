package com.wcorp.w_corpandroidpedido.Util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    public static String ConverteJsonEmString(BufferedReader bufferedReader) throws IOException{
        String resposta, jsonEmString = "";
        while((resposta = bufferedReader.readLine()) != null){
            jsonEmString += resposta;
        }
        return jsonEmString;
    }

    public static boolean SalvarImagemEmExternalStorage(Context context, Bitmap bitmap, String fileName) {
        // Verifica se o armazenamento externo está montado e gravável
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        }

        // Obtém o diretório de armazenamento externo
        File directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        // Cria o arquivo de destino
        File file = new File(directory, fileName);

        // Tenta salvar a imagem no arquivo
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String ConversorData(String inputDateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            Date date = inputFormat.parse(inputDateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Handle the parsing exception as needed
        }
    }

    public static Bitmap gerarQRCodeBitmap(String qrCodeData, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, width, height);

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
