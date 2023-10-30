package com.example.w_corpandroidpedido.Models.Empresa;

import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Empresa {
    public List<Inconsistences> inconsistences;
    public boolean validated;
    public boolean hasInconsistence;
    public List<Retorno> retorno;

    public class Retorno{
        public int id;
        public int idMunicipio;
        public int numeroNfatual;
        public int numeroCtatual;
        public int numeroMdfatual;
        public int numeroNfsatual;
        public int numeroEntradaNotaFiscalRadar;
        public String dataHoraUltimoSincronismoRadar;
        public int serieCt;
        public int serieMdf;
        public String nomeRazaoSocial;
        public String nomeFantasia;
        public String cnpj;
        public int tipoRegimeTributario;
        public String dataAdesaoSimplesNacional;
        public String cnaefiscal;
        public String inscricaoEstadual;
        public String inscricaoMunicipal;
        public boolean isentoInscricaoEstadual;
        public int status;
        public String logradouro;
        public String numero;
        public String bairro;
        public String complemento;
        public String cep;
        public int idMunicipioEntrega;
        public String logradouroEntrega;
        public String enderecoNumeroEntrega;
        public String bairroEntrega;
        public String cepEntrega;
        public String complementoEntrega;
        public String telefone;
        public String email;
        public String observacao;
        public String codigoAtivacaoSat;
        public String assinaturaSat;
        public int perfilBloco000SpedEnum;
        public int atividadeBloco000SpedEnum;
        public int rowCheckId;
    }
}
