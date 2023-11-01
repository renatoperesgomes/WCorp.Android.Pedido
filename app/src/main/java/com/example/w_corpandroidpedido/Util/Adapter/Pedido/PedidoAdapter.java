package com.example.w_corpandroidpedido.Util.Adapter.Pedido;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.R;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {
    private Context context;
    private List<Pedido> items;

    public PedidoAdapter(Context context, List<Pedido> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.pedidos, parent, false);
        PedidoViewHolder holder = new PedidoViewHolder(itemLista);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapter.PedidoViewHolder holder, int position) {
        holder.idPedido.setText(String.valueOf(items.get(position).idPedido));
        holder.idMesa.setText(String.valueOf(items.get(position).idMesa));
        holder.nomeCliente.setText(items.get(position).nomeCliente);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class PedidoViewHolder extends RecyclerView.ViewHolder{
        TextView idPedido = itemView.findViewById(R.id.idPedido);
        TextView idMesa = itemView.findViewById(R.id.idMesa);
        TextView nomeCliente = itemView.findViewById(R.id.nomeCliente);
        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}