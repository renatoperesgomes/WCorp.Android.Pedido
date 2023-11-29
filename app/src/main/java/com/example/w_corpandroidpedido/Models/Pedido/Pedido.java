package com.example.w_corpandroidpedido.Models.Pedido;

import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;
import com.example.w_corpandroidpedido.Models.Material.Material;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
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
        public ArrayList<PedidoMaterialItem> listPedidoMaterialItem;
}
