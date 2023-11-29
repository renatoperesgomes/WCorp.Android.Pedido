package com.example.w_corpandroidpedido.Atividades.Pedido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w_corpandroidpedido.Menu.DadosComanda;
import com.example.w_corpandroidpedido.Models.BaseApi;
import com.example.w_corpandroidpedido.Models.Pedido.Pedido;
import com.example.w_corpandroidpedido.Menu.NavegacaoBarraApp;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Service.Pedido.PedidoService;
import com.example.w_corpandroidpedido.Util.DataStore;
import com.google.common.util.concurrent.ListenableFuture;


import io.reactivex.Flowable;

public class PesquisarPedidoActivity extends AppCompatActivity {
    private TextView txtIdComanda;
    private TextView txtValorComanda;
    private EditText pesquisarComanda;
    private Button btnPesquisar;
    private String nmrComanda;
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private String bearer;
    public static DadosComanda dadosComanda;
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
        TextView numeroComanda = findViewById(R.id.txtIdComanda);
        TextView valorComanda = findViewById(R.id.txtValorComanda);

        pesquisarComanda = findViewById(R.id.textPesquisarComanda);
        btnPesquisar = findViewById(R.id.btnPesquisar);
        txtIdComanda = findViewById(R.id.txtIdComanda);
        txtValorComanda = findViewById(R.id.txtValorComanda);


        NavegacaoBarraApp navegacaoBarraApp = new NavegacaoBarraApp(cardViewInicioMenu, cardViewPagamentoMenu,cardViewComandaMenu);
        navegacaoBarraApp.addClick(this);

        if(dadosComanda != null){
            numeroComanda.setText(dadosComanda.numeroComanda);
            valorComanda.setText(dadosComanda.valorComanda);
        }

        btnPesquisar.setOnClickListener(view -> {
            PedidoService buscarComandaService = new PedidoService();
            nmrComanda = pesquisarComanda.getText().toString();

            if(nmrComanda.isEmpty()){
                Toast.makeText(this, "Por favor, selecione um número de comanda", Toast.LENGTH_SHORT).show();
            }else {
                ListenableFuture<Pedido> buscarPedido = buscarComandaService.BuscarPedido(bearer, Integer.parseInt(nmrComanda));

                buscarPedido.addListener(() -> {
                    runOnUiThread(() -> {
                        try {
                            Pedido retornoPedido = buscarPedido.get();

                            if(retornoPedido.validated && retornoPedido.comanda.isEmpty()){
                                txtIdComanda.setText(nmrComanda);
                                txtValorComanda.setText("0,00");

                            }else{
                                dadosComanda = new DadosComanda(retornoPedido);
                            }
                        } catch (Exception e) {
                            System.out.println("Erro: " + e.getMessage());
                        }
                    });
                }, MoreExecutors.directExecutor());
            }
        });
    }
}