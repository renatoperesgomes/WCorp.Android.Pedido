package com.example.w_corpandroidpedido.Atividades.Categoria;

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

import com.example.w_corpandroidpedido.Atividades.Material.MaterialActivity;
import com.example.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidosActivity;
import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Models.Material.MaterialSubCategoria;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.MaterialSubCategoriaService;
import com.example.w_corpandroidpedido.Util.Adapter.Categoria.SubCategoriaAdapter;
import com.example.w_corpandroidpedido.Util.Adapter.Util.VoltarAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.example.w_corpandroidpedido.Util.Enum.ViewType;
import com.google.common.util.concurrent.ListenableFuture;

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
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private DadosComanda dadosComanda = PesquisarPedidosActivity.dadosComanda;
    private String bearer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categoria);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        CardView inicio = findViewById(R.id.cardInicio);
        CardView pagamento = findViewById(R.id.cardPagamento);
        CardView comanda = findViewById(R.id.cardComanda);
        TextView numeroComanda = findViewById(R.id.txtIdComanda);
        TextView valorComanda = findViewById(R.id.txtValorComanda);

        Intent intent = getIntent();

        idCategoria = intent.getIntExtra(CategoriaActivity.ID_CATEGORIA, 0);
        multiplaSelecaoCategoria = intent.getBooleanExtra(MULTIPLA_SELECAO, false);
        qtdSelecaoCategoria = intent.getIntExtra(QTD_SELECAO, 0);
        comboCategoriaFilho = intent.getBooleanExtra(CategoriaActivity.COMBO_CATEGORIA_FILHO, false);

        getRecycleSubCategoria = findViewById(R.id.viewSubCategoria);
        getRecycleSubCategoria.setLayoutManager(new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false));
        getRecycleSubCategoria.setHasFixedSize(true);


        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(inicio, pagamento,comanda);
        navegacaoBarraApp.addClick(this);

        if(dadosComanda != null){
            numeroComanda.setText(dadosComanda.numeroComanda);
            valorComanda.setText(dadosComanda.valorComanda);
        }


        pesquisarSubCategorias();
    }

    private void pesquisarSubCategorias(){
        MaterialSubCategoriaService materialSubCategoriaService = new MaterialSubCategoriaService();

        ListenableFuture<MaterialSubCategoria> materialSubCategoria = materialSubCategoriaService.getSubCategoria(bearer, idCategoria);
        materialSubCategoria.addListener(() -> {
            try{
                MaterialSubCategoria result = materialSubCategoria.get();
                runOnUiThread(() ->{
                    if(result.validated){
                        if(comboCategoriaFilho){
                            finish();
                            irParaProdutos(this, idCategoria,true);
                        }else{
                            getRecycleSubCategoria.setAdapter(new ConcatAdapter(new SubCategoriaAdapter(this, result.retorno),
                                    new VoltarAdapter(this, this, ViewType.SUB_CATEGORIA.ordinal())));
                        }
                    }else if(result.hasInconsistence){
                        finish();
                        if(multiplaSelecaoCategoria){
                            irParaProdutos(this, idCategoria,true, qtdSelecaoCategoria);
                        }else if(comboCategoriaFilho){
                            irParaProdutos(this, idCategoria,true);
                        }
                        else {
                            irParaProdutos(this, idCategoria);
                        }
                    }
                });
            }catch (Exception e){
                System.out.println("Erro :" + e.getMessage());
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
}