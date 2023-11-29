package com.example.w_corpandroidpedido.Models.Material;

import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;

import java.util.List;

public class MaterialCategoria {
        public int id;
        public int idEmpresa;
        public int idPai;
        public String nome;
        public String descricao;
        public String campoEtiqueta;
        public boolean visualizaEntrada;
        public boolean visualizaVendas;
        public boolean visualizaFaturamento;
        public boolean visualizaContrato;
        public boolean visualizaCompras;
        public boolean visualizaServico;
        public boolean visualizaProducao;
        public boolean visualizaPdv;
        public boolean pdvMultiplaSelecao;
        public int pdvMultiplaSelecaoQuantidade;
        public boolean pdvComboCategoriaFilho;
        public boolean utilizarOrdenacaoImpressao;
        public int idMagento;
        public int status;
        public int rowCheckId;
}
