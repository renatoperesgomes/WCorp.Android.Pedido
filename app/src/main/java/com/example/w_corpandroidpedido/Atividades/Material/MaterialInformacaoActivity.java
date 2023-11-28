package com.example.w_corpandroidpedido.Atividades.Material;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.w_corpandroidpedido.Atividades.Impressora.Impressora;
import com.example.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidosActivity;
import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.BuscarMaterialService;
import com.example.w_corpandroidpedido.Util.Adapter.Material.MaterialInformacaoAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class MaterialInformacaoActivity extends AppCompatActivity {
    private RecyclerView getRecycleMaterialInformacao;
    private boolean multiplaSelecao;
    private boolean comboCategoriaFilho;
    private int qtdSelecao;
    private ArrayList<Integer> listIdMateriais;
    private ArrayList<Material> listMaterial = new ArrayList<>();
    private BuscarMaterialService buscarMaterialService = new BuscarMaterialService();
    private Button getBtnAdicionar;
    private Button getBtnVoltar;

    private DadosComanda dadosComanda = PesquisarPedidosActivity.dadosComanda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_informacao);

        CardView inicio = findViewById(R.id.cardInicio);
        CardView pagamento = findViewById(R.id.cardPagamento);
        CardView comanda = findViewById(R.id.cardComanda);
        TextView numeroComanda = findViewById(R.id.txtIdComanda);
        TextView valorComanda = findViewById(R.id.txtValorComanda);

        Intent intent = getIntent();

        multiplaSelecao = intent.getBooleanExtra(MaterialActivity.MULTIPLA_SELECAO, false);
        comboCategoriaFilho = intent.getBooleanExtra(MaterialActivity.COMBO_CATEGORIA, false);
        qtdSelecao = intent.getIntExtra(MaterialActivity.QTD_SELECAO, 0);

        getRecycleMaterialInformacao = findViewById(R.id.viewMaterialInformacao);

        getRecycleMaterialInformacao.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecycleMaterialInformacao.setHasFixedSize(true);


        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(inicio, pagamento,comanda);
        navegacaoBarraApp.addClick(this);

        if(dadosComanda != null){
            numeroComanda.setText(dadosComanda.numeroComanda);
            valorComanda.setText(dadosComanda.valorComanda);
        }


        if(multiplaSelecao) {
            listIdMateriais = (ArrayList<Integer>) Arrays.stream(Objects.requireNonNull(intent.getIntArrayExtra(MaterialActivity.ITEMS))).boxed().collect(Collectors.toList());

            for(int i = 0; i < listIdMateriais.size(); i++){
                ListenableFuture<Material> material = buscarMaterialService.buscarMaterial(listIdMateriais.get(i));
                material.addListener(() -> {
                    try{
                        Material result = material.get();
                        runOnUiThread(() ->{
                            listMaterial.add(result);
                            if(listMaterial.size() == listIdMateriais.size()){
                                getRecycleMaterialInformacao.setAdapter(new MaterialInformacaoAdapter(this,true, qtdSelecao, listMaterial));
                            }
                        });
                    }catch (Exception e){
                        System.out.println("Erro :" + e.getMessage());
                    }
                }, MoreExecutors.directExecutor());
            }
        }else if(comboCategoriaFilho){
            listIdMateriais = (ArrayList<Integer>) Arrays.stream(Objects.requireNonNull(intent.getIntArrayExtra(MaterialActivity.ITEMS))).boxed().collect(Collectors.toList());

            for(int i = 0; i < listIdMateriais.size(); i++){
                ListenableFuture<Material> material = buscarMaterialService.buscarMaterial(listIdMateriais.get(i));
                material.addListener(() -> {
                    try{
                        Material result = material.get();
                        runOnUiThread(() ->{
                            listMaterial.add(result);
                            if(listMaterial.size() == listIdMateriais.size()){
                                getRecycleMaterialInformacao.setAdapter(new MaterialInformacaoAdapter(this,true, listMaterial));
                            }
                        });
                    }catch (Exception e){
                        System.out.println("Erro :" + e.getMessage());
                    }
                }, MoreExecutors.directExecutor());
            }
        }
        else {
            listIdMateriais = (ArrayList<Integer>) Arrays.stream(Objects.requireNonNull(intent.getIntArrayExtra(MaterialActivity.ITEMS))).boxed().collect(Collectors.toList());

            for(int i = 0; i < listIdMateriais.size(); i++){
                ListenableFuture<Material> material = buscarMaterialService.buscarMaterial(listIdMateriais.get(i));
                material.addListener(() -> {
                    try{
                        Material result = material.get();
                        runOnUiThread(() ->{
                            listMaterial.add(result);
                            if(listMaterial.size() == listIdMateriais.size()){
                                getRecycleMaterialInformacao.setAdapter(new MaterialInformacaoAdapter(this, listMaterial));
                            }
                        });
                    }catch (Exception e){
                        System.out.println("Erro :" + e.getMessage());
                    }
                }, MoreExecutors.directExecutor());
            }
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