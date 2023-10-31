package com.example.w_corpandroidpedido.Models.Usuario;

import com.example.w_corpandroidpedido.Models.Inconsistences.Inconsistences;

import java.util.List;

public class ListaUsuario {
    public List<Inconsistences> inconsistences;
    public boolean validated;
    public boolean hasInconsistence;
    public List<Retorno> retorno;

    public static class Retorno{
        public int id;
        public int idEmpresa;
        public int idUsuarioGrupo;
        public int idFuncionarioVendedor;

        public int idCliente;
        public String nome;
        public String login;
        public String senha;
        public String codigoCracha;
        public int status;
        public String telefone;
        public String email;
        public boolean acessoWebApp;
        public int rowCheckId;
    }
}
