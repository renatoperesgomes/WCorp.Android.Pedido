package com.example.w_corpandroidpedido.Atividades.Categoria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoria;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.MaterialCategoriaService;
import com.example.w_corpandroidpedido.Util.Adapter.CategoriaAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import io.reactivex.Flowable;

public class CategoriaActivity extends AppCompatActivity {
    private RecyclerView getRecycleCategoria;
    private Toolbar getToolbarCategoria;
    public static final String ID_CATEGORIA = "com.example.w_corpandroidpedido.IDCATEGORIA";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private String bearer;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        getRecycleCategoria = findViewById(R.id.viewCategoria);
        getToolbarCategoria = findViewById(R.id.toolbarCategoria);

        getRecycleCategoria.setLayoutManager(new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false));
        getRecycleCategoria.setHasFixedSize(true);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        MaterialCategoriaService materialCategoriaService = new MaterialCategoriaService();

        ListenableFuture<List<MaterialCategoria.Retorno>> materialCategoria = materialCategoriaService.getCategoria(bearer);

        materialCategoria.addListener(() -> {
            try{
                List<MaterialCategoria.Retorno> result = materialCategoria.get();
                runOnUiThread(() ->{
                    getRecycleCategoria.setAdapter(new CategoriaAdapter(this, result));
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
}