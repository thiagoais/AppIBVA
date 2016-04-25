package com.vidasnoaltarmda.celulas.Dados;

/**
 * Created by thiago on 06/03/2016.
 */
public class Usuario {
    public static final int PERMISSAO_BASICA = 1;

    private int id;
    private String nome;
    private String senha;
    private String sobrenome;
    private String dataNascimento;
    private Celula celula;
    //private Escala escala; //TODO
    private String login;
    private int permissao;

    public Usuario() {
        permissao = PERMISSAO_BASICA;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Celula getCelula() {
        return celula;
    }

    public void setCelula(Celula celula) {
        this.celula = celula;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPermissao() {
        return permissao;
    }

    public void setPermissao(int permissao) {
        this.permissao = permissao;
    }

    @Override
    public String toString() {
        return getNome() +  " "+getSobrenome() + " - Dia " + getDataNascimento();
    }
}
