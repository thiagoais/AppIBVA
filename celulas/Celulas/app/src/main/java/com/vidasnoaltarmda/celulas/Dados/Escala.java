package com.vidasnoaltarmda.celulas.Dados;

/**
 * Created by barque on 20/03/2016.
 */
public class Escala {
    private int id_escala;
    private String data_celula;
    private String hora_celula;
    private String local_celula;
    private String membro;
    private int item_responsavel;

    public int getId_escala() {
        return id_escala;
    }

    public void setId_escala(int id_escala) {
        this.id_escala = id_escala;
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

    public String getMembro() {
        return membro;
    }

    public void setMembro(String membro) {
        this.membro = membro;
    }

    public int getItem_responsavel() {
        return item_responsavel;
    }

    public void setItem_responsavel(int item_responsavel) {
        this.item_responsavel = item_responsavel;
    }
}