package com.wcorp.w_corpandroidpedido.Atividades.Categoria;

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
import com.wcorp.w_corpandroidpedido.Atividades.Material.MaterialActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.Models.Material.ListMaterialCategoria;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Service.Material.MaterialCategoriaService;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Categoria.SubCategoriaAdapter;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Util.VoltarAdapter;
import com.wcorp.w_corpandroidpedido.Util.DataStore;
import com.wcorp.w_corpandroidpedido.Util.Enum.ViewType;

import java.text.NumberFormat;
import java.util.Locale;

import io.reactivex.Flowable;

public class SubCategoriaActivity extends AppCompatActivity {
    private RecyclerView getRecycleSubCategoria;
    private int idCategoria;
    private boolean comboCategoriaFilho;
    private boolean multiplaSelecaoCategoria;
    private int qtdSelecaoCategoria;
    public static final String ID_SUBCATEGORIA = "com.example.w_corpandroidpedido.IDSUBCATEGORIA";
    public static final String MULTIPLA_SELECAO = "com.example.w_corpandroidpedido.MULTIPLASELECAO";
    public static final String QTD_SELECAO = "com.example.w_corpandroidpedido.QTDSELECAO";
    public static final String COMBO_CATEGORIA_FILHO = "com.example.w_corpandroidpedido.COMBOCATEGORIAFILHO";
    public static final String CATEGORIA_ITEM_PROMOCIONAL = "com.example.w_corpandroidpedido.CATEGORIAITEMPROMOCIONAL";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private String bearer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categoria);

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

        idCategoria = intent.getIntExtra(CategoriaActivity.ID_CATEGORIA, 0);
        multiplaSelecaoCategoria = intent.getBooleanExtra(MULTIPLA_SELECAO, false);
        qtdSelecaoCategoria = intent.getIntExtra(QTD_SELECAO, 0);
        comboCategoriaFilho = intent.getBooleanExtra(CategoriaActivity.COMBO_CATEGORIA_FILHO, false);

        getRecycleSubCategoria = findViewById(R.id.viewSubCategoria);
        getRecycleSubCategoria.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        getRecycleSubCategoria.setHasFixedSize(false);

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu, cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
        txtValorComanda.setText(formatNumero.format(dadosComanda.GetValorComanda()));

        pesquisarSubCategorias();
    }

    private void pesquisarSubCategorias(){
        MaterialCategoriaService materialCategoriaService = new MaterialCategoriaService();

        ListenableFuture<ListMaterialCategoria> listMaterialCategoria = materialCategoriaService.BuscarListaMaterialCategoria(bearer, idCategoria);

        listMaterialCategoria.addListener(() -> {
            try{
                ListMaterialCategoria listaMaterialCategoriaRetorno = listMaterialCategoria.get();
                runOnUiThread(() ->{
                    if(listaMaterialCategoriaRetorno.validated){
                        if(comboCategoriaFilho && multiplaSelecaoCategoria){
                            finish();
                            irParaProdutos(this, idCategoria, true, qtdSelecaoCategoria, true, true);
                        }else if(comboCategoriaFilho) {
                            finish();
                            irParaProdutos(this, idCategoria, true);
                        }else if(multiplaSelecaoCategoria){
                            finish();
                            irParaProdutos(this, idCategoria,true, qtdSelecaoCategoria);
                        }else if(listaMaterialCategoriaRetorno.retorno.size() == 0){
                            finish();
                            irParaProdutos(this, idCategoria);
                        }else{
                            getRecycleSubCategoria.setAdapter(new ConcatAdapter(new SubCategoriaAdapter(this, listaMaterialCategoriaRetorno.retorno),
                                    new VoltarAdapter(this, this, ViewType.SUB_CATEGORIA.ordinal())));
                        }
                    }else if(listaMaterialCategoriaRetorno.hasInconsistence) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(SubCategoriaActivity.this);
                        alert.setTitle("Atenção");
                        StringBuilder inconsistencesJoin = new StringBuilder();
                        for (Inconsistences inconsistences :
                                listaMaterialCategoriaRetorno.inconsistences) {
                            inconsistencesJoin.append(inconsistences.text + "\n");
                        }
                        alert.setMessage(inconsistencesJoin);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                });
            }catch (Exception e) {
                runOnUiThread(() -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SubCategoriaActivity.this);
                    alert.setTitle("Atenção");
                    alert.setMessage(e.getMessage());
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", null);
                    alert.show();
                });
            }
        }, MoreExecutors.directExecutor());
    }

    public void irParaProdutos(Context context, int idSubCategoria){
        Intent intent = new Intent(context, MaterialActivity.class);

        intent.putExtra(ID_SUBCATEGORIA, idSubCategoria);

        context.startActivity(intent);
    }
    public void irParaProdutos(Context context, int idSubCategoria, boolean comboCategoriaFilho){
        Intent intent = new Intent(context, MaterialActivity.class);

        intent.putExtra(ID_SUBCATEGORIA, idSubCategoria);
        intent.putExtra(COMBO_CATEGORIA_FILHO, comboCategoriaFilho);

        context.startActivity(intent);
    }
    public void irParaProdutos(Context context, int idSubCategoria, boolean multiplaSelecao, int qtdSelecao){
        Intent intent = new Intent(context, MaterialActivity.class);

        intent.putExtra(ID_SUBCATEGORIA, idSubCategoria);
        intent.putExtra(MULTIPLA_SELECAO, multiplaSelecao);
        intent.putExtra(QTD_SELECAO, qtdSelecao);

        context.startActivity(intent);
    }
    public void irParaProdutos(Context context,
                               int idSubCategoria,
                               boolean multiplaSelecao,
                               int qtdSelecao,
                               boolean comboCategoriaFilho,
                               boolean categoriaParaItemPromocional)
    {
        Intent intent = new Intent(context, MaterialActivity.class);

        intent.putExtra(ID_SUBCATEGORIA, idSubCategoria);
        intent.putExtra(MULTIPLA_SELECAO, multiplaSelecao);
        intent.putExtra(QTD_SELECAO, qtdSelecao);
        intent.putExtra(COMBO_CATEGORIA_FILHO, comboCategoriaFilho);
        intent.putExtra(CATEGORIA_ITEM_PROMOCIONAL, categoriaParaItemPromocional);

        context.startActivity(intent);
    }
}