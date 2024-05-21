package com.wcorp.w_corpandroidpedido;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidoActivity;
import com.wcorp.w_corpandroidpedido.Models.BancoDados.BancoDados;
import com.wcorp.w_corpandroidpedido.Models.Empresa.Empresa;
import com.wcorp.w_corpandroidpedido.Models.Empresa.ListEmpresa;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.Models.Usuario.Usuario;
import com.wcorp.w_corpandroidpedido.Service.BancoDados.BancoDadosService;
import com.wcorp.w_corpandroidpedido.Service.Empresa.EmpresaService;
import com.wcorp.w_corpandroidpedido.Service.Usuario.UsuarioService;
import com.wcorp.w_corpandroidpedido.Util.Adapter.Empresa.EmpresaAdapter;
import com.wcorp.w_corpandroidpedido.Util.ApiCall;
import com.wcorp.w_corpandroidpedido.Util.DataStore;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Single;

public class MainActivity extends AppCompatActivity {
    private static final Preferences.Key<String> STRING_KEY = new Preferences.Key<>("authentication");
    private static Empresa EmpresaSelecionada = new Empresa();
    private EditText getTxtNomeUsuario;
    private EditText getTxtSenhaUsuario;
    private Button getBotaoLogin;
    private Spinner getEmpresa;
    private String idEmpresa;
    private Dialog dialogLoading;
    private RxDataStore<Preferences> dataStore;
    private final String android_id = Build.SERIAL;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static Empresa EmpresaSelecionada() {
        return EmpresaSelecionada;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTxtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        getTxtSenhaUsuario  = findViewById(R.id.txtSenhaUsuario);
        getBotaoLogin = findViewById(R.id.botaoLogin);
        getEmpresa = findViewById(R.id.selectEmpresa);

        dataStore = DataStore.getInstance(this);

        BancoDadosService bancoDadosService = new BancoDadosService();

        abrirDialogLoading();
        ListenableFuture<BancoDados> bancoDadosAcesso = bancoDadosService.BuscarAcesso(android_id);
        bancoDadosAcesso.addListener(() ->{
            try{
                BancoDados bancoDados = bancoDadosAcesso.get();
                runOnUiThread(() ->{
                    if(bancoDados.validated){
                        ApiCall.BaseUrl = bancoDados.retorno.urlAcesso;
                        PagamentoCall.CodigoAtivacaoTerminal = bancoDados.retorno.codigoAutorizacao;
                        buscarEmpresas();
                    }else if(bancoDados.hasInconsistence){
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Atenção");
                        StringBuilder inconsistencesJoin = new StringBuilder();
                        for (Inconsistences inconsistences :
                                bancoDados.inconsistences) {
                            inconsistencesJoin.append(inconsistences.text).append("\n");
                        }
                        alert.setMessage(inconsistencesJoin);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();

                        getBotaoLogin.setClickable(false);
                    }
                });
            }catch (Exception e){
                runOnUiThread(() -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Atenção");
                    alert.setMessage(e.getMessage());
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", null);
                    alert.show();

                    getBotaoLogin.setClickable(false);
                });
            }
        }, MoreExecutors.directExecutor());

        getTxtSenhaUsuario.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Context context = getBaseContext();
                    logarUsuario(context);
                    return true;
                }
                return false;
            }
        });

        getBotaoLogin.setOnClickListener(view -> {
            logarUsuario(this);
        });

        getEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmpresaSelecionada.id = Integer.parseInt(String.valueOf(id));
                EmpresaSelecionada = (Empresa) view.getTag();
                idEmpresa = String.valueOf(id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("ERRO");
            }
        });
    }

    private void buscarEmpresas(){
        EmpresaService empresaService = new EmpresaService();
        Future<ListEmpresa> listEmpresa = empresaService.BuscarListEmpresa();
        executor.execute(() ->{
            try {
                ListEmpresa listEmpresaRetorno = listEmpresa.get();
                runOnUiThread(() -> {
                    if (listEmpresaRetorno.validated) {
                        getEmpresa.setAdapter(new EmpresaAdapter(this, listEmpresaRetorno.retorno));
                        dialogLoading.dismiss();
                    } else if (listEmpresaRetorno.hasInconsistence) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Atenção");
                        StringBuilder inconsistencesJoin = new StringBuilder();
                        for (Inconsistences inconsistences :
                                listEmpresaRetorno.inconsistences) {
                            inconsistencesJoin.append(inconsistences.text).append("\n");
                        }
                        alert.setMessage(inconsistencesJoin);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                });
                dialogLoading.dismiss();
            }catch(Exception e) {
                runOnUiThread(() -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Atenção");
                    alert.setMessage(e.getMessage());
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", null);
                    alert.show();

                    getBotaoLogin.setClickable(false);
                    dialogLoading.dismiss();
                });
            }
        });
    }
    private void logarUsuario(Context context) {
        abrirDialogLoading();
        String nomeUsuario = getTxtNomeUsuario.getText().toString();
        String senhaUsuario = getTxtSenhaUsuario.getText().toString();
        UsuarioService usuarioService = new UsuarioService();
        ListenableFuture<Usuario> tokenRetorno = usuarioService.Login(nomeUsuario, senhaUsuario, idEmpresa);
        tokenRetorno.addListener(() -> {
            try {
                Usuario tokenRetornoResult = tokenRetorno.get();
                runOnUiThread(() -> {
                    if (tokenRetornoResult.validated) {
                        Single<Preferences> updateResult = dataStore.updateDataAsync(prefsIn -> {
                            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
                            mutablePreferences.set(STRING_KEY, tokenRetornoResult.retorno);
                            return Single.just(mutablePreferences);
                        });

                        Intent intent = new Intent(context, PesquisarPedidoActivity.class);
                        startActivity(intent);
                    } else if (tokenRetornoResult.hasInconsistence) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Atenção");
                        StringBuilder inconsistencesJoin = new StringBuilder();
                        for (Inconsistences inconsistences :
                                tokenRetornoResult.inconsistences) {
                            inconsistencesJoin.append(inconsistences.text + "\n");
                        }
                        alert.setMessage(inconsistencesJoin);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                    dialogLoading.dismiss();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Atenção");
                    alert.setMessage(e.getMessage());
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", null);
                    alert.show();

                    dialogLoading.dismiss();
                });
            }
        }, MoreExecutors.directExecutor());
    }

    private void abrirDialogLoading(){
        dialogLoading = new Dialog(this);
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoading.setContentView(R.layout.loading);
        dialogLoading.setCancelable(false);

        dialogLoading.show();
    }
}


