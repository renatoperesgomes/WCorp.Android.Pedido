package com.wcorp.w_corpandroidpedido.Models.Material;

import java.io.Serializable;

public class MaterialCategoria implements Serializable {
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
