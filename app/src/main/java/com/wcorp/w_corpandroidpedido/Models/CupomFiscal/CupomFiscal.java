package com.wcorp.w_corpandroidpedido.Models.CupomFiscal;

import com.wcorp.w_corpandroidpedido.Models.BaseApi;

import java.util.ArrayList;
import java.util.Date;

public class CupomFiscal extends BaseApi {
        public Retorno retorno;
        public class Retorno {
            public int id;
            public int idEmpresa;
            public int idNaturezaOperacao;
            public int idCliente;
            public int idPedido;
            public int idOrdemServico;
            public boolean calcularAutomaticamente;
            public int numero;
            public String dataHoraEmissao;
            public String destinatarioNome;
            public String destinatarioCpf;
            public String destinatarioCnpj;
            public double totalBaseCalculoIcms;
            public double totalBaseCalculoPis;
            public double totalBaseCalculoCofins;
            public double totalBaseCalculoIpi;
            public double totalValorIcms;
            public double totalValorPis;
            public double totalValorCofins;
            public double totalValorIpi;
            public double totalBaseCalculoStIcms;
            public double totalBaseCalculoStPis;
            public double totalBaseCalculoStCofins;
            public double totalValorStIcms;
            public double totalValorIcmsUfDestino;
            public double totalValorIcmsUfRemetente;
            public double totalValorStPis;
            public double totalValorStCofins;
            public double totalProdutos;
            public double totalDesconto;
            public double totalNotaFiscal;
            public double totalFrete;
            public double totalDespesas;
            public double totalSeguro;
            public double totalAproximadoTributos;
            public String observacao;
            public int statusCupomFiscal;
            public int status;
            public String chaveAcessoCfe;
            public String xmlCfe;
            public String assinaturaQrCode;
            public String xmlCancelamentoCfe;
            public String mensagemRejeicaoCfe;
            public String motivoCancelamentoCfe;
            public String mensagemRejeicaoCancelamentoSat;
            public String mensagemErroCriarXmlSat;
            public String mensagemErroSat;
            public int tipoCriacaoCupomFiscalEnum;
            public ArrayList<CupomFiscalItem> listCupomFiscalItem;
            public int rowCheckId;
        }
}