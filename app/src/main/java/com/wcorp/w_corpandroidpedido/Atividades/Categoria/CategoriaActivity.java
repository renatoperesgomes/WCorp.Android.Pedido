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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.Models.Material.ListMaterialCategoria;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Service.Material.MaterialCategoriaService;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Categoria.CategoriaAdapter;
import com.wcorp.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.NumberFormat;
import java.util.Locale;

import io.reactivex.Flowable;

public class CategoriaActivity extends AppCompatActivity {
    private RecyclerView getRecycleCategoria;
    public static final String ID_CATEGORIA = "com.example.w_corpandroidpedido.IDCATEGORIA";
    public static final String MULTIPLA_SELECAO = "com.example.w_corpandroidpedido.MULTIPLASELECAO";
    public static final String QTD_SELECAO = "com.example.w_corpandroidpedido.QTDSELECAO";
    public static final String COMBO_CATEGORIA_FILHO = "com.example.w_corpandroidpedido.COMBOCATEGORIAFILHO";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private String bearer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        CardView cardViewInicioMenu = findViewById(R.id.cardInicio);
        CardView cardViewPagamentoMenu = findViewById(R.id.cardPagamento);
        CardView cardViewComandaMenu = findViewById(R.id.cardComanda);
        TextView txtNumeroComanda = findViewById(R.id.txtNumeroComanda);
        TextView txtValorComanda = findViewById(R.id.txtValorComanda);

        getRecycleCategoria = findViewById(R.id.viewCategoria);

        getRecycleCategoria.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        getRecycleCategoria.setHasFixedSize(true);

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu, cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
        txtValorComanda.setText(formatNumero.format(dadosComanda.GetValorComanda()));


        pesquisarCategorias();
    }

    private void pesquisarCategorias(){

        MaterialCategoriaService materialCategoriaService = new MaterialCategoriaService();

        ListenableFuture<ListMaterialCategoria> listMaterialCategoria = materialCategoriaService.BuscarListaMaterialCategoria(bearer, null);

        listMaterialCategoria.addListener(() -> {
            try{
                ListMaterialCategoria listaMaterialRetorno = listMaterialCategoria.get();
                runOnUiThread(() ->{
                    if(listaMaterialRetorno.validated){
                        getRecycleCategoria.setAdapter(new CategoriaAdapter(this, listaMaterialRetorno.retorno));
                    }else if(listaMaterialRetorno.hasInconsistence){
                        AlertDialog.Builder alert = new AlertDialog.Builder(CategoriaActivity.this);
                        alert.setTitle("Atenção");
                        StringBuilder inconsistencesJoin = new StringBuilder();
                        for (Inconsistences inconsistences :
                                listaMaterialRetorno.inconsistences) {
                            inconsistencesJoin.append(inconsistences.text).append("\n");
                        }
                        alert.setMessage(inconsistencesJoin);
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

    public void IrParaSubCategoria(Context context, int idCategoria){
        Intent intent = new Intent(context, SubCategoriaActivity.class);

        intent.putExtra(ID_CATEGORIA, idCategoria);

        context.startActivity(intent);
    }
    public void IrParaSubCategoria(Context context, int idCategoria, boolean comboCategoriaFilho){
        Intent intent = new Intent(context, SubCategoriaActivity.class);

        intent.putExtra(ID_CATEGORIA, idCategoria);
        intent.putExtra(COMBO_CATEGORIA_FILHO, comboCategoriaFilho);

        context.startActivity(intent);
    }

    public void IrParaSubCategoria(Context context, int idCategoria, boolean multiplaSelecaoCategoria, int qtdSelecaoCategoria){
        Intent intent = new Intent(context, SubCategoriaActivity.class);

        intent.putExtra(ID_CATEGORIA, idCategoria);
        intent.putExtra(MULTIPLA_SELECAO, multiplaSelecaoCategoria);
        intent.putExtra(QTD_SELECAO, qtdSelecaoCategoria);

        context.startActivity(intent);
    }
}