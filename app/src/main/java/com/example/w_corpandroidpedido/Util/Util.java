package com.example.w_corpandroidpedido.Util;

import androidx.concurrent.futures.CallbackToFutureAdapter;

import com.example.w_corpandroidpedido.Models.Empresa.Empresa;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {
    public static String ConverteJsonEmString(BufferedReader bufferedReader) throws IOException{
        String resposta, jsonEmString = "";
        while((resposta = bufferedReader.readLine()) != null){
            jsonEmString += resposta;
        }
        return jsonEmString;
    }
}
