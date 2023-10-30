package com.example.w_corpandroidpedido.Util.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_corpandroidpedido.Atividades.Categoria.SubCategoriaActivity;
import com.example.w_corpandroidpedido.Atividades.Material.MaterialActivity;
import com.example.w_corpandroidpedido.Models.Material.Material;
import com.example.w_corpandroidpedido.R;
import com.example.w_corpandroidpedido.Util.Enum.ViewType;


public class VoltarAdapter extends RecyclerView.Adapter<VoltarAdapter.VoltarViewHolder> {
    private final Context context;
    private final SubCategoriaActivity classeSubCategoria;
    private final MaterialActivity classeMaterial;
    private final int viewType;
    public VoltarAdapter(Context context, SubCategoriaActivity classeSubCategoria, MaterialActivity classeMaterial ,int viewType){
        this.context = context;
        this.classeSubCategoria = classeSubCategoria;
        this.classeMaterial = classeMaterial;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public VoltarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View voltarView = LayoutInflater.from(context).inflate(R.layout.card_voltar, parent, false);
        VoltarViewHolder holder = new VoltarViewHolder(voltarView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VoltarViewHolder holder, int position) {
        if(viewType == ViewType.SUB_CATEGORIA.ordinal()){
            holder.itemView.setOnClickListener(view ->{
                classeSubCategoria.finish();
            });
        }else if(viewType == ViewType.MATERIAL.ordinal()){
            holder.itemView.setOnClickListener(view ->{
                classeMaterial.finish();
            });
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class VoltarViewHolder extends RecyclerView.ViewHolder{
        public VoltarViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
