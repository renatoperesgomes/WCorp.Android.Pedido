package com.example.w_corpandroidpedido.Atividades.Pedido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Util.Adapter.Pedido.Pedido;
import com.example.w_corpandroidpedido.Util.Adapter.Pedido.PedidoAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListaPedidosActivity extends AppCompatActivity {
    private RecyclerView getRecycleListaPedido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        List<Pedido> pedidos = new ArrayList<>();
        getRecycleListaPedido = findViewById(R.id.viewListaPedidos);
        getRecycleListaPedido.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        getRecycleListaPedido.setHasFixedSize(true);

        Pedido pedido = new Pedido(0, 0, "Gabriel");
        Pedido pedido2 = new Pedido(1, 1, "Jordan");
        Pedido pedido3 = new Pedido(2, 2, "Felix");

        pedidos.add(0, pedido);
        pedidos.add(1, pedido2);
        pedidos.add(2, pedido3);

        try {
            runOnUiThread(() -> getRecycleListaPedido.setAdapter(new PedidoAdapter(this, pedidos)));
        } catch (Exception e) {
            System.out.println("Erro :" + e.getMessage());
        }
    }
}