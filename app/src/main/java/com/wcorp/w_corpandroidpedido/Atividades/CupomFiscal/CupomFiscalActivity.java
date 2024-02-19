package com.wcorp.w_corpandroidpedido.Atividades.CupomFiscal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wcorp.w_corpandroidpedido.Atividades.Pedido.TipoPagamentoPedidoActivity;
import com.wcorp.w_corpandroidpedido.R;

public class CupomFiscalActivity extends AppCompatActivity {

    private Button btnEmitirCupom;
    private Button btnVoltar;
    private EditText edtTextCpfCnpj;
    private RadioButton rdbCpf;
    private RadioButton rdbCnpj;
    private RadioButton rdbNaoInformar;
    public static final String CUPOM_FISCAL = "com.example.w_corpandroidpedido.CUPOMFISCAL";
    public static final String CUPOM_CPF = "com.example.w_corpandroidpedido.CUPOMCPF";
    public static final String CUPOM_CNPJ = "com.example.w_corpandroidpedido.CUPOMCNPJ";
    private static final String mascaraCNPJ = "##.###.###/####-##";
    private static final String mascaraCPF = "###.###.###-##";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupom_fiscal);

        btnEmitirCupom = findViewById(R.id.btnEmitirCupom);
        btnVoltar = findViewById(R.id.btnVoltar);
        edtTextCpfCnpj = findViewById(R.id.edtTextCpfCnpj);
        rdbCpf = findViewById(R.id.rdbCpf);
        rdbCpf.setChecked(true);
        rdbCnpj = findViewById(R.id.rdbCnpj);
        rdbNaoInformar = findViewById(R.id.rdbNaoInformado);

        addMascaraDoc();
        btnEmitirCupom.setOnClickListener(view ->{
            criarReceiptCupomFiscal(this);
        });

        btnVoltar.setOnClickListener(view ->{
            voltarPagina();
        });
    }

    private static String tirarMascaraCpfCnpj(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    private void addMascaraDoc(){
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
        if (str.length() > 11){
            mascaraPadrao = mascaraCNPJ;
        }
        return mascaraPadrao;
    }
    private void criarReceiptCupomFiscal(Context context) {


        String getTextoCpfCnpj = edtTextCpfCnpj.getText().toString();

        if (rdbCpf.isChecked() && tirarMascaraCpfCnpj(getTextoCpfCnpj).length() != 11) {
            Toast.makeText(context, "Por favor, coloque um CPF válido!", Toast.LENGTH_SHORT).show();
        }else if (rdbCnpj.isChecked() && tirarMascaraCpfCnpj(getTextoCpfCnpj).length() != 14) {
            Toast.makeText(context, "Por favor, coloque um CNPJ válido!", Toast.LENGTH_SHORT).show();
        }else if(rdbNaoInformar.isChecked()){
            Intent intent = new Intent(context , TipoPagamentoPedidoActivity.class);
            intent.putExtra(CUPOM_FISCAL, true);
            context.startActivity(intent);
        }else{
            Intent intent = new Intent(context , TipoPagamentoPedidoActivity.class);
            intent.putExtra(CUPOM_FISCAL, true);

            if(rdbCpf.isChecked())
                intent.putExtra(CUPOM_CPF, getTextoCpfCnpj);
            else if(rdbCnpj.isChecked())
                intent.putExtra(CUPOM_CNPJ, getTextoCpfCnpj);

            context.startActivity(intent);
        }
    }

    private void voltarPagina(){
        finish();
    }
}