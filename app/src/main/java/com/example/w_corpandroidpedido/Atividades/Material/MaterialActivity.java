package com.example.w_corpandroidpedido.Atividades.Material;


import androidx.appcompat.app.AlertDialog;
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
import com.example.w_corpandroidpedido.Models.Material.ListaMaterial;
import com.example.w_corpandroidpedido.Models.Material.MaterialCategoriaSelecionado;
import com.example.w_corpandroidpedido.R;

import com.example.w_corpandroidpedido.Service.Material.BuscarMaterialCategoriaService;
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
    public static final String ID_MATERIAL = "com.example.w_corpandroidpedido.IDMATERIAL";
    public static final String NOME_MATERIAL = "com.example.w_corpandroidpedido.NOMEMATERIAL";
    public static final String VALOR_MATERIAL = "com.example.w_corpandroidpedido.VALORMATERIAL";
    public static final String MULTIPLA_SELECAO = "com.example.w_corpandroidpedido.MULTIPLASELECAO";
    public static final String COMBO_CATEGORIA = "com.example.w_corpandroidpedido.COMBOCATEGORIA";
    public static final String QTD_SELECAO = "com.example.w_corpandroidpedido.QTDSELECAO";
    public static final String ITEMS = "com.example.w_corpandroidpedido.ITEMS";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private String bearer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        Intent intent = getIntent();

        idSubCategoria = intent.getIntExtra(SubCategoriaActivity.ID_SUBCATEGORIA, 0);
        multiplaSelecao = intent.getBooleanExtra(SubCategoriaActivity.MULTIPLA_SELECAO, false);
        qtdSelecao = intent.getIntExtra(SubCategoriaActivity.QTD_SELECAO, 0);
        comboCategoriaFilho = intent.getBooleanExtra(SubCategoriaActivity.COMBO_CATEGORIA_FILHO, false);


        getRecycleMaterial = findViewById(R.id.viewProduto);
        getRecycleMaterial.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        getRecycleMaterial.setHasFixedSize(true);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        pesquisarMateriais();

    }

    @Override
    protected void onResume(){
        super.onResume();
        pesquisarMateriais();
    }

    private void pesquisarMateriais(){
        MaterialService materialService = new MaterialService();

        ListenableFuture<ListaMaterial> materialCategoria = materialService.getMaterial(bearer, idSubCategoria);

        materialCategoria.addListener(() -> {
            try{
                ListaMaterial result = materialCategoria.get();
                runOnUiThread(() ->{
                    if(result.validated){
                        if(multiplaSelecao){
                            getRecycleMaterial.setAdapter(new ConcatAdapter(new MaterialAdapter(this, result.retorno, true, qtdSelecao),
                                    new VoltarAdapter(this, this, ViewType.MATERIAL.ordinal())));
                        }else if(comboCategoriaFilho){
                            getRecycleMaterial.setAdapter(new ConcatAdapter(new MaterialAdapter(this, result.retorno,true),
                                    new VoltarAdapter(this, this, ViewType.MATERIAL.ordinal())));
                        }
                        else{
                            getRecycleMaterial.setAdapter(new ConcatAdapter(new MaterialAdapter(this, result.retorno),
                                    new VoltarAdapter(this ,this, ViewType.MATERIAL.ordinal())));
                        }
                    }else if(result.hasInconsistence){
                        AlertDialog.Builder alert = new AlertDialog.Builder(MaterialActivity.this);
                        alert.setTitle("Atenção");
                        alert.setMessage(result.inconsistences.get(0).text);
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

    public void irParaProdutoInformacao(Context context, int idMaterial, String nomeMaterial, double valorMaterial){
        Intent intent = new Intent(context, MaterialInformacaoActivity.class);

        intent.putExtra(ID_MATERIAL, idMaterial);
        intent.putExtra(NOME_MATERIAL, nomeMaterial);

        String valorString = String.valueOf(valorMaterial);
        intent.putExtra(VALOR_MATERIAL, valorString);

        context.startActivity(intent);
    }
    public void irParaProdutoInformacao(Context context, boolean multiplaSelecao, int qtdSelecao, ArrayList<Integer> listIdMateriais){
        Intent intent = new Intent(context, MaterialInformacaoActivity.class);

        intent.putExtra(MULTIPLA_SELECAO, multiplaSelecao);
        intent.putExtra(QTD_SELECAO, qtdSelecao);
        int[] arr = listIdMateriais.stream().mapToInt(i -> i).toArray();
        intent.putExtra(ITEMS, arr);

        context.startActivity(intent);
    }

    public void irParaProdutoInformacao(Context context, boolean comboCategoriaFilho, ArrayList<Integer> listIdMateriais){
        Intent intent = new Intent(context, MaterialInformacaoActivity.class);

        intent.putExtra(COMBO_CATEGORIA, comboCategoriaFilho);
        intent.putExtra(QTD_SELECAO, qtdSelecao);
        int[] arr = listIdMateriais.stream().mapToInt(i -> i).toArray();
        intent.putExtra(ITEMS, arr);

        context.startActivity(intent);
    }
}