package com.example.w_corpandroidpedido.Atividades.Material;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.w_corpandroidpedido.R;

public class MaterialInformacaoActivity extends AppCompatActivity {
    private int idMaterial;
    private String nomeMaterial;
    private String valorProduto;
    private EditText getTxtNomeMaterial;
    private EditText getTxtPrecoMaterial;
    private EditText getTxtQtdMaterial;
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

        getTxtNomeMaterial = findViewById(R.id.txtNomeMaterial);
        getTxtPrecoMaterial = findViewById(R.id.txtPrecoMaterial);
        getTxtQtdMaterial = findViewById(R.id.txtQtdMaterial);
        getBtnAdicionar = findViewById(R.id.btnAdicionarProduto);
        getBtnVoltar = findViewById(R.id.btnVoltar);

        getTxtNomeMaterial.setText(nomeMaterial);
        getTxtPrecoMaterial.setText(valorProduto);
        getTxtQtdMaterial.setText("1");

        getBtnAdicionar.setOnClickListener(view ->{
            adicionarProduto(nomeMaterial, valorProduto, "1");
        });

        getBtnVoltar.setOnClickListener(view ->{
            voltarParaProduto();
        });
    }

    private void adicionarProduto(String nomeMaterial, String valorMaterial, String qtdMaterial){
        System.out.println(nomeMaterial);
        System.out.println(valorMaterial);
    }

    private void voltarParaProduto(){
        finish();
    }
}