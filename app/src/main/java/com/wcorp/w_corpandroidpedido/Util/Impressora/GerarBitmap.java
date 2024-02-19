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
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

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
                //addImage(cupomFiscal.retorno.assinaturaQrCode).
                addBlankSpace(30).
                addText("Software de Gestão WCorp").
                addText("www.waveconcept.com.br");
        return receipt.build();
    }
}


//    //Cria a classe da impressora
//    W_Corp.Relatorio.ImpressoraNaoFiscal.Impressora impressora = W_Corp.Relatorio.ImpressoraNaoFiscal.Impressora.ConfigurarImpressora(empresa, dbContext);
//
//            impressora.IniciaPorta();
//
//                    ticket = impressora.ImprimirCabecalho(empresa) + "\n";
//
//                    ticket += impressora.TagAbrirCentralizar() + impressora.TagAbrirCondensar();
//                    ticket += impressora.CriarSeparador() + "\n";
//                    ticket += impressora.TagAbrirNegrito() + "Cupom Fiscal Eletrônico - SAT" + impressora.TagFecharNegrito() + "\n";
//                    ticket += impressora.TagAbrirNegrito() + "No.: " + cupomFiscal.Numero.ToString() + impressora.TagFecharNegrito() + "\n";
//                    ticket += cupomFiscal.DataHoraEmissao.ToString("dd/MM/yy HH:mm:ss") + "\n";
//
//                    ticket += impressora.CriarSeparador() + "\n";
//
//                    if (!String.IsNullOrEmpty(cupomFiscal.DestinatarioCPF))
//                    ticket += "CPF do Consumidor:" + cupomFiscal.DestinatarioCPF + "\n";
//                    else if (!String.IsNullOrEmpty(cupomFiscal.DestinatarioCNPJ))
//                    ticket += "CNPJ do Consumidor:" + cupomFiscal.DestinatarioCNPJ + "\n";
//                    else
//                    ticket += "CPF/CNPJ do Consumidor: Não Informado\n";
//
//                    ticket += impressora.TagFecharCentralizar() + impressora.TagFecharCondensar() + impressora.TagAbrirCondensar();
//                    ticket += impressora.TagAbrirCentralizar() + impressora.CriarSeparador() + impressora.TagFecharCentralizar() + "\n";
//
//                    ticket += "Código       Descrição do Item\n";
//                    ticket += "                                Vl.Unit      Qtde      Vl.Total\n";
//                    ticket += impressora.TagAbrirCentralizar() + impressora.CriarSeparador() + impressora.TagFecharCentralizar() + "\n";
//
//                    impressora.ImprimirTextoTag(ticket);
//                    ticket = String.Empty;
//                    Decimal totalDesconto = 0;
//
//                    foreach (Persistencia.CupomFiscalItem cupomFiscalItem in cupomFiscal.CupomFiscalItem)
//                    {
//                    totalDesconto += cupomFiscalItem.ValorDesconto;
//                    ticket += cupomFiscalItem.Material.Codigo.PadRight(15, ' ') + cupomFiscalItem.Material.Nome.PadRight(8, ' ') + "\n";
//                    ticket += "                                " + cupomFiscalItem.ValorUnitario.ToString(casasDecimais).PadLeft(7, ' ') + "   " + cupomFiscalItem.Quantidade.ToString().PadLeft(8, ' ') + "  " + Math.Round(cupomFiscalItem.Quantidade * cupomFiscalItem.ValorUnitario, 2).ToString().PadLeft(9, ' ') + "\n";
//
//                    foreach (Persistencia.CupomFiscalMaterialLoteEstoqueItem cupomFiscalMaterialLoteEstoqueItem in cupomFiscalItem.CupomFiscalMaterialLoteEstoqueItem)
//                    {
//                    if (!string.IsNullOrEmpty(cupomFiscalMaterialLoteEstoqueItem.MaterialLoteEstoque.NumeroLote))
//                    {
//                    ticket += cupomFiscalMaterialLoteEstoqueItem.MaterialLoteEstoque.NumeroLote + "\n";
//                    }
//                    }
//
//                    impressora.ImprimirTextoTag(ticket);
//                    ticket = String.Empty;
//                    }
//
//                    ticket += impressora.TagAbrirCentralizar() + impressora.CriarSeparador() + impressora.TagFecharCentralizar() + "\n";
//
//                    ticket += "DESCONTOS (-R$): ".PadRight(20, ' ') + totalDesconto.ToString() + "\n";
//                    ticket += "VALOR TOTAL (R$): ".PadRight(20, ' ') + cupomFiscal.TotalNotaFiscal.ToString() + "\n";
//
//                    Decimal totalPago = 0M;
//
//                    List<Persistencia.PedidoCaixaItem> listPedidoCaixaItem = new List<Persistencia.PedidoCaixaItem>();
//
//        if (cupomFiscal.Pedido != null)
//        listPedidoCaixaItem = cupomFiscal.Pedido.PedidoCaixaItem.ToList();
//        else if (cupomFiscal.OrdemServico != null)
//        listPedidoCaixaItem = cupomFiscal.OrdemServico.PedidoCaixaItem.ToList();
//
//        foreach (Persistencia.PedidoCaixaItem pedidoCaixaItem in listPedidoCaixaItem)
//        {
//        if (pedidoCaixaItem.ValorDinheiro > 0)
//        {
//        ticket += "DINHEIRO (R$): ".PadRight(20, ' ') + pedidoCaixaItem.ValorDinheiro.ToString() + "\n";
//        totalPago += pedidoCaixaItem.ValorDinheiro;
//        }
//        if (pedidoCaixaItem.ValorCartao > 0)
//        {
//        ticket += "CARTÃO (R$): ".PadRight(20, ' ') + pedidoCaixaItem.ValorCartao.ToString() + "\n";
//        totalPago += pedidoCaixaItem.ValorCartao;
//        }
//        if (pedidoCaixaItem.ValorCheque > 0)
//        {
//        ticket += "CHEQUE (R$): ".PadRight(20, ' ') + pedidoCaixaItem.ValorCheque.ToString() + "\n";
//        totalPago += pedidoCaixaItem.ValorCheque;
//        }
//        if (pedidoCaixaItem.ValorCredito > 0)
//        {
//        ticket += "OUTROS (R$): ".PadRight(20, ' ') + pedidoCaixaItem.ValorCredito.ToString() + "\n";
//        totalPago += pedidoCaixaItem.ValorCredito;
//        }
//        if (pedidoCaixaItem.ValorTroco > 0)
//        {
//        ticket += "TROCO (R$): ".PadRight(20, ' ') + pedidoCaixaItem.ValorTroco.ToString() + "\n";
//        totalPago -= pedidoCaixaItem.ValorTroco;
//        }
//        }
//
//        if (cupomFiscal.TotalNotaFiscal - totalPago > 0)
//        {
//        ticket += "OUTROS (R$): ".PadRight(20, ' ') + (cupomFiscal.TotalNotaFiscal - totalPago).ToString() + "\n";
//        }
//
//        ticket += impressora.TagAbrirCentralizar() + impressora.CriarSeparador() + impressora.TagFecharCentralizar() + "\n";
//
//        if (!String.IsNullOrEmpty(cupomFiscal.Observacao))
//        {
//        ticket += cupomFiscal.Observacao + "\n";
//        ticket += impressora.TagAbrirCentralizar() + impressora.CriarSeparador() + impressora.TagFecharCentralizar() + "\n";
//        }
//
//        ticket += impressora.TagAbrirCentralizar() + cupomFiscal.ChaveAcessoCFe.Replace("CFe", "") + impressora.TagFecharCentralizar();
//        impressora.ImprimirTextoTag(ticket);
//
//        impressora.ImprimirQrCode(cupomFiscal.AssinaturaQrCode);
//
//        ticket = impressora.TagAbrirCentralizar() + impressora.CriarSeparador() + impressora.TagFecharCentralizar() + "\n";
//        ticket += impressora.TagAbrirCentralizar() + "Software de Gestão WCorp" + impressora.TagFecharCentralizar() + "\n";
//        ticket += impressora.TagAbrirCentralizar() + "www.waveconcept.com.br" + impressora.TagFecharCentralizar();
//        impressora.ImprimirTextoTag(ticket);
//
//        impressora.AcionarGuilhotina();
//
//        impressora.FechaPorta();