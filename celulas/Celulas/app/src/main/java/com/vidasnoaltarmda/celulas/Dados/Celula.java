package com.vidasnoaltarmda.celulas.Dados;


import java.sql.Blob;

public class Celula {
    private int id_celula;
    private String nome;
    private String lider;
    private String dia;
    private String horario;
    private String local_celula;
    private int dia_jejum;
    private Enum periodo;
    private String versiculo;
    private Blob imagem;


    public Blob getImagem() {
        return imagem;
    }

    public void setImagem(Blob imagem) {
        this.imagem = imagem;
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

    public String getLider() {
        return lider;
    }

    public void setLider(String lider) {
        this.lider = lider;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal_celula() {
        return local_celula;
    }

    public void setLocal_celula(String local_celula) {
        this.local_celula = local_celula;
    }

    public int getDia_jejum() {
        return dia_jejum;
    }

    public void setDia_jejum(int dia_jejum) {
        this.dia_jejum = dia_jejum;
    }

    public Enum getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Enum periodo) {
        this.periodo = periodo;
    }

    public String getVersiculo() {
        return versiculo;
    }

    public void setVersiculo(String versiculo) {
        this.versiculo = versiculo;
    }
}
