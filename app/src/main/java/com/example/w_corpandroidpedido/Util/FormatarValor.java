package com.example.w_corpandroidpedido.Util;

public class FormatarValor {
    public String customFormat(String padraoFormato, double value) {
        java.text.DecimalFormat formatadorCustom = new java.text.DecimalFormat(padraoFormato);
        String stringValorFormatado = formatadorCustom.format(value);
        return stringValorFormatado;
    }
}
