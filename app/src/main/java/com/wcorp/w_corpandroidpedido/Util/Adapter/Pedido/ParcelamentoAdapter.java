package com.wcorp.w_corpandroidpedido.Util.Adapter.Pedido;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.IniciarDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wcorp.w_corpandroidpedido.Atividades.Pedido.PagamentoPedidoActivity;
import com.wcorp.w_corpandroidpedido.Atividades.Pedido.ParcelamentoActivity;
import com.wcorp.w_corpandroidpedido.Atividades.Pedido.TipoPagamentoPedidoActivity;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.InfoPagamento;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;

import java.text.DecimalFormat;
import java.util.ArrayList;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInitializationResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInstallment;

public class ParcelamentoAdapter extends RecyclerView.Adapter<ParcelamentoAdapter.ParcelamentoViewHolder> {
    private Context context;
    private int valorTotal;
    private ArrayList<Double> valoresParcelados;
    private Integer nmrParcela = 2;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    public ParcelamentoAdapter(Context context, Integer valorTotal ,ArrayList<Double> valoresParcelados){
        this.context = context;
        this.valoresParcelados = valoresParcelados;
        this.valorTotal = valorTotal;
    }

    @NonNull
    @Override
    public ParcelamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.card_parcelamento, parent, false);
        return new ParcelamentoViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ParcelamentoViewHolder holder, int position) {
        holder.itemView.setTag(nmrParcela);
        holder.txtNmrParcela.setText(nmrParcela + " x ");
        double valorParcelado = valoresParcelados.get(position);
        String valorParceladoConvertidoDec = decimalFormat.format(valorParcelado).replace(".", ",");
        holder.txtValorParcela.setText(valorParceladoConvertidoDec);

        holder.itemView.setOnClickListener(view ->{
            View btnCardView = holder.itemView;
            btnCardView.setClickable(false);
            int nmrParcela = holder.itemView.getTag().hashCode();

            new ParcelamentoActivity().ChamarPagamento(context, btnCardView,PlugPag.TYPE_CREDITO, valorTotal, PlugPag.INSTALLMENT_TYPE_PARC_VENDEDOR, nmrParcela);
         });

        nmrParcela++;
    }

    @Override
    public int getItemCount() {
        return valoresParcelados.size();
    }

    static class ParcelamentoViewHolder extends RecyclerView.ViewHolder{
        TextView txtNmrParcela = itemView.findViewById(R.id.txtNmrParcelamento);
        TextView txtValorParcela = itemView.findViewById(R.id.txtValorParcelado);

        public ParcelamentoViewHolder(@NonNull View itemView) {super(itemView);}
    }
}
