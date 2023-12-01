package com.example.w_corpandroidpedido.Models.Pedido;

import com.example.w_corpandroidpedido.Models.Material.Material;

public class PedidoMaterialItem {
    public int id;
    public int idMaterial;
    public Material material;
    public double valorUnitario;
    public double quantidade;
    public int percDesconto;
    public double valorDesconto;
    public double valorTotalDesconto;
    public double valorTotalDescontoImpostos;
    public int statusPedidoMaterialItem;
    public String observacao;
    public boolean bonificacao;
}
