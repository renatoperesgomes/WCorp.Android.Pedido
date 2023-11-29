package com.example.w_corpandroidpedido;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Empresa.Empresa;
import com.example.w_corpandroidpedido.Models.Empresa.ListEmpresa;
import com.example.w_corpandroidpedido.Models.Usuario.Usuario;
import com.example.w_corpandroidpedido.Service.Empresa.EmpresaService;
import com.example.w_corpandroidpedido.Service.Usuario.UsuarioService;
import com.example.w_corpandroidpedido.Util.Adapter.Empresa.EmpresaAdapter;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class MainActivity extends AppCompatActivity {
    private static final Preferences.Key<String> STRING_KEY = new Preferences.Key<>("authentication");
    private static final Preferences.Key<String> STRING_KEY_EMPRESA = new Preferences.Key<>("empresa");
    private EditText getTxtNomeUsuario;
    private EditText getTxtSenhaUsuario;
    private Button getBotaoLogin;
    private Spinner getEmpresa;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTxtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        getTxtSenhaUsuario  = findViewById(R.id.txtSenhaUsuario);
        getBotaoLogin = findViewById(R.id.botaoLogin);
        getEmpresa = findViewById(R.id.selectEmpresa);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        EmpresaService empresaService = new EmpresaService();
        ListenableFuture<ListEmpresa> listEmpresa = empresaService.BuscarListEmpresa();

        listEmpresa.addListener(() ->{
            try{
                runOnUiThread(() ->{
                    try {
                        ListEmpresa listEmpresaRetorno = listEmpresa.get();

                        getEmpresa.setAdapter(new EmpresaAdapter(this, listEmpresaRetorno.retorno));
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e.getCause());
                    }
                });
            }catch(Exception e){
                System.out.println("Erro :" + e.getMessage());
            }
        }, MoreExecutors.directExecutor());

        getEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idEmpresa = String.valueOf(id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("ERRO");
            }
        });

        getBotaoLogin.setOnClickListener(view -> {
            String nomeUsuario = getTxtNomeUsuario.getText().toString();
            String senhaUsuario = getTxtSenhaUsuario.getText().toString();

            if(nomeUsuario.isEmpty() || senhaUsuario.isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Atenção");
                alert.setMessage("Usuário/Senha não preenchidos!");
                alert.setCancelable(false);
                alert.setPositiveButton("OK", null);
                alert.show();
            }else{
                UsuarioService usuarioService = new UsuarioService();
                ListenableFuture<Usuario> tokenRetorno =  usuarioService.Login(nomeUsuario, senhaUsuario, idEmpresa);
                tokenRetorno.addListener(() ->{
                    try{
                        Usuario tokenRetornoResult = tokenRetorno.get();
                        runOnUiThread(() ->{
                            if(tokenRetornoResult.validated){
                                Single<Preferences> updateResult =  dataStore.updateDataAsync(prefsIn -> {
                                    MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
                                    mutablePreferences.set(STRING_KEY, tokenRetornoResult.retorno);
                                    return Single.just(mutablePreferences);
                                });
                                logarUsuario(this);
                            }else if(tokenRetornoResult.hasInconsistence){
                                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                alert.setTitle("Atenção");
                                alert.setMessage(tokenRetornoResult.inconsistences.get(0).text);
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
        });
    }
    private void logarUsuario(Context context){
        Intent intent = new Intent(context, CategoriaActivity.class);
        startActivity(intent);
    }
}


