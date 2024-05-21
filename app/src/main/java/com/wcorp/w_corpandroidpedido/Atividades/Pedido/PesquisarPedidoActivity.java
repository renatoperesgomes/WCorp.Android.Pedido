package com.wcorp.w_corpandroidpedido.Atividades.Pedido;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Atividades.Categoria.CategoriaActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.Models.Pedido.Pedido;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Service.Pedido.PedidoService;
import com.wcorp.w_corpandroidpedido.Util.DataStore;

import java.text.NumberFormat;
import java.util.Locale;

import io.reactivex.Flowable;

public class PesquisarPedidoActivity extends AppCompatActivity {

    private EditText pesquisarComanda;
    private String nmrComanda;
    private String bearer = "";
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_pedido);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        CardView cardViewInicioMenu = findViewById(R.id.cardInicio);
        CardView cardViewPagamentoMenu = findViewById(R.id.cardPagamento);
        CardView cardViewComandaMenu = findViewById(R.id.cardComanda);

        pesquisarComanda = findViewById(R.id.textPesquisarComanda);
        Button btnPesquisar = findViewById(R.id.btnPesquisar);
        TextView txtNumeroComanda = findViewById(R.id.txtNumeroComanda);
        TextView txtValorComanda = findViewById(R.id.txtValorComanda);

        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu,cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        if(dadosComanda.GetPedido() != null){
            navegacaoBarraApp.addClick(this);

            txtNumeroComanda.setText(dadosComanda.GetNumeroComanda());
            txtValorComanda.setText(formatNumero.format(dadosComanda.GetValorComanda()));
        }else{
            txtNumeroComanda.setTextColor(Color.parseColor("#FF0000"));
            txtValorComanda.setTextColor(Color.parseColor("#FF0000"));
            navegacaoBarraApp.addClickError(this);
        }

        pesquisarComanda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Context context = getBaseContext();
                    buscarComanda(context);
                    return true;
                }
                return false;
            }
        });

        btnPesquisar.setOnClickListener(view -> {
            buscarComanda(this);
        });
    }

    private void buscarComanda(Context context) {
        PedidoService buscarComandaService = new PedidoService();
        nmrComanda = pesquisarComanda.getText().toString();

        if (nmrComanda.isEmpty()) {
            Toast.makeText(this, "Por favor, selecione um número de comanda", Toast.LENGTH_SHORT).show();
        } else {
            ListenableFuture<Pedido> buscarPedido = buscarComandaService.BuscarPedido(bearer, Integer.parseInt(nmrComanda));

            buscarPedido.addListener(() -> {
                runOnUiThread(() -> {
                    try {
                        Pedido retornoPedido = buscarPedido.get();
                        if (retornoPedido.validated) {
                            if (retornoPedido.retorno == null) {
                                dadosComanda.SetPedido(null);
                                dadosComanda.SetNumeroComanda(nmrComanda);
                                dadosComanda.SetValorComanda(0.00);
                            } else {
                                dadosComanda.SetPedido(retornoPedido);
                            }
                            irParaPaginaInicial(context);
                        } else if (retornoPedido.hasInconsistence) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(PesquisarPedidoActivity.this);
                            alert.setTitle("Atenção");
                            StringBuilder inconsistencesJoin = new StringBuilder();
                            for (Inconsistences inconsistences :
                                    retornoPedido.inconsistences) {
                                inconsistencesJoin.append(inconsistences.text + "\n");
                            }
                            alert.setMessage(inconsistencesJoin);
                            alert.setCancelable(false);
                            alert.setPositiveButton("OK", null);
                            alert.show();
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            AlertDialog.Builder alert = new AlertDialog.Builder(PesquisarPedidoActivity.this);
                            alert.setTitle("Atenção");
                            alert.setMessage(e.getMessage());
                            alert.setCancelable(false);
                            alert.setPositiveButton("OK", null);
                            alert.show();
                        });
                    }
                });
            }, MoreExecutors.directExecutor());
        }
    }
    private void irParaPaginaInicial(Context context){
        Intent intent = new Intent(context , CategoriaActivity.class);
        this.startActivity(intent);
    }
}