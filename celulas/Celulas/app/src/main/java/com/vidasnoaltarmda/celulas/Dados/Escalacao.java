package com.vidasnoaltarmda.celulas.Dados;

import java.io.Serializable;

/**
 * Created by thiago on 25/04/2016.
 */
public class Escalacao implements Serializable{
    private int idEscalacao;
    private String membro; //TODO fazer correspondencia com a tabela de usuarios no banco
    private String tarefa;

    public int getIdEscalacao() {
        return idEscalacao;
    }

    public void setIdEscalacao(int idEscalacao) {
        this.idEscalacao = idEscalacao;
    }

    public String getMembro() {
        return membro;
    }

    public void setMembro(String membro) {
        this.membro = membro;
    }

    public String getTarefa() {
        return tarefa;
    }

    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }

    @Override
    public String toString() {
        return getMembro() + " (" + (getTarefa()) + " )";
    }
}
