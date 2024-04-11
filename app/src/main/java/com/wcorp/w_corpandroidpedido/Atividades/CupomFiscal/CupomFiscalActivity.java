package com.wcorp.w_corpandroidpedido.Atividades.CupomFiscal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.test.espresso.core.internal.deps.guava.util.concurrent.MoreExecutors;

import com.google.common.util.concurrent.ListenableFuture;
import com.wcorp.w_corpandroidpedido.Atividades.Pedido.PesquisarPedidoActivity;
import com.wcorp.w_corpandroidpedido.Menu.DadosComanda;
import com.wcorp.w_corpandroidpedido.Models.CupomFiscal.CupomFiscal;
import com.wcorp.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Service.CupomFiscal.CupomFiscalService;
import com.wcorp.w_corpandroidpedido.Util.DataStore;
import com.wcorp.w_corpandroidpedido.Util.Enum.StatusCupomFiscal;
import com.wcorp.w_corpandroidpedido.Util.Impressora.GerarBitmap;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;
import com.wcorp.w_corpandroidpedido.Util.Util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrinterData;
import io.reactivex.Flowable;

public class CupomFiscalActivity extends AppCompatActivity {

    private final Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final PlugPag plugPag = PagamentoCall.plugPag;
    private String bearer;
    private EditText edtTextCpfCnpj;
    private RadioButton rdbCpf;
    private RadioButton rdbCnpj;
    private RadioButton rdbNaoInformar;
    boolean imprimirCupomFiscal = false;
    DadosComanda dadosComanda = DadosComanda.GetDadosComanda();
    private Dialog dialogLoading;
    private static final String mascaraCNPJ = "##.###.###/####-##";
    private static final String mascaraCPF = "###.###.###-##";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupom_fiscal);

        Button btnEmitirCupom = findViewById(R.id.btnEmitirCupom);
        Button btnVoltar = findViewById(R.id.btnVoltar);
        edtTextCpfCnpj = findViewById(R.id.edtTextCpfCnpj);
        rdbCpf = findViewById(R.id.rdbCpf);
        rdbCpf.setChecked(true);
        rdbCnpj = findViewById(R.id.rdbCnpj);
        rdbNaoInformar = findViewById(R.id.rdbNaoInformado);

        RxDataStore<Preferences> dataStore = DataStore.getInstance(this);

        Flowable<String> getBearer =
                dataStore.data().map(prefs -> prefs.get(BEARER));

        bearer = getBearer.blockingFirst();

        rdbNaoInformar.setOnClickListener(view ->{
            edtTextCpfCnpj.setText("");
        });

        addMascaraDoc();
        btnEmitirCupom.setOnClickListener(view -> {
            criarReceiptCupomFiscal(this);
        });

        btnVoltar.setOnClickListener(view -> {
            voltarPagina();
        });
    }

    private static String tirarMascaraCpfCnpj(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    private void addMascaraDoc() {
        edtTextCpfCnpj.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Não faz nada
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String str = tirarMascaraCpfCnpj(charSequence.toString());
                String mask;
                String defaultMask = selecionarMascara(str);
                switch (str.length()) {
                    case 11:
                        mask = mascaraCPF;
                        break;
                    case 14:
                        mask = mascaraCNPJ;
                        break;

                    default:
                        mask = defaultMask;
                        break;
                }

                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if ((m != '#' && str.length() > old.length()) || (m != '#' && str.length() < old.length() && str.length() != i)) {
                        mascara += m;
                        continue;
                    }

                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                edtTextCpfCnpj.setText(mascara);
                edtTextCpfCnpj.setSelection(mascara.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Não faz nada
            }
        });
    }

    private static String selecionarMascara(String str) {
        String mascaraPadrao = mascaraCPF;
        if (str.length() > 11) {
            mascaraPadrao = mascaraCNPJ;
        }
        return mascaraPadrao;
    }

    private void criarReceiptCupomFiscal(Context context) {
        String getTextoCpfCnpj = edtTextCpfCnpj.getText().toString();

        if (rdbCpf.isChecked() && tirarMascaraCpfCnpj(getTextoCpfCnpj).length() != 11) {
            Toast.makeText(this, "Por favor, coloque um CPF válido!", Toast.LENGTH_SHORT).show();
        } else if (rdbCnpj.isChecked() && tirarMascaraCpfCnpj(getTextoCpfCnpj).length() != 14) {
            Toast.makeText(this, "Por favor, coloque um CNPJ válido!", Toast.LENGTH_SHORT).show();
        } else {
            Integer idPedido = dadosComanda.GetPedido().retorno.id;
            String numeroCpf = "";
            String numeroCnpj = "";

            if (rdbCpf.isChecked())
                numeroCpf = getTextoCpfCnpj;
            else if (rdbCnpj.isChecked())
                numeroCnpj = getTextoCpfCnpj;

            gerarCupomFiscal(context, bearer, idPedido, numeroCpf, numeroCnpj);
        }
    }

    private void gerarCupomFiscal(Context context, String bearer, Integer idPedido, String numeroCpf, String numeroCnpj) {
        abrirDialogLoading(context);
        CupomFiscalService cupomFiscalService = new CupomFiscalService();
        executor.execute(() -> {
            ListenableFuture<CupomFiscal> cupomFiscal = cupomFiscalService.EmitirCupomFiscal(bearer, idPedido, numeroCpf, numeroCnpj);
            cupomFiscal.addListener(() -> {
                try {
                    CupomFiscal retornoCupomFiscal = cupomFiscal.get();
                    if (retornoCupomFiscal.validated) {
                        if(retornoCupomFiscal.retorno.statusCupomFiscal == StatusCupomFiscal.ENVIADO_SAT_APROVADO.ordinal()){
                            Bitmap bitmap = GerarBitmap.GerarBitmapCupomFiscal(context, retornoCupomFiscal);
                            imprimirCupomFiscal = Util.SalvarImagemEmExternalStorage(context, bitmap, "receiptCupomFiscal.bmp");
                            if (imprimirCupomFiscal) {
                                imprimirCupomFiscal();
                            }
                            dialogLoading.dismiss();
                            runOnUiThread(() ->{
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Cupom fiscal");
                                alert.setMessage("Cupom fiscal gerado com sucesso!");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        irParaPesquisaPedido(context);
                                    }
                                });
                                alert.show();
                            });
                        }else if(retornoCupomFiscal.retorno.statusCupomFiscal == StatusCupomFiscal.ENVIADO_SAT_REJEITADO.ordinal()){
                            dialogLoading.dismiss();
                            runOnUiThread(() ->{
                                StringBuilder inconsistencesJoin = new StringBuilder();
                                for (Inconsistences inconsistences :
                                        retornoCupomFiscal.inconsistences) {
                                    inconsistencesJoin.append(inconsistences.text).append("\n");
                                }
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Atenção");
                                alert.setMessage(retornoCupomFiscal.retorno.mensagemRejeicaoCfe);
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        irParaPesquisaPedido(context);
                                    }
                                });
                                alert.show();
                            });
                        }
                    } else if (retornoCupomFiscal.hasInconsistence && retornoCupomFiscal.retorno != null) {
                        dialogLoading.dismiss();

                        runOnUiThread(() ->{
                            StringBuilder inconsistencesJoin = new StringBuilder();
                            for (Inconsistences inconsistences :
                                    retornoCupomFiscal.inconsistences) {
                                inconsistencesJoin.append(inconsistences.text).append("\n");
                            }
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Atenção");
                            alert.setMessage(inconsistencesJoin);
                            alert.setCancelable(false);
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    irParaPesquisaPedido(context);
                                }
                            });
                            alert.show();
                        });
                    }else if(retornoCupomFiscal.hasInconsistence){
                        dialogLoading.dismiss();

                        runOnUiThread(() ->{
                            StringBuilder inconsistencesJoin = new StringBuilder();
                            for (Inconsistences inconsistences :
                                    retornoCupomFiscal.inconsistences) {
                                inconsistencesJoin.append(inconsistences.text).append("\n");
                            }
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Atenção");
                            alert.setMessage(inconsistencesJoin);
                            alert.setCancelable(false);
                            alert.setPositiveButton("OK", null);
                            alert.show();
                        });
                    }
                } catch (Exception e) {
                    dialogLoading.dismiss();
                    runOnUiThread(() ->{
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Atenção");
                        alert.setMessage(e.getMessage());
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                irParaPesquisaPedido(context);
                            }
                        });
                        alert.show();
                    });
                }
            }, MoreExecutors.directExecutor());
        });
    }

    private void imprimirCupomFiscal() {
        executor.execute(() ->{
            // Cria objeto com informações da impressão
            PlugPagPrinterData plugPagPrinterData = new PlugPagPrinterData(
                    "/storage/emulated/0/Android/data/com.wcorp.w_corpandroidpedido/files/Download/receiptCupomFiscal.bmp",
                    4, 10 * 12);

            plugPag.printFromFile(plugPagPrinterData);
        });
    }

    private void abrirDialogLoading(Context context){
        dialogLoading = new Dialog(context);
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoading.setContentView(R.layout.loading);
        dialogLoading.setCancelable(false);

        dialogLoading.show();
    }
    private void irParaPesquisaPedido(Context context){
        dadosComanda.SetPedido(null);
        Intent intent = new Intent(context, PesquisarPedidoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    private void voltarPagina(){
        finish();
    }
}