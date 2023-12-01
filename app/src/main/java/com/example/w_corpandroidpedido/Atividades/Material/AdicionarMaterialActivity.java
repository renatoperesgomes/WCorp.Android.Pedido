package com.example.w_corpandroidpedido.Atividades.Material;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidoActivity;
import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Pedido.AdicionarPedidoService;
import com.example.w_corpandroidpedido.Util.Adapter.Material.AdicionarMaterialAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;

import io.reactivex.Flowable;

public class AdicionarMaterialActivity extends AppCompatActivity {
    private RecyclerView getRecycleMaterialInformacao;
    TextView txtNumeroComanda;
    TextView txtValorComanda;
    private boolean multiplaSelecao;
    private boolean comboCategoriaFilho;
    private int qtdSelecao;
    private ArrayList<Material> listMaterial = new ArrayList<>();
    private Button getBtnAdicionar;
    private Button getBtnVoltar;
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private Pedido pedidoAtual = DadosComanda.pedidoAtual;
    private final DadosComanda dadosComanda = PesquisarPedidoActivity.dadosComanda;
    private String bearer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_informacao);

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
        txtNumeroComanda = findViewById(R.id.txtIdComanda);
        txtValorComanda = findViewById(R.id.txtValorComanda);

        getRecycleMaterialInformacao = findViewById(R.id.viewMaterialInformacao);

        getRecycleMaterialInformacao.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecycleMaterialInformacao.setHasFixedSize(true);


        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu, cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        if (pedidoAtual != null) {
            txtNumeroComanda.setText(pedidoAtual.retorno.comanda);
            txtValorComanda.setText(String.valueOf(pedidoAtual.retorno.valorTotalPedido));
        } else {
            txtNumeroComanda.setText(dadosComanda.numeroComanda);
            txtValorComanda.setText(dadosComanda.valorComanda);
        }

        if (multiplaSelecao) {
            getRecycleMaterialInformacao.setAdapter(new AdicionarMaterialAdapter(this, true, qtdSelecao, listMaterial));
        } else if (comboCategoriaFilho) {
            getRecycleMaterialInformacao.setAdapter(new AdicionarMaterialAdapter(this, true, listMaterial));
        } else {
            getRecycleMaterialInformacao.setAdapter(new AdicionarMaterialAdapter(this, listMaterial));
        }

        getBtnAdicionar = findViewById(R.id.btnAdicionarProduto);
        getBtnVoltar = findViewById(R.id.btnVoltar);

        getBtnAdicionar.setOnClickListener(view ->{
            adicionarProduto();
        });

        getBtnVoltar.setOnClickListener(view ->{
            voltarParaMaterialActivity();
        });
    }

    private void adicionarProduto(){
        for (Material item:
             listMaterial) {
            PedidoMaterialItem pedidoMaterialItemAtual = new PedidoMaterialItem();
            pedidoMaterialItemAtual.idMaterial = item.id;
            pedidoMaterialItemAtual.valorUnitario = item.preco;
            pedidoMaterialItemAtual.quantidade = 1 / listMaterial.size();
            pedidoMaterialItemAtual.observacao = "Sem cebola";

            if(pedidoAtual != null){
                atualizarPedido(this, pedidoAtual.retorno.comanda, pedidoMaterialItemAtual);
            }else{
                atualizarPedido(this, dadosComanda.numeroComanda, pedidoMaterialItemAtual);
            }
        }
    }

    private void voltarParaMaterialActivity(){
        finish();
    }

    private void atualizarPedido(Context context, String nmrComanda, PedidoMaterialItem pedidoMaterialItemAtual){
        AdicionarPedidoService adicionarPedidoService = new AdicionarPedidoService();

        ListenableFuture <Pedido> pedidoAtualizado = adicionarPedidoService.AdicionarPedido(bearer, Integer.valueOf(nmrComanda), pedidoMaterialItemAtual);

        pedidoAtualizado.addListener(() ->{
            try {
                pedidoAtual = pedidoAtualizado.get();

                Intent intent = new Intent(context, CategoriaActivity.class);
                context.startActivity(intent);

            }catch (Exception e){
                System.out.println("Erro: " + e.getMessage());
            }
        }, MoreExecutors.directExecutor());
    }
}