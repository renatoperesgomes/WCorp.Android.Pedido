package com.example.w_corpandroidpedido.Util.Adapter.Empresa;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.w_corpandroidpedido.Models.Empresa.Empresa;

import java.util.List;

public class EmpresaAdapter extends BaseAdapter {
    private Context context;
    private List<Empresa> empresas;

    public EmpresaAdapter(Context context, List<Empresa> empresas) {
        this.context = context;
        this.empresas = empresas;
    }

    @Override
    public int getCount(){
        return empresas.size();
    }

    @Override
    public Empresa getItem(int position){
        return empresas.get(position);
    }

    @Override
    public long getItemId(int position){
        return empresas.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(empresas.get(position).nomeFantasia);
        return label;
    }
}
