package com.vidasnoaltarmda.celulas.Dados;

import java.util.ArrayList;

public class Escala {
    private Integer id_escala;
    private Integer id_celula;
    private String data_celula;
    private String hora_celula;
    private String local_celula;
    private ArrayList<Escalacao> escalacoes;

    public Escala() {
        escalacoes = new ArrayList<Escalacao>();
    }

    public Integer getId_escala() {
        return id_escala;
    }

    public void setId_escala(Integer id_escala) {
        this.id_escala = id_escala;
    }

    public Integer getId_celula() {
        return id_celula;
    }

    public void setId_celula(Integer id_celula) {
        this.id_celula = id_celula;
    }

    public String getData_celula() {
        return data_celula;
    }

    public void setData_celula(String data_celula) {
        this.data_celula = data_celula;
    }

    public String getHora_celula() {
        return hora_celula;
    }

    public void setHora_celula(String hora_celula) {
        this.hora_celula = hora_celula;
    }

    public String getLocal_celula() {
        return local_celula;
    }

    public void setLocal_celula(String local_celula) {
        this.local_celula = local_celula;
    }

    public ArrayList<Escalacao> getEscalacoes() {
        return escalacoes;
    }

    public void setEscalacoes(ArrayList<Escalacao> escalacoes) {
        this.escalacoes = escalacoes;
    }

}
