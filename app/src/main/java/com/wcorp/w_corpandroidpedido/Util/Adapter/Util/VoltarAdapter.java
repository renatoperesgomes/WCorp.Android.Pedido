package com.wcorp.w_corpandroidpedido.Util.Adapter.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wcorp.w_corpandroidpedido.Atividades.Categoria.SubCategoriaActivity;
import com.wcorp.w_corpandroidpedido.Atividades.Material.MaterialActivity;
import com.wcorp.w_corpandroidpedido.R;
import com.wcorp.w_corpandroidpedido.Util.Enum.ViewType;


public class VoltarAdapter extends RecyclerView.Adapter<VoltarAdapter.VoltarViewHolder> {
    private final Context context;
    private final SubCategoriaActivity classeSubCategoriaActivity;
    private final MaterialActivity classeMaterialActivity;
    private final int viewType;
    public VoltarAdapter(Context context, SubCategoriaActivity classeSubCategoriaActivity, int viewType){
        this.context = context;
        this.classeSubCategoriaActivity = classeSubCategoriaActivity;
        this.classeMaterialActivity = null;
        this.viewType = viewType;
    }

    public VoltarAdapter(Context context, MaterialActivity classeMaterialActivity, int viewType){
        this.context = context;
        this.classeSubCategoriaActivity = null;
        this.classeMaterialActivity = classeMaterialActivity;
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
                classeSubCategoriaActivity.finish();
            });
        }else if(viewType == ViewType.MATERIAL.ordinal()){
            holder.itemView.setOnClickListener(view ->{
                classeMaterialActivity.finish();
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
