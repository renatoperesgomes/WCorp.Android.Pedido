package com.wcorp.w_corpandroidpedido.Models.BancoDados;

import com.wcorp.w_corpandroidpedido.Models.BaseApi;

public class BancoDados extends BaseApi {
    public Retorno retorno;
    public class Retorno{
        public Integer id;
        public String urlAcesso;
        public String codigoAutorizacao;
        public String chaveConsulta;
    }
}
