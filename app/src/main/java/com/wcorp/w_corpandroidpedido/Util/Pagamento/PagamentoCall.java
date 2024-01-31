package com.wcorp.w_corpandroidpedido.Util.Pagamento;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.FecharDialog;
import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.MostrarDialog;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Atividades.Pedido.PagamentoPedidoActivity;
import com.wcorp.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidoActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Service.Pedido.PedidoService;

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
    int countPassword;
    public void EfetuarPagamento(Context context, InfoPagamento infoPagamento, boolean isCupomFiscal) {
        executor.execute(() ->{
            PlugPagPaymentData paymentData = dadosPagamento(infoPagamento);
            // Cria a identificação do aplicativo
            PlugPagAppIdentification appIdentification =
                    new PlugPagAppIdentification(context);

            PlugPag plugPag = new PlugPag(context);
            // Ativa terminal e faz o pagamento
            PlugPagInitializationResult initResult = plugPag.initializeAndActivatePinpad(new
                PlugPagActivationData("749879"));

            if(initResult.getResult() == PlugPag.RET_OK){
                plugPag.doAsyncPayment(paymentData, new PlugPagPaymentListener() {
                    @Override
                    public void onSuccess(@NonNull PlugPagTransactionResult plugPagTransactionResult) {
                        FecharDialog();
                        if(isCupomFiscal){
                            imprimirCupomFiscal(plugPag);
                        }
                        pagarPedido(context, infoPagamento);
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

    private void imprimirCupomFiscal(PlugPag plugPag) {
        executor.execute(() ->{
            // Cria objeto com informações da impressão
            PlugPagPrinterData plugPagPrinterData = new PlugPagPrinterData(
                    "/storage/emulated/0/Android/data/com.wcorp.w_corpandroidpedido/files/Download/receiptCupomFiscal.bmp",
                    4, 10 * 12);


            PlugPagPrintResult result = plugPag.printFromFile(plugPagPrinterData);
        });
    }

    private void pagarPedido(Context context, InfoPagamento infoPagamento){
        PedidoService pedidoService = new PedidoService();
        executor.execute(() ->{
            Future<Pedido> pagarPedido = pedidoService.PagarPedido(infoPagamento.Bearer, infoPagamento.IdPedido, infoPagamento.TipoPagamento, infoPagamento.ValorPagoDouble);
            try {
                Pedido retornaPedido = pagarPedido.get();
                if (retornaPedido.validated) {
                    double valorRestantePago = retornaPedido.retorno.valorTotalPedido - retornaPedido.retorno.valorTotalPago;
                    if (valorRestantePago == 0) {
                        dadosComanda.SetPedido(null);
                        Intent intent = new Intent(context, PesquisarPedidoActivity.class);
                        context.startActivity(intent);
                    } else {
                        dadosComanda.SetValorComanda(retornaPedido.retorno.valorTotalPedido - retornaPedido.retorno.valorTotalPago);
                        Intent intent = new Intent(context, PagamentoPedidoActivity.class);
                        context.startActivity(intent);
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        });
    }

    private PlugPagPaymentData dadosPagamento(InfoPagamento infoPagamento){
        // Define os dados do pagamento
        PlugPagPaymentData paymentData =
                new PlugPagPaymentData(
                        infoPagamento.TipoPagamento,
                        infoPagamento.ValorPago,
                        infoPagamento.TipoParcela,
                        infoPagamento.NumeroParcela,
                        "CODVENDA",
                        true,
                        false,
                        false);
       return paymentData;
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


