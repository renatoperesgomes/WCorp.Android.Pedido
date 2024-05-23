package com.wcorp.w_corpandroidpedido.Atividades.Material;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Atividades.Categoria.SubCategoriaActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.Models.Material.ListMaterial;
import com.wcorp.w_corpandroidpedido.Models.Material.Material;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Service.Material.MaterialService;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Material.MaterialAdapter;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Util.VoltarAdapter;
import com.wcorp.w_corpandroidpedido.Util.DataStore;
import com.wcorp.w_corpandroidpedido.Util.Enum.ViewType;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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
        TextView txtNumeroComanda = findViewById(R.id.txtNumeroComanda);
        TextView txtValorComanda = findViewById(R.id.txtValorComanda);

        Intent intent = getIntent();

        idSubCategoria = intent.getIntExtra(SubCategoriaActivity.ID_SUBCATEGORIA, 0);
        multiplaSelecao = intent.getBooleanExtra(SubCategoriaActivity.MULTIPLA_SELECAO, false);
        qtdSelecao = intent.getIntExtra(SubCategoriaActivity.QTD_SELECAO, 0);
        comboCategoriaFilho = intent.getBooleanExtra(SubCategoriaActivity.COMBO_CATEGORIA_FILHO, false);


        getRecycleMaterial = findViewById(R.id.viewProduto);
        getRecycleMaterial.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        getRecycleMaterial.setHasFixedSize(false);

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu,cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
        txtValorComanda.setText(formatNumero.format(dadosComanda.GetValorComanda()));

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
                        if(multiplaSelecao && comboCategoriaFilho){
                            getRecycleMaterial.setAdapter(new ConcatAdapter(new MaterialAdapter(this, listaMaterialRetorno.retorno, true, true, qtdSelecao),
                                    new VoltarAdapter(this, this, ViewType.MATERIAL.ordinal())));
                        }
                        else if(multiplaSelecao){
                            getRecycleMaterial.setAdapter(new ConcatAdapter(new MaterialAdapter(this, listaMaterialRetorno.retorno, true, qtdSelecao),
                                    new VoltarAdapter(this, this, ViewType.MATERIAL.ordinal())));
                        }
                        else if(comboCategoriaFilho){
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
                        StringBuilder inconsistencesJoin = new StringBuilder();
                        for (Inconsistences inconsistences :
                                listaMaterialRetorno.inconsistences) {
                            inconsistencesJoin.append(inconsistences.text + "\n");
                        }
                        alert.setMessage(inconsistencesJoin);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();

                        getRecycleMaterial.setAdapter(new VoltarAdapter(this,this,ViewType.MATERIAL.ordinal()));
                    }
                });
            }catch (Exception e){
                runOnUiThread(() ->{
                    AlertDialog.Builder alert = new AlertDialog.Builder(MaterialActivity.this);
                    alert.setTitle("Atenção");
                    alert.setMessage(e.getMessage());
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", null);
                    alert.show();
                });
            }
        }, MoreExecutors.directExecutor());
    }

    public void irParaMaterialInformacao(Context context, ArrayList<Material> listMateriais){
        Intent intent = new Intent(context, AdicionarMaterialActivity.class);
        intent.putExtra(ITEMS, listMateriais);

        context.startActivity(intent);
    }

    public void irParaMaterialInformacao(Context context, boolean multiplaSelecao, int qtdSelecao, boolean comboCategoriaFilho, ArrayList<Material> listMateriais){
        Intent intent = new Intent(context, AdicionarMaterialActivity.class);

        intent.putExtra(MULTIPLA_SELECAO, multiplaSelecao);
        intent.putExtra(COMBO_CATEGORIA, comboCategoriaFilho);
        intent.putExtra(QTD_SELECAO, qtdSelecao);
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