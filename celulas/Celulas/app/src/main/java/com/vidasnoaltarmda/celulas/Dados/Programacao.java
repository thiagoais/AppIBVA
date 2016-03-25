package com.vidasnoaltarmda.celulas.Dados;

import java.sql.Blob;

/**
 * Created by barque on 20/03/2016.
 */
public class Programacao {

    private int id_programacao;
    private int id_celula;
    private String nome;
    private String data_prog;
    private String horario;
    private String local_prog;
    private String telefone;
    private String valor;
    private Blob imagem;

    public int getId_programacao() {
        return id_programacao;
    }

    public void setId_programacao(int id_programacao) {
        this.id_programacao = id_programacao;
    }

    public int getId_celula() {
        return id_celula;
    }

    public void setId_celula(int id_celula) {
        this.id_celula = id_celula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData_prog() {
        return data_prog;
    }

    public void setData_prog(String data_prog) {
        this.data_prog = data_prog;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal_prog() {
        return local_prog;
    }

    public void setLocal_prog(String local_prog) {
        this.local_prog = local_prog;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Blob getImagem() {
        return imagem;
    }

    public void setImagem(Blob imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
