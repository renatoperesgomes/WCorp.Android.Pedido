package com.wcorp.w_corpandroidpedido.Util.Pagamento;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.MostrarDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Atividades.Pedido.PagamentoPedidoActivity;
import com.wcorp.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidoActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Models.CupomFiscal.CupomFiscal;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Models.Pedido.PedidoCaixaItem;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Service.CupomFiscal.CupomFiscalService;
import com.wcorp.w_corpandroidpedido.Service.Pedido.PedidoService;
import com.wcorp.w_corpandroidpedido.Util.Impressora.GerarBitmap;
import com.wcorp.w_corpandroidpedido.Util.Util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagActivationData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagAppIdentification;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagEventData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInitializationResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPaymentData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrintResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrinterData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagTransactionResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.listeners.PlugPagPaymentListener;

public class PagamentoCall {
    private Executor executor = Executors.newSingleThreadExecutor();
    DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    public static String CodigoAtivacaoTerminal = "";
    private PlugPag plugPag;
    int countPassword;
    boolean imprimirCupomFiscal = false;
    private Dialog progressBarDialog;
    public void EfetuarPagamento(Context context, InfoPagamento infoPagamento, boolean isCupomFiscal) {
        executor.execute(() ->{
            PlugPagPaymentData paymentData = dadosPagamento(infoPagamento);
            // Cria a identificação do aplicativo
            PlugPagAppIdentification appIdentification =
                    new PlugPagAppIdentification(context);

            plugPag = new PlugPag(context);

            // Ativa terminal e faz o pagamento
            PlugPagInitializationResult initResult = plugPag.initializeAndActivatePinpad(new
                PlugPagActivationData(CodigoAtivacaoTerminal));

            if(initResult.getResult() == PlugPag.RET_OK){
                plugPag.doAsyncPayment(paymentData, new PlugPagPaymentListener() {
                    @Override
                    public void onSuccess(@NonNull PlugPagTransactionResult plugPagTransactionResult) {
                        pagarPedido(context, infoPagamento, isCupomFiscal);
                    }

                    @Override
                    public void onError(@NonNull PlugPagTransactionResult plugPagTransactionResult) {
                        MostrarDialog(context, plugPagTransactionResult.getErrorCode() + " - " + plugPagTransactionResult.getMessage());
                    }
                    @Override
                    public void onPaymentProgress(@NonNull PlugPagEventData plugPagEventData) {
                        if(plugPagEventData.getEventCode() == PlugPagEventData.EVENT_CODE_DIGIT_PASSWORD ||
                        plugPagEventData.getEventCode() == PlugPagEventData.EVENT_CODE_NO_PASSWORD){
                            MostrarDialog(context, checkMessagePassword(plugPagEventData.getEventCode()));
                        }else{
                            MostrarDialog(context, plugPagEventData.getCustomMessage());
                        }
                    }
                    @Override
                    public void onPrinterSuccess(@NonNull PlugPagPrintResult plugPagPrintResult) {}

                    @Override
                    public void onPrinterError(@NonNull PlugPagPrintResult plugPagPrintResult) {
                        MostrarDialog(context, plugPagPrintResult.getMessage());
                    }
                });
            }else{
                System.out.println(initResult.getErrorCode() + " - " + initResult.getErrorMessage());
            }
        });
    }
    private PlugPagPaymentData dadosPagamento(InfoPagamento infoPagamento){
        // Define os dados do pagamento
        return new PlugPagPaymentData(
                infoPagamento.TipoPagamento,
                infoPagamento.ValorPago,
                infoPagamento.TipoParcela,
                infoPagamento.NumeroParcela,
                "CODVENDA",
                true,
                false,
                false);
    }
    private void pagarPedido(Context context, InfoPagamento infoPagamento,boolean isCupomFiscal){
        PedidoService pedidoService = new PedidoService();
        executor.execute(() ->{
            Future<Pedido> pagarPedido = pedidoService.PagarPedido(infoPagamento.Bearer, infoPagamento.IdPedido, infoPagamento.TipoPagamento, infoPagamento.ValorPagoDouble);
            try {
                Pedido retornaPedido = pagarPedido.get();
                if (retornaPedido.validated) {
                    double valorPagoTotal = 0;

                    for (PedidoCaixaItem pedidoCaixaitem:
                            retornaPedido.retorno.listPedidoCaixaItem) {
                        valorPagoTotal += pedidoCaixaitem.valorCartao + pedidoCaixaitem.valorPix;
                    }

                    double valorRestantePago = retornaPedido.retorno.valorTotalPedido - valorPagoTotal;

                    if (valorRestantePago == 0) {
                        if(isCupomFiscal) {
                            gerarCupomFiscal(context, infoPagamento);
                        }else{
                            dadosComanda.SetPedido(null);
                            Intent intent = new Intent(context, PesquisarPedidoActivity.class);
                            context.startActivity(intent);
                        }
                    } else {
                        dadosComanda.SetValorComanda(retornaPedido.retorno.valorTotalPedido - valorPagoTotal);
                        Intent intent = new Intent(context, PagamentoPedidoActivity.class);
                        context.startActivity(intent);
                    }
                } else if (retornaPedido.hasInconsistence) {
                    StringBuilder inconsistencesJoin = new StringBuilder();
                    for (Inconsistences inconsistences :
                            retornaPedido.inconsistences) {
                        inconsistencesJoin.append(inconsistences.text).append("\n");
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            MostrarDialog(context, String.valueOf(inconsistencesJoin));
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        });
    }
    private void gerarCupomFiscal(Context context, InfoPagamento infoPagamento) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                abrirDialogAlerta(context);
                CupomFiscalService cupomFiscalService = new CupomFiscalService();
                ListenableFuture<CupomFiscal> cupomFiscal = cupomFiscalService.EmitirCupomFiscal(infoPagamento.Bearer, infoPagamento.IdPedido, infoPagamento.Cpf, infoPagamento.Cnpj);
                cupomFiscal.addListener(() -> {
                    try {
                        CupomFiscal retornoCupomFiscal = cupomFiscal.get();

                        if (retornoCupomFiscal.validated) {
                            Bitmap bitmap = GerarBitmap.GerarBitmapCupomFiscal(context, retornoCupomFiscal);

                            imprimirCupomFiscal = Util.SalvarImagemEmExternalStorage(context, bitmap, "receiptCupomFiscal.bmp");

                            if (imprimirCupomFiscal) {
                                imprimirCupomFiscal();
                            }

                            progressBarDialog.dismiss();
                            dadosComanda.SetPedido(null);
                            Intent intent = new Intent(context, PesquisarPedidoActivity.class);
                            context.startActivity(intent);
                        } else if (retornoCupomFiscal.hasInconsistence) {
                            StringBuilder inconsistencesJoin = new StringBuilder();
                            for (Inconsistences inconsistences :
                                    retornoCupomFiscal.inconsistences) {
                                inconsistencesJoin.append(inconsistences.text).append("\n");
                            }
                            progressBarDialog.dismiss();

                            dadosComanda.SetPedido(null);
                            Intent intent = new Intent(context, PesquisarPedidoActivity.class);
                            intent.putExtra("MESSAGE", String.valueOf(inconsistencesJoin));
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                }, MoreExecutors.directExecutor());
            }
        });
    }

    private void imprimirCupomFiscal() {
        executor.execute(() ->{
            // Cria objeto com informações da impressão
            PlugPagPrinterData plugPagPrinterData = new PlugPagPrinterData(
                    "/storage/emulated/0/Android/data/com.wcorp.w_corpandroidpedido/files/Download/receiptCupomFiscal.bmp",
                    4, 10 * 12);

            plugPag.printFromFile(plugPagPrinterData);
        });
    }

    private void abrirDialogAlerta(Context context){
        progressBarDialog = new Dialog(context);
        progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressBarDialog.setContentView(R.layout.loading);
        progressBarDialog.setCancelable(false);

        progressBarDialog.show();
    }
    private String checkMessagePassword(int eventCode) {
        StringBuilder strPassword = new StringBuilder();

        if (eventCode == PlugPagEventData.EVENT_CODE_DIGIT_PASSWORD) {
            countPassword++;
        }

        if (eventCode == PlugPagEventData.EVENT_CODE_NO_PASSWORD) {
            countPassword = 0;
        }

        for (int count = countPassword; count > 0; count--) {
            strPassword.append("*");
        }

        return String.format("SENHA: %s", strPassword);
    }
}


