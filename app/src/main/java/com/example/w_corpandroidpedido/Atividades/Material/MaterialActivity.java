package com.example.w_corpandroidpedido.Atividades.Material;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.w_corpandroidpedido.Atividades.Categoria.SubCategoriaActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidoActivity;
import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.example.w_corpandroidpedido.Models.Material.ListMaterial;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.R;

import com.example.w_corpandroidpedido.Service.Material.MaterialService;
import com.example.w_corpandroidpedido.Util.Adapter.Material.MaterialAdapter;
import com.example.w_corpandroidpedido.Util.Adapter.Util.VoltarAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.example.w_corpandroidpedido.Util.Enum.ViewType;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;

import io.reactivex.Flowable;

public class MaterialActivity extends AppCompatActivity {
    private RecyclerView getRecycleMaterial;
    private int idSubCategoria;
    private boolean multiplaSelecao;
    private int qtdSelecao;
    private boolean comboCategoriaFilho;
    public static final String MULTIPLA_SELECAO = "com.example.w_corpandroidpedido.MULTIPLASELECAO";
    public static final String COMBO_CATEGORIA = "com.example.w_corpandroidpedido.COMBOCATEGORIA";
    public static final String QTD_SELECAO = "com.example.w_corpandroidpedido.QTDSELECAO";
    public static final String ITEMS = "com.example.w_corpandroidpedido.ITEMS";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private String bearer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        CardView cardViewInicioMenu = findViewById(R.id.cardInicio);
        CardView cardViewPagamentoMenu = findViewById(R.id.cardPagamento);
        CardView cardViewComandaMenu = findViewById(R.id.cardComanda);
        TextView txtNumeroComanda = findViewById(R.id.txtIdComanda);
        TextView txtValorComanda = findViewById(R.id.txtValorComanda);

        Intent intent = getIntent();

        idSubCategoria = intent.getIntExtra(SubCategoriaActivity.ID_SUBCATEGORIA, 0);
        multiplaSelecao = intent.getBooleanExtra(SubCategoriaActivity.MULTIPLA_SELECAO, false);
        qtdSelecao = intent.getIntExtra(SubCategoriaActivity.QTD_SELECAO, 0);
        comboCategoriaFilho = intent.getBooleanExtra(SubCategoriaActivity.COMBO_CATEGORIA_FILHO, false);


        getRecycleMaterial = findViewById(R.id.viewProduto);
        getRecycleMaterial.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        getRecycleMaterial.setHasFixedSize(true);

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu,cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        if(dadosComanda.GetPedido() != null){
            txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
            txtValorComanda.setText(String.valueOf(dadosComanda.GetValorComanda()));
        }else{
            txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
            txtValorComanda.setText(dadosComanda.GetValorComanda());
        }

        pesquisarMateriais();

    }

    @Override
    protected void onResume(){
        super.onResume();
        pesquisarMateriais();
    }

    private void pesquisarMateriais(){
        MaterialService materialService = new MaterialService();

        ListenableFuture<ListMaterial> listMaterial = materialService.BuscarMaterial(bearer, null ,idSubCategoria);

        listMaterial.addListener(() -> {
            try{
                ListMaterial listaMaterialRetorno = listMaterial.get();
                runOnUiThread(() ->{
                    if(listaMaterialRetorno.validated){
                        if(multiplaSelecao){
                            getRecycleMaterial.setAdapter(new ConcatAdapter(new MaterialAdapter(this, listaMaterialRetorno.retorno, true, qtdSelecao),
                                    new VoltarAdapter(this, this, ViewType.MATERIAL.ordinal())));
                        }else if(comboCategoriaFilho){
                            getRecycleMaterial.setAdapter(new ConcatAdapter(new MaterialAdapter(this, listaMaterialRetorno.retorno,true),
                                    new VoltarAdapter(this, this, ViewType.MATERIAL.ordinal())));
                        }
                        else{
                            getRecycleMaterial.setAdapter(new ConcatAdapter(new MaterialAdapter(this, listaMaterialRetorno.retorno),
                                    new VoltarAdapter(this ,this, ViewType.MATERIAL.ordinal())));
                        }
                    }else if(listaMaterialRetorno.hasInconsistence){
                        AlertDialog.Builder alert = new AlertDialog.Builder(MaterialActivity.this);
                        alert.setTitle("Atenção");

                        for (Inconsistences inconsistences :
                                listaMaterialRetorno.inconsistences) {
                            alert.setMessage(String.join(",", inconsistences.text));
                        }

                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();

                        getRecycleMaterial.setAdapter(new VoltarAdapter(this,this,ViewType.MATERIAL.ordinal()));
                    }
                });
            }catch (Exception e){
                System.out.println("Erro :" + e.getMessage());
            }
        }, MoreExecutors.directExecutor());
    }

    public void irParaMaterialInformacao(Context context, ArrayList<Material> listMateriais){
        Intent intent = new Intent(context, AdicionarMaterialActivity.class);
        intent.putExtra(ITEMS, listMateriais);

        context.startActivity(intent);
    }
    public void irParaMaterialInformacao(Context context, boolean multiplaSelecao, int qtdSelecao, ArrayList<Material> listMateriais){
        Intent intent = new Intent(context, AdicionarMaterialActivity.class);

        intent.putExtra(MULTIPLA_SELECAO, multiplaSelecao);
        intent.putExtra(QTD_SELECAO, qtdSelecao);
        intent.putExtra(ITEMS, listMateriais);

        context.startActivity(intent);
    }

    public void irParaMaterialInformacao(Context context, boolean comboCategoriaFilho, ArrayList<Material> listMateriais){
        Intent intent = new Intent(context, AdicionarMaterialActivity.class);

        intent.putExtra(COMBO_CATEGORIA, comboCategoriaFilho);
        intent.putExtra(QTD_SELECAO, qtdSelecao);

        intent.putExtra(ITEMS, listMateriais);

        context.startActivity(intent);
    }
}