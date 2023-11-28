package com.example.w_corpandroidpedido.Atividades.Categoria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;

import com.example.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidosActivity;
import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.MaterialCategoriaService;
import com.example.w_corpandroidpedido.Util.Adapter.Categoria.CategoriaAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;

import io.reactivex.Flowable;

public class CategoriaActivity extends AppCompatActivity {
    private RecyclerView getRecycleCategoria;
    public static final String ID_CATEGORIA = "com.example.w_corpandroidpedido.IDCATEGORIA";
    public static final String MULTIPLA_SELECAO = "com.example.w_corpandroidpedido.MULTIPLASELECAO";
    public static final String QTD_SELECAO = "com.example.w_corpandroidpedido.QTDSELECAO";
    public static final String COMBO_CATEGORIA_FILHO = "com.example.w_corpandroidpedido.COMBOCATEGORIAFILHO";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private DadosComanda dadosComanda = PesquisarPedidosActivity.dadosComanda;
    private String bearer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        CardView inicio = findViewById(R.id.cardInicio);
        CardView pagamento = findViewById(R.id.cardPagamento);
        CardView comanda = findViewById(R.id.cardComanda);
        TextView numeroComanda = findViewById(R.id.txtIdComanda);
        TextView valorComanda = findViewById(R.id.txtValorComanda);

        getRecycleCategoria = findViewById(R.id.viewCategoria);

        getRecycleCategoria.setLayoutManager(new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false));
        getRecycleCategoria.setHasFixedSize(true);

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(inicio, pagamento,comanda);
        navegacaoBarraApp.addClick(this);

        if(dadosComanda != null){
            numeroComanda.setText(dadosComanda.numeroComanda);
            valorComanda.setText(dadosComanda.valorComanda);
        }

        pesquisarCategorias();
    }

    private void pesquisarCategorias(){

        MaterialCategoriaService materialCategoriaService = new MaterialCategoriaService();

        ListenableFuture<MaterialCategoria> materialCategoria = materialCategoriaService.getCategoria(bearer);

        materialCategoria.addListener(() -> {
            try{
                MaterialCategoria result = materialCategoria.get();
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