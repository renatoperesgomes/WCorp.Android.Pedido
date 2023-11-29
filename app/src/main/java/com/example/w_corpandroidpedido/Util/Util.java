package com.example.w_corpandroidpedido.Util;


import java.io.BufferedReader;
import java.io.IOException;

public class Util {
    public static String ConverteJsonEmString(BufferedReader bufferedReader) throws IOException{
        String resposta, jsonEmString = "";
        while((resposta = bufferedReader.readLine()) != null){
            jsonEmString += resposta;
        }
        return jsonEmString;
    }
}
