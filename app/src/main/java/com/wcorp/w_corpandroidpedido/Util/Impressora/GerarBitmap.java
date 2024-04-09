package com.wcorp.w_corpandroidpedido.Util.Impressora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;
import com.wcorp.w_corpandroidpedido.MainActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Models.CupomFiscal.CupomFiscal;
import com.wcorp.w_corpandroidpedido.Models.CupomFiscal.CupomFiscalItem;
import com.wcorp.w_corpandroidpedido.Models.Empresa.Empresa;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;
import com.wcorp.w_corpandroidpedido.Util.Util;

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
                    addParagraph().
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

    public static Bitmap GerarBitmapCupomFiscal(Context context, CupomFiscal cupomFiscal) {
        double totalDesconto = 0;
        String qrCodeData = cupomFiscal.retorno.assinaturaQrCode;
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
        if (cupomFiscal.retorno.destinatarioCpf != null) {
            receipt.addText("CPF: " + cupomFiscal.retorno.destinatarioCpf);
        } else if (cupomFiscal.retorno.destinatarioCnpj != null) {
            receipt.addText("CNPJ: " + cupomFiscal.retorno.destinatarioCnpj);
        } else {
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
        for (CupomFiscalItem cupomFiscalItem :
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
                addText(formatNumero.format(cupomFiscal.retorno.totalNotaFiscal)).
                setAlign(Paint.Align.CENTER);

        if (cupomFiscal.retorno.observacao.isEmpty()) {
            receipt.
                    setAlign(Paint.Align.CENTER).
                    addBlankSpace(30).
                    addText(cupomFiscal.retorno.observacao);
        }
        if (cupomFiscal.retorno.assinaturaQrCode != null) {
            receipt.
                    addImage(qrCodeBitmap);
        }
        receipt.
                addBlankSpace(30).
                addText("Software de Gestão WCorp").
                addText("www.waveconcept.com.br");
        return receipt.build();
    }
}