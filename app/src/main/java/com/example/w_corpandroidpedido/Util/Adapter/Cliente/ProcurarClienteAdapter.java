package com.example.w_corpandroidpedido.Util.Adapter.Cliente;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.R;

import java.util.List;

public class ProcurarClienteAdapter extends RecyclerView.Adapter<ProcurarClienteAdapter.ClienteViewHolder> {
    private Context context;
    private List<Cliente> items;
    public ProcurarClienteAdapter(Context context, List<Cliente> items){
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.clientes, parent, false);
        ClienteViewHolder holder = new ClienteViewHolder(itemLista);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        holder.idCliente.setText(String.valueOf(items.get(position).Id));
        holder.nomeCliente.setText(items.get(position).nomeCliente);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ClienteViewHolder extends RecyclerView.ViewHolder{
        TextView idCliente = itemView.findViewById(R.id.Id);
        TextView nomeCliente = itemView.findViewById(R.id.nomeCliente);
        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
