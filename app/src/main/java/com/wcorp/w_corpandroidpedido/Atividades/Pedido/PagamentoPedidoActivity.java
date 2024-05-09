package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.MostrarDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wcorp.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.Models.BaseApi;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Service.CupomFiscal.CupomFiscalService;
import com.wcorp.w_corpandroidpedido.Service.Pedido.PedidoService;
import com.wcorp.w_corpandroidpedido.Service.Pedido.RemoverPedidoItemService;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Pedido.PagamentoAdapter;
import com.wcorp.w_corpandroidpedido.Util.DataStore;
import com.wcorp.w_corpandroidpedido.Util.Impressora.GerarBitmap;
import com.wcorp.w_corpandroidpedido.Util.Util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrintResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrinterData;
import io.reactivex.Flowable;

public class PagamentoPedidoActivity extends AppCompatActivity {
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private Dialog dialogLoading;
    private Executor executor = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_pedido);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        String bearer = getBearer.blockingFirst();

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        CardView cardViewInicio = findViewById(R.id.cardInicio);
        CardView cardViewPagamento = findViewById(R.id.cardPagamento);
        CardView cardViewComanda = findViewById(R.id.cardComanda);
        TextView txtNumeroComanda = findViewById(R.id.txtNumeroComanda);
        TextView txtValorComanda = findViewById(R.id.txtValorComanda);
        TextView txtValorTotal = findViewById(R.id.txtValorTotal);
        TextView txtValorDivididoPessoas = findViewById(R.id.txtValorDivPessoas);
        TextView txtValorTaxaServico = findViewById(R.id.txtValorTaxaServico);
        TextView lblValorTaxaServico = findViewById(R.id.lblValorTaxaServico);
        EditText txtQtdPessoasDividir = findViewById(R.id.txtQtdPessoasDividir);
        CheckBox ckbIncluirTaxaServico = findViewById(R.id.ckbIncluirTaxaServico);
        txtValorDivididoPessoas.setText(formatNumero.format(dadosComanda.GetValorComanda()));
        txtValorTotal.setText(formatNumero.format(dadosComanda.GetValorComanda()));
        txtValorTaxaServico.setText(formatNumero.format(dadosComanda.GetValorTaxaServico()));

        Button btnCalcularValorDividido = findViewById(R.id.btnCalcularValorDividido);
        btnCalcularValorDividido.setOnClickListener(view ->{

            try{
                float nmrDivisao = Float.parseFloat(txtQtdPessoasDividir.getText().toString());

                double resultadoDivisaoPessoas = dadosComanda.GetValorComanda() / nmrDivisao;
                txtValorDivididoPessoas.setText(formatNumero.format(resultadoDivisaoPessoas));

            }catch (Exception e){
                Toast.makeText(this, "Necessário colocar a quantidade de pessoas!", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView getRecyclerViewPagamento = findViewById(R.id.viewCarrinhoPagamento);

        getRecyclerViewPagamento.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecyclerViewPagamento.setHasFixedSize(true);
        getRecyclerViewPagamento.setAdapter(new PagamentoAdapter(this, bearer ,dadosComanda.GetPedido()));

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicio, cardViewPagamento,cardViewComanda);
        navegacaoBarraApp.addClick(this);

        txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
        txtValorComanda.setText(formatNumero.format(dadosComanda.GetValorComanda()));

        Button getBtnFazerPagamento = findViewById(R.id.btnFazerPagamento);
        Button mImpressao = findViewById(R.id.btnPrinter);

        getBtnFazerPagamento.setOnClickListener(view ->{
            Intent intent = new Intent(this, TipoPagamentoPedidoActivity.class);
            this.startActivity(intent);
        });

        mImpressao.setOnClickListener(view ->{
            imprimirArquivo(this);
        });

        ckbIncluirTaxaServico.setChecked(dadosComanda.GetIncluirTaxaServico());

        ckbIncluirTaxaServico.setOnClickListener(view -> {
            editarTaxaServico(this, bearer, ckbIncluirTaxaServico.isChecked());
        });

        if(!incluirTaxaServico(this, bearer)){
            ckbIncluirTaxaServico.setEnabled(false);
            ckbIncluirTaxaServico.setVisibility(View.INVISIBLE);
            txtValorTaxaServico.setVisibility(View.INVISIBLE);
            lblValorTaxaServico.setVisibility(View.INVISIBLE);
        };

        if(dadosComanda.GetPedido() == null){
            getBtnFazerPagamento.setOnClickListener(view ->{
                Toast.makeText(this,"Necessário adicionar um material para fazer o pagamento.",Toast.LENGTH_SHORT).show();
            });

            mImpressao.setOnClickListener(view ->{
                Toast.makeText(this, "Não é possível imprimir um pedido vazio.", Toast.LENGTH_SHORT).show();
            });
            ckbIncluirTaxaServico.setEnabled(false);
        }

        Button getBtnVoltar = findViewById(R.id.btnVoltar);
        getBtnVoltar.setOnClickListener(view ->{
            voltarParaPaginaInicial();
        });

    }
    private void voltarParaPaginaInicial(){
        finish();
    }
    public void ExcluirPedidoMaterialItem(Context context, String bearer ,Integer idPedidoMaterialItem){
        abrirDialogLoading(context);

        RemoverPedidoItemService removerPedidoItemService = new RemoverPedidoItemService();
        Integer idComandaAtual = Integer.parseInt(dadosComanda.GetNumeroComanda());

        executor.execute(() -> {
            Future<Pedido> removerPedidoMaterialItem = removerPedidoItemService.RemoverPedidoItem(bearer, idComandaAtual, idPedidoMaterialItem);

            try {
                Pedido pedidoMaterialExcluidoRetorno = removerPedidoMaterialItem.get();
                runOnUiThread(() -> {
                    if (pedidoMaterialExcluidoRetorno.validated) {
                        dadosComanda.SetPedido(pedidoMaterialExcluidoRetorno);

                        Intent intent = new Intent(context, CategoriaActivity.class);
                        context.startActivity(intent);
                    } else if (pedidoMaterialExcluidoRetorno.hasInconsistence) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Atenção");
                        StringBuilder inconsistencesJoin = new StringBuilder();
                        for (Inconsistences inconsistences :
                                pedidoMaterialExcluidoRetorno.inconsistences) {
                            inconsistencesJoin.append(inconsistences.text + "\n");
                        }
                        alert.setMessage(inconsistencesJoin);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                });
                dialogLoading.dismiss();
            } catch (Exception e) {
                runOnUiThread(() ->{
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Atenção");
                    alert.setMessage(e.getMessage());
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", null);
                    alert.show();
                });
                dialogLoading.dismiss();
            }
        });
    }
    private void imprimirArquivo(Context context) {
        Bitmap bitmap = GerarBitmap.GerarBitmapPedido(context);
        executor.execute(() -> {
            // Cria a referência do PlugPag
            PlugPag plugPag = new PlugPag(context);

            boolean isSalvo = Util.SalvarImagemEmExternalStorage(context, bitmap, "receipt.bmp");

            if(isSalvo) {
                // Cria objeto com informações da impressão
                PlugPagPrinterData plugPagPrinterData = new PlugPagPrinterData(
                        "/storage/emulated/0/Android/data/com.wcorp.w_corpandroidpedido/files/Download/receipt.bmp",
                        4, 10 * 12);

                PlugPagPrintResult result = plugPag.printFromFile(plugPagPrinterData);
            }
        });
    }
    private void abrirDialogLoading(Context context){
        dialogLoading = new Dialog(context);
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoading.setContentView(R.layout.loading);
        dialogLoading.setCancelable(false);

        dialogLoading.show();
    }

    private void editarTaxaServico(Context context, String bearer, boolean incluirTaxaServico){
        abrirDialogLoading(context);

        Pedido pedidoAtual = dadosComanda.GetPedido();

        PedidoService editarTaxaServicoPedido = new PedidoService();
        Integer idComandaAtual = Integer.parseInt(pedidoAtual.retorno.comanda);
        double valorPedidoComTaxaServico = pedidoAtual.retorno.valorTotalPedido;
        double valorTaxaServico = pedidoAtual.retorno.valorTotalProduto * pedidoAtual.retorno.valorPorcentagemTaxaServico / 100;

        if(incluirTaxaServico)
          valorPedidoComTaxaServico += valorTaxaServico;
        else
            valorPedidoComTaxaServico -= valorTaxaServico;

        double finalValorPedidoComTaxaServico = valorPedidoComTaxaServico;

        executor.execute(() -> {
            Future<Pedido> editarTaxaServico = editarTaxaServicoPedido.EditarTaxaServicoPedido(bearer, idComandaAtual, finalValorPedidoComTaxaServico,valorTaxaServico, incluirTaxaServico);

            try {
                Pedido pedidoTaxaServicoEditada = editarTaxaServico.get();
                runOnUiThread(() -> {
                    if (pedidoTaxaServicoEditada.validated) {
                        dadosComanda.SetPedido(pedidoTaxaServicoEditada);

                        Intent intent = new Intent(context, CategoriaActivity.class);
                        context.startActivity(intent);
                    } else if (pedidoTaxaServicoEditada.hasInconsistence) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Atenção");
                        StringBuilder inconsistencesJoin = new StringBuilder();
                        for (Inconsistences inconsistences :
                                pedidoTaxaServicoEditada.inconsistences) {
                            inconsistencesJoin.append(inconsistences.text + "\n");
                        }
                        alert.setMessage(inconsistencesJoin);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                });
                dialogLoading.dismiss();
            } catch (Exception e) {
                runOnUiThread(() ->{
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Atenção");
                    alert.setMessage(e.getMessage());
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", null);
                    alert.show();
                });
                dialogLoading.dismiss();
            }
        });
    }

    private boolean incluirTaxaServico(Context context, String bearer) {
        PedidoService pedidoService = new PedidoService();
        Future<BaseApi> buscarParametroTaxaServico = pedidoService.BuscarParametroIncluirTaxaServico(bearer);
        try {
            BaseApi parametroTaxaServico = buscarParametroTaxaServico.get();
            return parametroTaxaServico.validated;
        } catch (Exception e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    MostrarDialog(context, String.valueOf(e.getMessage()));
                }
            });
        }
        return false;
    }

}




