package com.wcorp.w_corpandroidpedido.Atividades.CupomFiscal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.wcorp.w_corpandroidpedido.Atividades.Pedido.TipoPagamentoPedidoActivity;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Impressora.GerarBitmap;
import com.wcorp.w_corpandroidpedido.Util.Util;

public class CupomFiscalActivity extends AppCompatActivity {

    private Button btnEmitirCupom;
    private Button btnVoltar;
    private EditText txtCpfCnpj;
    private RadioButton rdbCpf;
    private RadioButton rdbCnpj;
    public static final String CUPOM_FISCAL = "com.example.w_corpandroidpedido.CUPOMFISCAL";
    private static final String mascaraCNPJ = "##.###.###/####-##";
    private static final String mascaraCPF = "###.###.###-##";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupom_fiscal);

        btnEmitirCupom = findViewById(R.id.btnEmitirCupom);
        btnVoltar = findViewById(R.id.btnVoltar);
        txtCpfCnpj = findViewById(R.id.edtCpfCnpj);
        rdbCpf = findViewById(R.id.rdbCpf);
        rdbCpf.setChecked(true);
        rdbCnpj = findViewById(R.id.rdbCnpj);

        addMascaraDoc();
        btnEmitirCupom.setOnClickListener(view ->{
            emitirCupom(this);
        });

        btnVoltar.setOnClickListener(view ->{
            voltarPagina();
        });
    }

    private static String tirarMascaraCpfCnpj(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    private void addMascaraDoc(){
        txtCpfCnpj.addTextChangedListener(new TextWatcher() {
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
                txtCpfCnpj.setText(mascara);
                txtCpfCnpj.setSelection(mascara.length());
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
    private void emitirCupom(Context context) {
        boolean isCpf = true;
        String getTextoCpfCnpj = txtCpfCnpj.getText().toString();
        if (rdbCpf.isChecked() && tirarMascaraCpfCnpj(getTextoCpfCnpj).length() != 11) {
            Toast.makeText(context, "Por favor, coloque um CPF válido!", Toast.LENGTH_SHORT).show();
        } else if (rdbCnpj.isChecked() && tirarMascaraCpfCnpj(getTextoCpfCnpj).length() != 14) {
            Toast.makeText(context, "Por favor, coloque um CNPJ válido!", Toast.LENGTH_SHORT).show();
        }else{
            String nmrCpfCnpj = txtCpfCnpj.getText().toString();

            if(rdbCnpj.isChecked())
                isCpf = false;

            Bitmap bitmap = GerarBitmap.GerarBitmapCupomFiscal(context, nmrCpfCnpj, isCpf);
            Util.SalvarImagemEmExternalStorage(context, bitmap, "receiptCupomFiscal.bmp");

            Intent intent = new Intent(context , TipoPagamentoPedidoActivity.class);
            intent.putExtra(CUPOM_FISCAL, true);
            context.startActivity(intent);
        }
    }

    private void voltarPagina(){
        finish();
    }
}