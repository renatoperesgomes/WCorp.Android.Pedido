package com.wcorp.w_corpandroidpedido.Util.Adapter.Pedido;

import static com.wcorp.w_corpandroidpedido.Util.Pagamento.DialogPagamento.IniciarDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wcorp.w_corpandroidpedido.Atividades.Pedido.TipoPagamentoPedidoActivity;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.InfoPagamento;
import com.wcorp.w_corpandroidpedido.Util.Pagamento.PagamentoCall;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;

public class ParcelamentoAdapter extends RecyclerView.Adapter<ParcelamentoAdapter.ParcelamentoViewHolder> {
    private Context context;
    private int valorTotal;
    private ArrayList<Double> valoresParcelados;
    private Integer nmrParcela = 2;
    private NumberFormat formatNumero = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private Boolean firstOpen = true;
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

    @Override
    public void onBindViewHolder(@NonNull ParcelamentoViewHolder holder, int position) {
        holder.itemView.setTag(nmrParcela);
        holder.txtNmrParcela.setText(String.valueOf(nmrParcela));
        double valorParcelado = valoresParcelados.get(position);
        String valorParceladoFormatado = formatNumero.format(valorParcelado);
        holder.txtValorParcela.setText(valorParceladoFormatado);

        holder.itemView.setOnClickListener(view ->{
            holder.cardParcelamento.setCardBackgroundColor(Color.parseColor("#bfbfbf"));
            holder.btnCard.setClickable(false);
            int nmrParcela = holder.itemView.getTag().hashCode();
            Intent intent = new Intent(context, TipoPagamentoPedidoActivity.class);
            intent.putExtra("isParcelado", true);
            intent.putExtra("tipoPagamento", PlugPag.TYPE_CREDITO);
            intent.putExtra("tipoParcela", PlugPag.INSTALLMENT_TYPE_PARC_VENDEDOR);
            intent.putExtra("valorTotal", valorTotal);
            intent.putExtra("nmrParcela", nmrParcela);
            context.startActivity(intent);
         });
        nmrParcela++;
    }

    @Override
    public int getItemCount() {
        return valoresParcelados.size();
    }

    static class ParcelamentoViewHolder extends RecyclerView.ViewHolder{
        View btnCard = itemView;
        CardView cardParcelamento = itemView.findViewById(R.id.cardParcelamento);
        TextView txtNmrParcela = itemView.findViewById(R.id.txtNmrParcelamento);
        TextView txtValorParcela = itemView.findViewById(R.id.txtValorParcelado);

        public ParcelamentoViewHolder(@NonNull View itemView) {super(itemView);}
    }
}
