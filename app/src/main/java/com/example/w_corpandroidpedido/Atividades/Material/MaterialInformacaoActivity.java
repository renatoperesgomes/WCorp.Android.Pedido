package com.example.w_corpandroidpedido.Atividades.Material;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Atividades.Impressora.Impressora;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.Navegacao.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Util.Adapter.Material.MaterialInformacaoAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_informacao);

        Intent intent = getIntent();

        multiplaSelecao = intent.getBooleanExtra(MaterialActivity.MULTIPLA_SELECAO, false);
        comboCategoriaFilho = intent.getBooleanExtra(MaterialActivity.COMBO_CATEGORIA, false);
        qtdSelecao = intent.getIntExtra(MaterialActivity.QTD_SELECAO, 0);

        getRecycleMaterialInformacao = findViewById(R.id.viewMaterialInformacao);

        getRecycleMaterialInformacao.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecycleMaterialInformacao.setHasFixedSize(true);

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
    private void voltarParaPaginaInicial(Context context){
        Intent intent = new Intent(context, CategoriaActivity.class);

        context.startActivity(intent);
    }

    public void adicionarProduto(Context context){
        Intent intent = new Intent(context, Impressora.class);
        context.startActivity(intent);
    }

    private void voltarParaMaterialActivity(){
        finish();
    }
}