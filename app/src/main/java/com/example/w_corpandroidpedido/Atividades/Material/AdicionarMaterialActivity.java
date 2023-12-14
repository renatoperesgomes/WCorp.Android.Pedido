package com.example.w_corpandroidpedido.Atividades.Material;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Pedido.AdicionarPedidoService;
import com.example.w_corpandroidpedido.Util.Adapter.Material.AdicionarBotaoAdapter;
import com.example.w_corpandroidpedido.Util.Adapter.Material.AdicionarMaterialAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;

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
    private Dialog progressBarDialog;
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
        getRecycleMaterialInformacao.setHasFixedSize(true);

        getGetRecyclerViewBotao.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        getGetRecyclerViewBotao.setHasFixedSize(true);
        getGetRecyclerViewBotao.setAdapter(new AdicionarBotaoAdapter(this));

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu, cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
        txtValorComanda.setText(formatNumero.format( dadosComanda.GetValorComanda()));


        if (multiplaSelecao) {
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
        abrirDialogAlerta();

        ArrayList<PedidoMaterialItem> listPedidoMaterialItem = new ArrayList<>();

        double quantidadeMaterialPedido = 0;
        double maiorValorMaterial = 0;
        int nmrQtdItem = Objects.requireNonNull(getGetRecyclerViewBotao.getAdapter()).getItemCount();
        String observacaoPedido = txtObservacao.getText().toString();

        for(int i = 1; i <= nmrQtdItem; i++){
            CardView cardSelecionado = getGetRecyclerViewBotao.findViewById(i);
            if(cardSelecionado.isSelected()){
                quantidadeMaterialPedido = cardSelecionado.getId();
            }
        }

        double divisaoMaterialMultiplaSelecao = quantidadeMaterialPedido / listMaterial.size();

        if(multiplaSelecao){
            for(int i = 0; i < listMaterial.size(); i++){
                if(maiorValorMaterial < listMaterial.get(i).preco){
                    maiorValorMaterial = listMaterial.get(i).preco;
                }
            }
        }

        for (Material item:
             listMaterial) {

            PedidoMaterialItem pedidoMaterialItemAtual = new PedidoMaterialItem();

            if(!multiplaSelecao){
                pedidoMaterialItemAtual.idMaterial = item.id;
                pedidoMaterialItemAtual.valorUnitario = item.preco;
                pedidoMaterialItemAtual.quantidade = quantidadeMaterialPedido;
                pedidoMaterialItemAtual.observacao = observacaoPedido;
            }else{
                pedidoMaterialItemAtual.idMaterial = item.id;
                pedidoMaterialItemAtual.valorUnitario = maiorValorMaterial;
                pedidoMaterialItemAtual.quantidade = divisaoMaterialMultiplaSelecao;
                pedidoMaterialItemAtual.observacao = observacaoPedido;
            }
            listPedidoMaterialItem.add(pedidoMaterialItemAtual);
        }
        atualizarPedido(this, dadosComanda.GetNumeroComanda(), listPedidoMaterialItem);
    }

    private void atualizarPedido(Context context, String nmrComanda, ArrayList<PedidoMaterialItem> pedidoMaterialItemAtual) {
        AdicionarPedidoService adicionarPedidoService = new AdicionarPedidoService();
        executor.execute(() ->{
            boolean todosMateriaisAdicionados = false;
            for (PedidoMaterialItem pedidoMaterialItem :
                    pedidoMaterialItemAtual) {
                Future<Pedido> pedidoAtualizado = adicionarPedidoService.AdicionarPedido(bearer, Integer.valueOf(nmrComanda), pedidoMaterialItem);
                try {
                    Pedido pedidoAtualizadoRetorno = pedidoAtualizado.get();
                    if (pedidoAtualizadoRetorno.validated) {
                        dadosComanda.SetPedido(pedidoAtualizadoRetorno);
                        todosMateriaisAdicionados = true;
                    } else if (pedidoAtualizadoRetorno.hasInconsistence) {
                        runOnUiThread(() ->{
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
                    System.out.println("Erro: " + e.getMessage());
                }
            }
            progressBarDialog.dismiss();

            if(todosMateriaisAdicionados){
                Intent intent = new Intent(context, CategoriaActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void abrirDialogAlerta(){
        progressBarDialog = new Dialog(this);
        progressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressBarDialog.setContentView(R.layout.loading);
        progressBarDialog.setCancelable(false);

        progressBarDialog.show();
    }

    private void voltarParaMaterialActivity(){
        finish();
    }
}