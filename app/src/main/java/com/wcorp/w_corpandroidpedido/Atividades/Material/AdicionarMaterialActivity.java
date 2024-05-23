package com.wcorp.w_corpandroidpedido.Atividades.Material;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.wcorp.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.Models.Material.Material;
import com.wcorp.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Service.Pedido.AdicionarPedidoService;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Material.AdicionarBotaoAdapter;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Material.AdicionarMaterialAdapter;
import com.wcorp.w_corpandroidpedido.Util.DataStore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Flowable;

public class AdicionarMaterialActivity extends AppCompatActivity {
    private RecyclerView getRecycleMaterialInformacao;
    private RecyclerView getGetRecyclerViewBotao;
    TextView txtNumeroComanda;
    TextView txtValorComanda;
    EditText txtObservacao;
    private boolean multiplaSelecao;
    private boolean comboCategoriaFilho;
    private int qtdSelecao;
    private ArrayList<Material> listMaterial = new ArrayList<>();
    private Button getBtnAdicionar;
    private Button getBtnVoltar;
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private String bearer;
    DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private Dialog dialogLoading;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_material);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        CardView cardViewInicioMenu = findViewById(R.id.cardInicio);
        CardView cardViewPagamentoMenu = findViewById(R.id.cardPagamento);
        CardView cardViewComandaMenu = findViewById(R.id.cardComanda);

        Intent intent = getIntent();

        multiplaSelecao = intent.getBooleanExtra(MaterialActivity.MULTIPLA_SELECAO, false);
        comboCategoriaFilho = intent.getBooleanExtra(MaterialActivity.COMBO_CATEGORIA, false);
        qtdSelecao = intent.getIntExtra(MaterialActivity.QTD_SELECAO, 0);
        listMaterial = (ArrayList<Material>) intent.getSerializableExtra(MaterialActivity.ITEMS);

        txtNumeroComanda = findViewById(R.id.txtNumeroComanda);
        txtValorComanda = findViewById(R.id.txtValorComanda);
        txtObservacao = findViewById(R.id.txtObservacao);
        getRecycleMaterialInformacao = findViewById(R.id.viewMaterialInformacao);
        getGetRecyclerViewBotao = findViewById(R.id.viewBotaoQtd);

        getRecycleMaterialInformacao.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecycleMaterialInformacao.setHasFixedSize(false);

        getGetRecyclerViewBotao.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        getGetRecyclerViewBotao.setHasFixedSize(false);
        getGetRecyclerViewBotao.setAdapter(new AdicionarBotaoAdapter(this));

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu, cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
        txtValorComanda.setText(formatNumero.format( dadosComanda.GetValorComanda()));

        if(multiplaSelecao && comboCategoriaFilho){
            getRecycleMaterialInformacao.setAdapter(new AdicionarMaterialAdapter(this, true, qtdSelecao,true, listMaterial));
        }else if (multiplaSelecao) {
            getRecycleMaterialInformacao.setAdapter(new AdicionarMaterialAdapter(this, true, qtdSelecao, listMaterial));
        } else if (comboCategoriaFilho) {
            getRecycleMaterialInformacao.setAdapter(new AdicionarMaterialAdapter(this, true, listMaterial));
        } else {
            getRecycleMaterialInformacao.setAdapter(new AdicionarMaterialAdapter(this, listMaterial));
        }

        getBtnAdicionar = findViewById(R.id.btnAdicionarProduto);
        getBtnVoltar = findViewById(R.id.btnVoltar);

        getBtnAdicionar.setOnClickListener(view -> {
            adicionarProduto();
        });

        getBtnVoltar.setOnClickListener(view -> {
            voltarParaMaterialActivity();
        });
    }

    private void adicionarProduto(){
        abrirDialogLoading();

        ArrayList<PedidoMaterialItem> listPedidoMaterialItem = new ArrayList<>();

        double quantidadeMaterialPedido = 0;
        double maiorValorMaterial = 0;
        int qtdMaterialNaoPromocional = 0;
        double divisaoMaterialMultiplaSelecaoPromocional = 0.0;
        int nmrQtdItem = Objects.requireNonNull(getGetRecyclerViewBotao.getAdapter()).getItemCount();
        String observacaoPedido = txtObservacao.getText().toString();

        for(int i = 1; i <= nmrQtdItem; i++){
            CardView cardSelecionado = getGetRecyclerViewBotao.findViewById(i);
            if(cardSelecionado.isSelected()){
                quantidadeMaterialPedido = cardSelecionado.getId();
            }
        }

        double divisaoMaterialMultiplaSelecao = quantidadeMaterialPedido / listMaterial.size();

        if(multiplaSelecao && comboCategoriaFilho) {
            for (Material material:
                 listMaterial) {
                if(maiorValorMaterial < material.preco){
                    maiorValorMaterial = material.preco;
                }
                for (MaterialCategoria materialCategoria:
                     material.listMaterialCategoria) {
                    if(!materialCategoria.pdvCategoriaParaItemPromocional &&
                        materialCategoria.idPai != 0){
                        qtdMaterialNaoPromocional++;
                    }
                }
            }

            divisaoMaterialMultiplaSelecaoPromocional = quantidadeMaterialPedido / qtdMaterialNaoPromocional;
        } else if(multiplaSelecao){
            for (Material material:
                    listMaterial) {
                if (maiorValorMaterial < material.preco) {
                    maiorValorMaterial = material.preco;
                }
            }
        }

        for (Material item:
             listMaterial) {

            PedidoMaterialItem pedidoMaterialItemAtual = new PedidoMaterialItem();

            if(multiplaSelecao && comboCategoriaFilho){
                boolean materialPromocional = false;

                for (MaterialCategoria materialCategoria:
                     item.listMaterialCategoria) {
                    if (materialCategoria.idPai != 0 &&
                            materialCategoria.pdvCategoriaParaItemPromocional) {
                        materialPromocional = true;
                        break;
                    }
                }

                pedidoMaterialItemAtual.idMaterial = item.id;
                pedidoMaterialItemAtual.observacao = observacaoPedido;

                if(materialPromocional){
                    pedidoMaterialItemAtual.valorUnitario = 0;
                    pedidoMaterialItemAtual.quantidade = 1;
                }else{
                    pedidoMaterialItemAtual.valorUnitario = maiorValorMaterial;
                    pedidoMaterialItemAtual.quantidade = divisaoMaterialMultiplaSelecaoPromocional;
                }

            } else if(multiplaSelecao){
                boolean dividirPreco = false;

                for (MaterialCategoria materialCategoria:
                     item.listMaterialCategoria) {
                    if(materialCategoria.pdvMulltiplaSelecaoDividirPreco)
                        dividirPreco = true;
                }

                pedidoMaterialItemAtual.idMaterial = item.id;
                pedidoMaterialItemAtual.observacao = observacaoPedido;

                if(dividirPreco){
                    pedidoMaterialItemAtual.valorUnitario = maiorValorMaterial / listMaterial.size();
                    pedidoMaterialItemAtual.quantidade = quantidadeMaterialPedido;
                }else{
                    pedidoMaterialItemAtual.valorUnitario = maiorValorMaterial;
                    pedidoMaterialItemAtual.quantidade = divisaoMaterialMultiplaSelecao;
                }
            }else{
                pedidoMaterialItemAtual.idMaterial = item.id;
                pedidoMaterialItemAtual.valorUnitario = item.preco;
                pedidoMaterialItemAtual.quantidade = quantidadeMaterialPedido;
                pedidoMaterialItemAtual.observacao = observacaoPedido;
            }

            listPedidoMaterialItem.add(pedidoMaterialItemAtual);
        }
        atualizarPedido(this, dadosComanda.GetNumeroComanda(), listPedidoMaterialItem);
    }

    private void atualizarPedido(Context context, String nmrComanda, ArrayList<PedidoMaterialItem> listPedidoMaterialItemAtual) {
        AdicionarPedidoService adicionarPedidoService = new AdicionarPedidoService();
        executor.execute(() -> {
            Future<Pedido> pedidoAtualizado = adicionarPedidoService.AdicionarPedido(bearer, Integer.valueOf(nmrComanda), listPedidoMaterialItemAtual);
            try {
                Pedido pedidoAtualizadoRetorno = pedidoAtualizado.get();
                if (pedidoAtualizadoRetorno.validated) {
                    dadosComanda.SetPedido(pedidoAtualizadoRetorno);
                    Intent intent = new Intent(context, CategoriaActivity.class);
                    context.startActivity(intent);
                } else if (pedidoAtualizadoRetorno.hasInconsistence) {
                    runOnUiThread(() -> {
                        AlertDialog.Builder alert = new AlertDialog.Builder(AdicionarMaterialActivity.this);
                        alert.setTitle("Atenção");
                        StringBuilder inconsistencesJoin = new StringBuilder();
                        for (Inconsistences inconsistences :
                                pedidoAtualizadoRetorno.inconsistences) {
                            inconsistencesJoin.append(inconsistences.text + "\n");
                        }
                        alert.setMessage(inconsistencesJoin);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AdicionarMaterialActivity.this);
                    alert.setTitle("Atenção");
                    alert.setMessage(e.getMessage());
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", null);
                    alert.show();
                });
            }
            dialogLoading.dismiss();
        });
    }

    private void abrirDialogLoading(){
        dialogLoading = new Dialog(this);
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoading.setContentView(R.layout.loading);
        dialogLoading.setCancelable(false);

        dialogLoading.show();
    }

    private void voltarParaMaterialActivity(){
        finish();
    }
}