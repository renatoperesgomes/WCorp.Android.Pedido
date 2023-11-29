package com.example.w_corpandroidpedido.Atividades.Material;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.w_corpandroidpedido.Atividades.Impressora.Impressora;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Material.ListMaterial;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.MaterialService;
import com.example.w_corpandroidpedido.Util.Adapter.Material.MaterialInformacaoAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.Flowable;

public class MaterialInformacaoActivity extends AppCompatActivity {
    private RecyclerView getRecycleMaterialInformacao;
    private boolean multiplaSelecao;
    private boolean comboCategoriaFilho;
    private int qtdSelecao;
    private ArrayList<Integer> listIdMateriais;
    private ArrayList<Material> listMaterial = new ArrayList<>();
    private Button getBtnAdicionar;
    private Button getBtnVoltar;
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private String bearer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_informacao);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        CardView cardViewInicioMenu = findViewById(R.id.cardInicio);
        CardView cardViewPagamentoMenu = findViewById(R.id.cardPagamento);
        CardView cardViewComandaMenu = findViewById(R.id.cardComanda);

        Intent intent = getIntent();

        multiplaSelecao = intent.getBooleanExtra(MaterialActivity.MULTIPLA_SELECAO, false);
        comboCategoriaFilho = intent.getBooleanExtra(MaterialActivity.COMBO_CATEGORIA, false);
        qtdSelecao = intent.getIntExtra(MaterialActivity.QTD_SELECAO, 0);

        getRecycleMaterialInformacao = findViewById(R.id.viewMaterialInformacao);

        getRecycleMaterialInformacao.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecycleMaterialInformacao.setHasFixedSize(true);

        MaterialService materialService = new MaterialService();

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu,cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        listIdMateriais = (ArrayList<Integer>) Arrays.stream(Objects.requireNonNull(intent.getIntArrayExtra(MaterialActivity.ITEMS))).boxed().collect(Collectors.toList());

        for(int i = 0; i < listIdMateriais.size(); i++){
            ListenableFuture<ListMaterial> material = materialService.BuscarMaterial(bearer, listIdMateriais.get(i), null);
            int finalI = i;
            material.addListener(() -> {
                try{
                    ListMaterial result = material.get();
                    runOnUiThread(() ->{
                        listMaterial.add(result.retorno.get(finalI));
                        if(listMaterial.size() == listIdMateriais.size()){

                            if(multiplaSelecao){
                                getRecycleMaterialInformacao.setAdapter(new MaterialInformacaoAdapter(this,true, qtdSelecao, listMaterial));
                            }else if(comboCategoriaFilho){
                                getRecycleMaterialInformacao.setAdapter(new MaterialInformacaoAdapter(this,true, listMaterial));
                            }else{
                                getRecycleMaterialInformacao.setAdapter(new MaterialInformacaoAdapter(this, listMaterial));
                            }

                        }
                    });
                }catch (Exception e){
                    System.out.println("Erro :" + e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }


        getBtnAdicionar = findViewById(R.id.btnAdicionarProduto);
        getBtnVoltar = findViewById(R.id.btnVoltar);

        getBtnAdicionar.setOnClickListener(view ->{
            adicionarProduto(this);
        });

        getBtnVoltar.setOnClickListener(view ->{
            voltarParaMaterialActivity();
        });
    }

    public void adicionarProduto(Context context){
        Intent intent = new Intent(context, Impressora.class);
        context.startActivity(intent);
    }

    private void voltarParaMaterialActivity(){
        finish();
    }
}