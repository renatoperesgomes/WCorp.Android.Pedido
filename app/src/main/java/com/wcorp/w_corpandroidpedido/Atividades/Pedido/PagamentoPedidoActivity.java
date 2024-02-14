package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
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
import com.wcorp.w_corpandroidpedido.Atividades.CupomFiscal.CupomFiscalActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.Models.BaseApi;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Service.CupomFiscal.CupomFiscalService;
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
    private RecyclerView getRecyclerViewPagamento;
    private Button mImpressao;
    private Button getBtnVoltar;
    private Button getBtnFazerPagamento;
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private Dialog progressBarDialog;
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
        EditText txtQtdPessoasDividir = findViewById(R.id.txtQtdPessoasDividir);
        txtValorDivididoPessoas.setText(formatNumero.format(dadosComanda.GetValorComanda()));
        txtValorTotal.setText(formatNumero.format(dadosComanda.GetValorComanda()));

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

        getRecyclerViewPagamento = findViewById(R.id.viewCarrinhoPagamento);

        getRecyclerViewPagamento.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecyclerViewPagamento.setHasFixedSize(true);
        getRecyclerViewPagamento.setAdapter(new PagamentoAdapter(this, bearer ,dadosComanda.GetPedido()));

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicio, cardViewPagamento,cardViewComanda);
        navegacaoBarraApp.addClick(this);

        txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
        txtValorComanda.setText(formatNumero.format(dadosComanda.GetValorComanda()));

        getBtnFazerPagamento = findViewById(R.id.btnFazerPagamento);
        mImpressao = findViewById(R.id.btnPrinter);

        getBtnFazerPagamento.setOnClickListener( view ->{
            CupomFiscalService cupomFiscalService = new CupomFiscalService();
            executor.execute(() ->{
                Future<BaseApi> buscarParametroCupomFiscal = cupomFiscalService.BuscarParametroCupomFiscal(bearer);
                try {
                    BaseApi parametroCupomFiscal = buscarParametroCupomFiscal.get();
                    runOnUiThread(() ->{
                        if(parametroCupomFiscal.validated){
                            Intent intent = new Intent(this, CupomFiscalActivity.class);
                            this.startActivity(intent);
                        }else{
                            abrirDialogCupomFiscal(this);
                        }
                    });
                }catch (Exception e){
                    System.out.println("Erro: " + e.getMessage());
                }
            });
        });

        mImpressao.setOnClickListener(view ->{
            imprimirArquivo(this);
        });

        if(dadosComanda.GetPedido() == null){
            getBtnFazerPagamento.setOnClickListener(view ->{
                Toast.makeText(this,"Necessário adicionar um material para fazer o pagamento.",Toast.LENGTH_SHORT).show();
            });

            mImpressao.setOnClickListener(view ->{
                Toast.makeText(this, "Não é possível imprimir um pedido vazio.", Toast.LENGTH_SHORT).show();
            });
        }

        getBtnVoltar = findViewById(R.id.btnVoltar);
        getBtnVoltar.setOnClickListener(view ->{
            voltarParaPaginaInicial();
        });

    }
    private void voltarParaPaginaInicial(){
        finish();
    }
    public void ExcluirPedidoMaterialItem(Context context, String bearer ,Integer idPedidoMaterialItem){
        abrirDialogAlerta(context);

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
                progressBarDialog.dismiss();
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        });
    }

    private void imprimirArquivo(Context context) {
        Bitmap bitmap = GerarBitmap.GerarBitmap(context);
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
    private void abrirDialogAlerta(Context context){
        progressBarDialog = new Dialog(context);
        progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressBarDialog.setContentView(R.layout.loading);
        progressBarDialog.setCancelable(false);

        progressBarDialog.show();
    }
    private void abrirDialogCupomFiscal(Context context){
        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setTitle("Deseja emitir cupom fiscal?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context , CupomFiscalActivity.class);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context , TipoPagamentoPedidoActivity.class);
                        context.startActivity(intent);
                    }
                });
        alert.show();
    }
}




