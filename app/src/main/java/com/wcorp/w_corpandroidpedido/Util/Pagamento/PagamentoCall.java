package com.wcorp.w_corpandroidpedido.Util.Pagamento;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagActivationData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagAppIdentification;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInitializationResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPaymentData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagTransactionResult;

public class PagamentoCall {
    private Executor executor = Executors.newSingleThreadExecutor();
    public void EfetuarPagamento(Context context, InfoPagamento infoPagamento) {
        executor.execute(() ->{
            PlugPagPaymentData paymentData = dadosPagamento(infoPagamento);
            // Cria a identificação do aplicativo
            PlugPagAppIdentification appIdentification =
                    new PlugPagAppIdentification(context);

            // Cria a referência do PlugPag
            PlugPag plugpag = new PlugPag(context);

            // Ativa terminal e faz o pagamento
            PlugPagInitializationResult initResult = plugpag.initializeAndActivatePinpad(new
                    PlugPagActivationData("749879"));

            if (initResult.getResult() == PlugPag.RET_OK) {
                PlugPagTransactionResult result = plugpag.doPayment(paymentData);

                System.out.println(result.getResult());
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
                        "CODVENDA");
       return paymentData;
    }
}
