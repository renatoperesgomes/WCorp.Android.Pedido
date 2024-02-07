package com.wcorp.w_corpandroidpedido.Models.CupomFiscal;

import com.wcorp.w_corpandroidpedido.Models.Material.Material;

public class CupomFiscalItem {
    public int id;

    public int idCupomFiscal;

    public int idMaterial;

    public String descricao;

    public String codigo;

    public double quantidade;

    public String unidade;

    public double valorUnitario;

    public double valorDesconto;

    public double valorDespesas;

    public double valorFrete;

    public double valorTotal;

    public double valorAproximadoTributos;

    public String ncm;

    public String pedidoCompra;

    public String numeroItemPedidoCompra;
    public Material material;
}
