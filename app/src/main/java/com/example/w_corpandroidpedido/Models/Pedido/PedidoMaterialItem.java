package com.example.w_corpandroidpedido.Models.Pedido;

import com.example.w_corpandroidpedido.Models.Material.Material;

public class PedidoMaterialItem {
    public int id;
    public int idMaterial;
    public Material material;
    public double valorUnitario;
    public int quantidade;
    public int percDesconto;
    public int valorDesconto;
    public double valorTotalDesconto;
    public int valorTotalDescontoImpostos;
    public int statusPedidoMaterialItem;
    public String observacao;
    public boolean bonificacao;
}
