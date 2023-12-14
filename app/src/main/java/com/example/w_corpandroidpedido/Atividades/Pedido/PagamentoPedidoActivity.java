package com.example.w_corpandroidpedido.Atividades.Pedido;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Atividades.Categoria.SubCategoriaActivity;
import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Pedido.RemoverPedidoItemService;
import com.example.w_corpandroidpedido.Util.Adapter.Pedido.PagamentoAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Flowable;

public class PagamentoPedidoActivity extends AppCompatActivity {
    private RecyclerView getRecyclerViewPagamento;
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
                float nmrDivisao = Integer.parseInt(txtQtdPessoasDividir.getText().toString());
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

        if(dadosComanda.GetPedido() == null){
            getBtnFazerPagamento.setOnClickListener(view ->{
                Toast.makeText(this,"Necessário adicionar um material para fazer o pagamento.",Toast.LENGTH_SHORT).show();
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

    private void abrirDialogAlerta(Context context){
        progressBarDialog = new Dialog(context);
        progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressBarDialog.setContentView(R.layout.loading);
        progressBarDialog.setCancelable(false);

        progressBarDialog.show();
    }
}




