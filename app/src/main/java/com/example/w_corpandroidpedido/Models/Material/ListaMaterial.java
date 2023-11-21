package com.example.w_corpandroidpedido.Models.Material;

import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;


import java.util.List;

public class ListaMaterial {
    public List<Inconsistences> inconsistences;
    public boolean validated;
    public boolean hasInconsistence;
    public List<Retorno> retorno;

    public static class Retorno{
        public int id;
        public int idEmpresa;
        public int idNCM;
        public int idMaterialOrigem;
        public int idPaisFabricacao;
        public int idUnidade;
        public int idLocalArmazenagem;
        public int idOrdemProducaoGrupoProcesso;
        public int idFornecedorFabricante;
        public int idClienteDesenvolvido;
        public int idMaterialLayoutFichaTecnica;
        public boolean isKit;
        public boolean isKitCriarItemPedido;
        public boolean controlarEstoque;
        public boolean controlarLote;
        public boolean obrigarSeparacao;
        public boolean naoPermitirConsumoParcialLote;
        public boolean obrigarNumeroLote;
        public boolean pedidoSelecionarLoteFifo;
        public int pedidoSelecionarLoteTipo;
        public boolean pedidoSelecionarLoteOrdemProducaoFifo;
        public int pedidoSelecionarLoteOrdemProducaoTipo;
        public boolean habilitarControleInspecaoLote;
        public boolean controlarDataVencimento;
        public boolean precoEtiquetaCodigoBarras;
        public int campoAbastecerEstoqueOrdemProducao;
        public String codigo;
        public String nome;
        public String descricao;
        public String descricaoResumo;
        public String serie;
        public double pesoBruto;
        public double pesoLiquido;
        public double pesoEmbalagem;
        public int volumeInterno;
        public int pesoVolumeInterno;
        public double preco;
        public double precoEspecial;
        public int tipoEnum;
        public double quantidadeMinimaEstoque;
        public double quantidadeMinimaCompra;
        public double quantidadeMinimaVenda;
        public double valorMultiploVenda;
        public int status;
        public int percDiferencaEntradaMaterial;
        public double fatorConversaoVolumeNotaFiscal;
        public String responsavelComposicao;
        public String dataFormulacao;
        public int gramaMetro;
        public int rowCheckId;
        public int tipoAtualizacaoCustoMaterialEntradaNotaFiscalEnum;
        public double custoUnitario;
        public int icmsSt;
        public int frete;
        public int percIpi;
        public double custoUnitarioTotal;
        public boolean calcularPreco;
        public int tipoPrecoEnum;
        public double percDespesasFixas;
        public int percCustoComercializacao;
        public double percMargemLucro;
        public double markup;
        public int percMargemLiquida;
        public double lucroLiquido;
        public int idMagento;
        public String ean;
        public String eanTributario;
        public int codigoAnp;
        public boolean calcularCustoComposicao;
        public int tipoCalcularCustoComposicao;
        public double alturaEmbalagemMetros;
        public int larguraEmbalagemMetros;
        public int comprimentoEmbalagemMetros;
        public boolean imprimirEtiqueta;
        public int modeloEtiquetaEnum;
        public int diasVencimento;
        public boolean controlarNumeroSerieLote;
        public int pedidoIndustrializacaoMateriaPrimaEnum;
    }
}
