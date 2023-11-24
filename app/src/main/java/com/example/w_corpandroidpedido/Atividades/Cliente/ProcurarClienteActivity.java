package com.example.w_corpandroidpedido.Atividades.Cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Util.Adapter.Cliente.Cliente;
import com.example.w_corpandroidpedido.Util.Adapter.Cliente.ProcurarClienteAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProcurarClienteActivity extends AppCompatActivity {
    private RecyclerView getRecycleCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurar_cliente);

        List<Cliente> clientes = new ArrayList<>();
        getRecycleCliente = findViewById(R.id.viewCliente);

        getRecycleCliente.setLayoutManager(new LinearLayoutManager(ProcurarClienteActivity.this, LinearLayoutManager.VERTICAL, false));
        getRecycleCliente.setHasFixedSize(true);

        SearchView buscaCliente = findViewById(R.id.buscaCliente);
        buscaCliente.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                return false;
            }
        });

        Cliente cliente = new Cliente(0, "Gabriel");
        Cliente cliente2 = new Cliente(1, "Jordan");
        Cliente cliente3 = new Cliente(2, "Felix");

        clientes.add(0, cliente);
        clientes.add(1, cliente2);
        clientes.add(2, cliente3);

        try {
            runOnUiThread(() -> getRecycleCliente.setAdapter(new ProcurarClienteAdapter(this, clientes)));
        } catch (Exception e) {
            System.out.println("Erro :" + e.getMessage());
        }
    }

}