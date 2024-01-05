package com.wcorp.w_corpandroidpedido.Util;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
