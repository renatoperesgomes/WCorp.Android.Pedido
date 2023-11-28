package com.example.w_corpandroidpedido.Atividades.Categoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.w_corpandroidpedido.MainActivity;
import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.example.w_corpandroidpedido.Navegacao.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.MaterialCategoriaService;
import com.example.w_corpandroidpedido.Util.Adapter.Categoria.CategoriaAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.android.material.appbar.AppBarLayout;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import io.reactivex.Flowable;

public class CategoriaActivity extends AppCompatActivity {
    private RecyclerView getRecycleCategoria;
    public static final String ID_CATEGORIA = "com.example.w_corpandroidpedido.IDCATEGORIA";
    public static final String MULTIPLA_SELECAO = "com.example.w_corpandroidpedido.MULTIPLASELECAO";
    public static final String QTD_SELECAO = "com.example.w_corpandroidpedido.QTDSELECAO";
    public static final String COMBO_CATEGORIA_FILHO = "com.example.w_corpandroidpedido.COMBOCATEGORIAFILHO";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    Preferences.Key<String> EMPRESA = PreferencesKeys.stringKey("empresa");
    private String bearer;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        getRecycleCategoria = findViewById(R.id.viewCategoria);

        getRecycleCategoria.setLayoutManager(new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false));
        getRecycleCategoria.setHasFixedSize(true);

        CardView inicio = findViewById(R.id.cardInicio);
        inicio.setOnClickListener(view->{
            NavegacaoBarraApp.irPaginaInicial(this);
        });

        CardView pagamento = findViewById(R.id.cardPagamento);
        pagamento.setOnClickListener(view->{
            NavegacaoBarraApp.irPaginaPagamento(this);
        });

        CardView comanda = findViewById(R.id.cardComanda);
        comanda.setOnClickListener(view->{
            NavegacaoBarraApp.irPaginaPesquisaComanda(this);
        });

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        Flowable<String> getEmpresa =
                dataStore.data().map(prefs -> prefs.get(EMPRESA));

        bearer = getBearer.blockingFirst();
        idEmpresa = "12";

        pesquisarCategorias();
    }

    private void pesquisarCategorias(){

        MaterialCategoriaService materialCategoriaService = new MaterialCategoriaService();

        ListenableFuture<BaseApi<List<MaterialCategoria>>> listmaterialCategoria = materialCategoriaService.BuscarListaMaterialCategoria(bearer, null);

        listmaterialCategoria.addListener(() -> {
            try{
                BaseApi<List<MaterialCategoria>> result = listmaterialCategoria.get();
                runOnUiThread(() ->{
                    if(result.validated){
                        getRecycleCategoria.setAdapter(new CategoriaAdapter(this, result.retorno));
                    }else if(result.hasInconsistence){
                        AlertDialog.Builder alert = new AlertDialog.Builder(CategoriaActivity.this);
                        alert.setTitle("Atenção");
                        alert.setMessage(result.inconsistences.get(0).text);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                });
            }catch (Exception e){
                System.out.println("Erro :" + e.getMessage());
            }
        }, MoreExecutors.directExecutor());
    }

    public void irParaSubCategoria(Context context, int idCategoria){
        Intent intent = new Intent(context, SubCategoriaActivity.class);

        intent.putExtra(ID_CATEGORIA, idCategoria);

        context.startActivity(intent);
    }
    public void irParaSubCategoria(Context context, int idCategoria, boolean comboCategoriaFilho){
        Intent intent = new Intent(context, SubCategoriaActivity.class);

        intent.putExtra(ID_CATEGORIA, idCategoria);
        intent.putExtra(COMBO_CATEGORIA_FILHO, comboCategoriaFilho);

        context.startActivity(intent);
    }

    public void irParaSubCategoria(Context context, int idCategoria, boolean multiplaSelecaoCategoria, int qtdSelecaoCategoria){
        Intent intent = new Intent(context, SubCategoriaActivity.class);

        intent.putExtra(ID_CATEGORIA, idCategoria);
        intent.putExtra(MULTIPLA_SELECAO, multiplaSelecaoCategoria);
        intent.putExtra(QTD_SELECAO, qtdSelecaoCategoria);

        context.startActivity(intent);
    }
}