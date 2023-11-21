package com.example.w_corpandroidpedido.Atividades.Material;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.w_corpandroidpedido.Atividades.Impressora.Impressora;
import com.example.w_corpandroidpedido.Models.Material.ListaMaterial;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Material.BuscarMaterialService;
import com.example.w_corpandroidpedido.Service.Material.MaterialService;
import com.example.w_corpandroidpedido.Util.Adapter.Material.MaterialAdapter;
import com.example.w_corpandroidpedido.Util.Adapter.Material.MaterialInformacaoAdapter;
import com.example.w_corpandroidpedido.Util.Adapter.Util.VoltarAdapter;
import com.example.w_corpandroidpedido.Util.Enum.ViewType;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MaterialInformacaoActivity extends AppCompatActivity {
    private RecyclerView getRecycleMaterialInformacao;
    private int idMaterial;
    private String nomeMaterial;
    private String valorProduto;
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

        idMaterial = intent.getIntExtra(MaterialActivity.ID_MATERIAL, 0);
        nomeMaterial = intent.getStringExtra(MaterialActivity.NOME_MATERIAL);
        valorProduto = intent.getStringExtra(MaterialActivity.VALOR_MATERIAL);
        multiplaSelecao = intent.getBooleanExtra(MaterialActivity.MULTIPLA_SELECAO, false);
        comboCategoriaFilho = intent.getBooleanExtra(MaterialActivity.COMBO_CATEGORIA, false);
        qtdSelecao = intent.getIntExtra(MaterialActivity.QTD_SELECAO, 0);

        getRecycleMaterialInformacao = findViewById(R.id.viewMaterialInformacao);

        getRecycleMaterialInformacao.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getRecycleMaterialInformacao.setHasFixedSize(true);

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
                                getRecycleMaterialInformacao.setAdapter(new MaterialInformacaoAdapter(this, nomeMaterial, valorProduto, true, qtdSelecao, listMaterial));
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
                                getRecycleMaterialInformacao.setAdapter(new MaterialInformacaoAdapter(this, nomeMaterial, valorProduto, true, listMaterial));
                            }
                        });
                    }catch (Exception e){
                        System.out.println("Erro :" + e.getMessage());
                    }
                }, MoreExecutors.directExecutor());
            }
        }
        else {
            getRecycleMaterialInformacao.setAdapter(new MaterialInformacaoAdapter(this, String.valueOf(idMaterial), nomeMaterial, valorProduto));
        }

        getBtnAdicionar = findViewById(R.id.btnAdicionarProduto);
        getBtnVoltar = findViewById(R.id.btnVoltar);

        getBtnAdicionar.setOnClickListener(view ->{
            adicionarProduto(this, nomeMaterial, valorProduto, "1");
        });

        getBtnVoltar.setOnClickListener(view ->{
            voltarParaProduto();
        });
    }

    public void adicionarProduto(Context context, String nomeMaterial, String valorMaterial, String qtdMaterial){
        Intent intent = new Intent(context, Impressora.class);
        context.startActivity(intent);
    }

    private void voltarParaProduto(){
        finish();
    }
}