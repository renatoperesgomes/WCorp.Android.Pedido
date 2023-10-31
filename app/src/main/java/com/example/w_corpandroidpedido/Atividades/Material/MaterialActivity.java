package com.example.w_corpandroidpedido.Atividades.Material;

import androidx.appcompat.app.AppCompatActivity;
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

import com.example.w_corpandroidpedido.Atividades.Categoria.SubCategoriaActivity;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.MaterialService;
import com.example.w_corpandroidpedido.Util.Adapter.MaterialAdapter;
import com.example.w_corpandroidpedido.Util.Adapter.VoltarAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.example.w_corpandroidpedido.Util.Enum.ViewType;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import io.reactivex.Flowable;

public class MaterialActivity extends AppCompatActivity {
    private RecyclerView getRecycleMaterial;
    private int idSubCategoria;
    public static final String ID_MATERIAL = "com.example.w_corpandroidpedido.IDMATERIAL";
    public static final String NOME_MATERIAL = "com.example.w_corpandroidpedido.IDMATERIAL";
    public static final String VALOR_MATERIAL = "com.example.w_corpandroidpedido.VALORMATERIAL";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private String bearer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        Intent intent = getIntent();
        idSubCategoria = intent.getIntExtra(SubCategoriaActivity.ID_SUBCATEGORIA, 0);

        getRecycleMaterial = findViewById(R.id.viewProduto);
        getRecycleMaterial.setLayoutManager(new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false));
        getRecycleMaterial.setHasFixedSize(true);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        MaterialService materialService = new MaterialService();

        ListenableFuture<Material> materialSubCategoria = materialService.getMaterial(bearer, idSubCategoria);

        materialSubCategoria.addListener(() -> {
            try{
                Material result = materialSubCategoria.get();
                runOnUiThread(() ->{
                    getRecycleMaterial.setAdapter(new ConcatAdapter(new MaterialAdapter(this, result.retorno),
                                                                    new VoltarAdapter(this ,null,this,ViewType.MATERIAL.ordinal())));
                });
            }catch (Exception e){
                System.out.println("Erro :" + e.getMessage());
            }
        }, MoreExecutors.directExecutor());
    }

    public void irParaProdutoInformacao(Context context, int idMaterial, String nomeMaterial, double valorMaterial){
        Intent intent = new Intent(context, MaterialInformacaoActivity.class);
        intent.putExtra(ID_MATERIAL, idMaterial);
        intent.putExtra(NOME_MATERIAL, nomeMaterial);

        String valorString = String.valueOf(valorMaterial);
        intent.putExtra(VALOR_MATERIAL, valorString);
        context.startActivity(intent);
    }
}