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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.w_corpandroidpedido.Atividades.Material.MaterialActivity;
import com.example.w_corpandroidpedido.Models.Material.MaterialSubCategoria;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.MaterialSubCategoriaService;
import com.example.w_corpandroidpedido.Util.Adapter.MaterialAdapter;
import com.example.w_corpandroidpedido.Util.Adapter.SubCategoriaAdapter;
import com.example.w_corpandroidpedido.Util.Adapter.VoltarAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.example.w_corpandroidpedido.Util.Enum.ViewType;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import io.reactivex.Flowable;

public class SubCategoriaActivity extends AppCompatActivity {
    private RecyclerView getRecycleSubCategoria;
    private int idCategoria;
    public static final String ID_SUBCATEGORIA = "com.example.w_corpandroidpedido.IDSUBCATEGORIA";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private String bearer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categoria);

        Intent intent = getIntent();
        idCategoria = intent.getIntExtra(CategoriaActivity.ID_CATEGORIA, 0);

        getRecycleSubCategoria = findViewById(R.id.viewSubCategoria);
        getRecycleSubCategoria.setLayoutManager(new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false));
        getRecycleSubCategoria.setHasFixedSize(true);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        MaterialSubCategoriaService materialSubCategoriaService = new MaterialSubCategoriaService();

        ListenableFuture<List<MaterialSubCategoria.Retorno>> materialSubCategoria = materialSubCategoriaService.getSubCategoria(bearer, idCategoria);

        materialSubCategoria.addListener(() -> {
            try{
                List<MaterialSubCategoria.Retorno> result = materialSubCategoria.get();

                runOnUiThread(() ->{
                    if(result == null){
                        finish();
                        irParaProdutos(this, idCategoria);
                    }else{
                        getRecycleSubCategoria.setAdapter(new ConcatAdapter(new SubCategoriaAdapter(this, result),
                                                                            new VoltarAdapter(this, this, null ,ViewType.SUB_CATEGORIA.ordinal())));
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
}