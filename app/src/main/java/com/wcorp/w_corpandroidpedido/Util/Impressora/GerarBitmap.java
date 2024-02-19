package com.wcorp.w_corpandroidpedido.Util.Impressora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Base64;

import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;
import com.wcorp.w_corpandroidpedido.MainActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Models.CupomFiscal.CupomFiscal;
import com.wcorp.w_corpandroidpedido.Models.CupomFiscal.CupomFiscalItem;
import com.wcorp.w_corpandroidpedido.Models.Empresa.Empresa;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;
import com.wcorp.w_corpandroidpedido.Util.Util;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GerarBitmap {
    private static DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private static Empresa empresa = MainActivity.EmpresaSelecionada();
    private static NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    public static Bitmap GerarBitmapPedido(Context context){
        Pedido pedidoAtual = dadosComanda.GetPedido();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        Date data = new Date();
        String dataFomatada = simpleDateFormat.format(data);
        ReceiptBuilder receipt = new ReceiptBuilder(1200);
        receipt.setMargin(30, 20).
                setAlign(Paint.Align.CENTER).
                setColor(Color.BLACK).
                setTypeface(context, "fonts/RobotoMono-Bold.ttf").
                setTextSize(100).
                addText(empresa.nomeFantasia).
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
                addText(formatNumero.format(pedidoAtual.retorno.valorTotalPedido));
        return receipt.build();
    }

    public static Bitmap GerarBitmapCupomFiscal(Context context, CupomFiscal cupomFiscal){
        double totalDesconto = 0;
        String qrCodeData = "35240258316217000195590010210000254843420947|20240219163138|68.30||QCQSkAnxyPRxInpevI31kAE4dRMBVHB99FEI05k4xWrDkKM6r/y1c5dR5QSyLGfUgYOV76WWTkvzRGv/wBQwdIXAW7efyUgMWZYhMq7z+sXZ77cwRMXPjbkRJjwIu2uyc79Zbdv4a6wnObM2mQ3IMeyxTzdsQ285f/nBehzv59PTGVXuZN1hZ/2GUCDTb9g2uyt9WJ7ZRtFuX2Hryv6W1Gf22bJSzcS8Em4gYmLAr8f1pI5HM3+0iwPIOhxhXhb4hLEr9u/NGpAN+1h7lz2X8am5n/TSIaamFt33H23Khb7yuDhEWpNbMeoX7Kgp1Kd9OV8gwgxMcA2+cjoRPdljew==";
        //String qrCodeData = cupomFiscal.retorno.assinaturaQrCode;
        int width = 900;
        int height = 900;
        Bitmap qrCodeBitmap = Util.gerarQRCodeBitmap(qrCodeData, width, height);
        ReceiptBuilder receipt = new ReceiptBuilder(1200);
        receipt.setMargin(10, 10).
                setAlign(Paint.Align.CENTER).
                setColor(Color.BLACK).
                setTypeface(context, "fonts/RobotoMono-Bold.ttf").
                setTextSize(100).
                addText(empresa.nomeFantasia).
                addBlankSpace(30).
                setTextSize(50).
                addText(empresa.telefone).
                setTextSize(40).
                addText("CNPJ: " + empresa.cnpj + " - IE: " + empresa.inscricaoEstadual).
                addText(empresa.logradouro + ", " + empresa.numero).
                addText("CEP: " + empresa.cep + " - " + empresa.inscricaoEstadual + " - " +
                        "Bairro: " + empresa.bairro).
                addText(empresa.municipio.nome + " - " + empresa.municipio.estado.sigla).
                addBlankSpace(30).
                setTextSize(50).
                addText("Cupom Fiscal Eletrônico - SAT").
                addText("No.: " + cupomFiscal.retorno.numero).
                addText(Util.ConversorData(cupomFiscal.retorno.dataHoraEmissao)).
                addBlankSpace(30).
                setTextSize(50).
                setTypeface(context, "fonts/RobotoMono-Bold.ttf");
                if(cupomFiscal.retorno.destinatarioCpf != null){
                    receipt.addText("CPF: " + cupomFiscal.retorno.destinatarioCpf);
                }else if(cupomFiscal.retorno.destinatarioCnpj != null){
                    receipt.addText("CNPJ: " + cupomFiscal.retorno.destinatarioCnpj);
                }else{
                    receipt.addText("Consumidor não informado");
                }
                receipt.
                addBlankSpace(20).
                setTextSize(50).
                setTypeface(context, "fonts/RobotoMono-Regular.ttf").
                setAlign(Paint.Align.LEFT).
                addText("Código  Des. do item  Vl.Unit").
                setAlign(Paint.Align.RIGHT).
                addText("Qtde  Vl.Total").
                addParagraph();
        for (CupomFiscalItem cupomFiscalItem:
                cupomFiscal.retorno.listCupomFiscalItem) {
            totalDesconto += cupomFiscalItem.valorDesconto;
            receipt.
                    setTypeface(context, "fonts/RobotoMono-Regular.ttf").
                    setAlign(Paint.Align.LEFT).
                    addText(cupomFiscalItem.codigo + "  " + cupomFiscalItem.descricao + "  " + formatNumero.format(cupomFiscalItem.valorUnitario)).
                    setAlign(Paint.Align.RIGHT).
                    addText(cupomFiscalItem.quantidade + "  " + formatNumero.format(cupomFiscalItem.quantidade * cupomFiscalItem.valorUnitario)).
                    addBlankSpace(30);
        }
        receipt.
                setAlign(Paint.Align.LEFT).
                addLine().
                addParagraph().
                setAlign(Paint.Align.LEFT).
                setTypeface(context, "fonts/RobotoMono-Regular.ttf").
                addText("DESCONTOS (-R$): ", false).
                setAlign(Paint.Align.RIGHT).
                addText(formatNumero.format(totalDesconto)).
                setAlign(Paint.Align.LEFT).
                addText("VALOR TOTAL", false).
                setAlign(Paint.Align.RIGHT).
                addText(formatNumero.format(cupomFiscal.retorno.totalNotaFiscal));

        if (cupomFiscal.retorno.observacao.isEmpty())
        {
            receipt.
                    setAlign(Paint.Align.CENTER).
                    addBlankSpace(30).
                    addText(cupomFiscal.retorno.observacao);
        }
        receipt.
                //addText(cupomFiscal.retorno.chaveAcessoCfe.replace("CFe", "")).
                setAlign(Paint.Align.CENTER).
                addImage(qrCodeBitmap).
                addBlankSpace(30).
                addText("Software de Gestão WCorp").
                addText("www.waveconcept.com.br");
        return receipt.build();
    }
}