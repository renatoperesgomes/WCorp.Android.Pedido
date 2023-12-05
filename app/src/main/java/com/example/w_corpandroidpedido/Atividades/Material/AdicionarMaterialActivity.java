package com.example.w_corpandroidpedido.Atividades.Material;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Models.Pedido.PedidoMaterialItem;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Pedido.AdicionarPedidoService;
import com.example.w_corpandroidpedido.Util.Adapter.Material.AdicionarMaterialAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;

import java.util.ArrayList;
import java.util.concurrent.Future;

import io.reactivex.Flowable;

public class AdicionarMaterialActivity extends AppCompatActivity {
    private RecyclerView getRecycleMaterialInformacao;
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

        txtNumeroComanda = findViewById(R.id.txtIdComanda);
        txtValorComanda = findViewById(R.id.txtValorComanda);
        txtObservacao = findViewById(R.id.txtObservacao);
        getRecycleMaterialInformacao = findViewById(R.id.viewMaterialInformacao);

        getRecycleMaterialInformacao.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecycleMaterialInformacao.setHasFixedSize(true);


        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu, cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        if(dadosComanda.GetPedido() != null){
            txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
            txtValorComanda.setText(dadosComanda.GetValorComanda());
        }else{
            txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
            txtValorComanda.setText(dadosComanda.GetValorComanda());
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
        ArrayList<PedidoMaterialItem> listPedidoMaterialItem = new ArrayList<>();
        double maiorValor = 0;
        double divisaoMaterial = 1.0 / listMaterial.size();
        String observacao = txtObservacao.getText().toString();

        if(observacao.isEmpty())
            observacao = "";

        if(multiplaSelecao){
            for(int i = 0; i < listMaterial.size(); i++){
                if(maiorValor < listMaterial.get(i).preco){
                    maiorValor = listMaterial.get(i).preco;
                }
            }
        }

        for (Material item:
             listMaterial) {

            PedidoMaterialItem pedidoMaterialItemAtual = new PedidoMaterialItem();

            if(!multiplaSelecao){
                pedidoMaterialItemAtual.idMaterial = item.id;
                pedidoMaterialItemAtual.valorUnitario = item.preco;
                pedidoMaterialItemAtual.quantidade = 1;
                pedidoMaterialItemAtual.observacao = observacao;
            }else{
                pedidoMaterialItemAtual.idMaterial = item.id;
                pedidoMaterialItemAtual.valorUnitario = maiorValor / qtdSelecao;
                pedidoMaterialItemAtual.quantidade = 1;
                pedidoMaterialItemAtual.observacao = observacao;
            }
            listPedidoMaterialItem.add(pedidoMaterialItemAtual);
        }
        atualizarPedido(this, dadosComanda.GetNumeroComanda(), listPedidoMaterialItem);
    }

    private void atualizarPedido(Context context, String nmrComanda, ArrayList<PedidoMaterialItem> pedidoMaterialItemAtual) {
        AdicionarPedidoService adicionarPedidoService = new AdicionarPedidoService();

        for (PedidoMaterialItem pedidoMaterialItem :
                pedidoMaterialItemAtual) {
            Future<Pedido> pedidoAtualizado = adicionarPedidoService.AdicionarPedido(bearer, Integer.valueOf(nmrComanda), pedidoMaterialItem);

            try {
                Pedido pedidoAtualizadoRetorno = pedidoAtualizado.get();
                if(pedidoAtualizadoRetorno.validated){
                    dadosComanda.SetPedido(pedidoAtualizadoRetorno);
                //TODO: Verificar se é necessário este código
//                dadosComanda.SetNumeroComanda(pedidoAtualizado.get().retorno.comanda);
//                dadosComanda.SetValorComanda(String.valueOf(pedidoAtualizado.get().retorno.valorTotalPedido));
                }else if(pedidoAtualizadoRetorno.hasInconsistence){
                    AlertDialog.Builder alert = new AlertDialog.Builder(AdicionarMaterialActivity.this);
                    alert.setTitle("Atenção");
                    for (Inconsistences inconsistences :
                            pedidoAtualizadoRetorno.inconsistences) {
                        alert.setMessage(String.join(",", inconsistences.text));
                    }
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", null);
                    alert.show();
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        Intent intent = new Intent(context, CategoriaActivity.class);
        context.startActivity(intent);
    }

    private void voltarParaMaterialActivity(){
        finish();
    }
}