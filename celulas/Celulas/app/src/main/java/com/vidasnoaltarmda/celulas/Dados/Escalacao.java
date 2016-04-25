package com.vidasnoaltarmda.celulas.Dados;

/**
 * Created by thiago on 25/04/2016.
 */
public class Escalacao {
    private int idEscalacao;
    private String membro;
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
