package com.example.w_corpandroidpedido.Models.Pedido;

import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.example.w_corpandroidpedido.Models.Material.Material;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    public List<Inconsistences> inconsistences;
    public boolean validated;
    public boolean hasInconsistence;
    public Retorno retorno;

    public class Retorno{
        public int id;
        public int idEmpresa;
        public String dataPedido;
        public int statusPedido;
        public String observacao;
        public String observacaoCliente;
        public String observacaoInterna;
        public double valorFrete;
        public double valorTotalProduto;
        public double valorTotalPedido;
        public int origemPedido;
        public int tipoPedidoEnum;
        public int status;
        public String comanda;
        public String dataFechamento;
        public ArrayList<ListPedidoMaterialItem> listPedidoMaterialItem;
    }

    public class ListPedidoMaterialItem{
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
}
